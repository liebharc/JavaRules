package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.verbs.Verb;
import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.core.common.InternalAgenda;
import org.drools.core.common.InternalWorkingMemoryEntryPoint;
import org.drools.core.common.NodeMemories;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.drools.core.runtime.process.InternalProcessRuntime;
import org.drools.core.spi.FactHandleFactory;
import org.drools.core.spi.GlobalResolver;
import org.drools.core.time.TimerService;
import org.kie.api.KieBase;
import org.kie.api.event.kiebase.KieBaseEventListener;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class JbpmDroolsEngine implements Engine {

    private final DataStore store;
    private final ReportStore reports;
    private final CustomSessionResetter resetter;

    private KieSession kieSession;

    private Logger logger = new Logger(StatefulReusingDroolsEngine.class);

    public JbpmDroolsEngine(DataStore store, ReportStore reports) {
        this.store = store;
        this.reports = reports;

        try {
            KnowledgeBuilderImpl kbuilder = (KnowledgeBuilderImpl)KnowledgeBuilderFactory.newKnowledgeBuilder();
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

            final KieBase kieBase = kbuilder.newKnowledgeBase(RuleBaseConfigurationProvider.createRuleBaseConfiguration(false));
            kieSession = kieBase.newKieSession();
            kieSession.dispose();
            resetter = new CustomSessionResetter((InternalKnowledgeBase)kieBase);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void process(Verb verb) {
        ((StatefulKnowledgeSessionImpl) kieSession).reset();
        try {
            kieSession.setGlobal("logger", logger);
            kieSession.insert(verb);
            kieSession.insert(store);
            kieSession.setGlobal("reports", reports);
            kieSession.startProcess("com.github.liebharc.JavaRules.ScriptTaskTest");
        } finally {
            kieSession.dispose();
        }
    }
}
