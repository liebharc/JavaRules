package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.verbs.Verb;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;
import java.net.URL;

public class JbpmDroolsEngine implements Engine {

    private final DataStore store;
    private final ReportStore reports;

    private KieSession kieSession;

    private Logger logger = new Logger(StatefulReusingDroolsEngine.class);

    public JbpmDroolsEngine(DataStore store, ReportStore reports) {
        this.store = store;
        this.reports = reports;

        try {
            KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            ClassLoader classloader = Thread
                    .currentThread()
                    .getContextClassLoader();
            kbuilder.add(ResourceFactory.newInputStreamResource( classloader.getResource("globals.drl").openStream()), ResourceType.DRL);
            kbuilder.add(ResourceFactory.newInputStreamResource(classloader.getResource("Flow.bpmn2").openStream()), ResourceType.BPMN2);
            if (kbuilder.hasErrors()) {
                throw new IllegalStateException("Can not initialize Drools: " + kbuilder
                        .getErrors()
                        .toString());
            }

            final KieBase kieBase = kbuilder.newKieBase();
            kieSession = kieBase.newKieSession();
            kieSession.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void process(Verb verb) {
        ((StatefulKnowledgeSessionImpl)kieSession).reset();
        try {
            kieSession.setGlobal("logger", logger);
            kieSession.insert(verb);
            kieSession.insert(store);
            kieSession.setGlobal("reports", reports);
            kieSession.startProcess("com.github.liebharc.JavaRules.ScriptTaskTest");
        } finally {
            kieSession.dispose();
        }

        Rules.INSTANCE.process(verb, store, logger, reports);
    }
}
