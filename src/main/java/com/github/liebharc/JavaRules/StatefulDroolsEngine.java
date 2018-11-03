package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.verbs.Verb;
import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionsPool;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;
import java.net.URL;

public class StatefulDroolsEngine implements Engine {

    private final DataStore store;
    private final ReportStore reports;
    private final KieSessionsPool kieSessionsPool;

    private KnowledgeBaseImpl kieBase;

    private Logger logger = new Logger(StatefulDroolsEngine.class);

    public StatefulDroolsEngine(DataStore store, ReportStore reports) {

        this.store = store;
        this.reports = reports;

        try {
            KnowledgeBuilderImpl kbuilder = (KnowledgeBuilderImpl)KnowledgeBuilderFactory.newKnowledgeBuilder();
            ClassLoader classloader = Thread
                .currentThread()
                .getContextClassLoader();
            URL resource = classloader.getResource("rules.drl");
            kbuilder.add(ResourceFactory.newInputStreamResource(resource.openStream()), ResourceType.DRL);
            if (kbuilder.hasErrors()) {
                throw new IllegalStateException("Can not initialize Drools: " + kbuilder
                    .getErrors()
                    .toString());
            }

            kieBase = (KnowledgeBaseImpl)kbuilder.newKnowledgeBase(RuleBaseConfigurationProvider.createRuleBaseConfiguration(false));
            kieSessionsPool = kieBase.newKieSessionsPool(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void process(Verb verb) {
        KieSession kieSession = kieSessionsPool.newKieSession();
        try {
            kieSession.setGlobal("logger", logger);
            kieSession.insert(verb);
            kieSession.insert(store);
            kieSession.setGlobal("reports", reports);
            kieSession.fireAllRules();
        } finally {
            kieSession.dispose();
        }
    }
}
