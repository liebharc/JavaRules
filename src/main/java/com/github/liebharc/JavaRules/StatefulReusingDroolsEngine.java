package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.verbs.Verb;
import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSessionsPool;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;
import java.net.URL;

public class StatefulReusingDroolsEngine implements Engine {

    private final DataAccess store;
    private final ReportStore reports;
    private final KieSessionsPool kieSessionsPool;

    private Logger logger = new Logger(StatefulReusingDroolsEngine.class);

    public StatefulReusingDroolsEngine(DataAccess store, ReportStore reports) {
        this.store = store;
        this.reports = reports;
        try {
            KnowledgeBuilderImpl kbuilder = (KnowledgeBuilderImpl) KnowledgeBuilderFactory.newKnowledgeBuilder();
            ClassLoader classloader = Thread
                .currentThread()
                .getContextClassLoader();
            URL resource = classloader.getResource(getDroolsFile());
            kbuilder.add(ResourceFactory.newInputStreamResource(resource.openStream()), ResourceType.DRL);
            if (kbuilder.hasErrors()) {
                throw new IllegalStateException("Can not initialize Drools: " + kbuilder
                    .getErrors()
                    .toString());
            }

            final KnowledgeBaseImpl kieBase = (KnowledgeBaseImpl) kbuilder.newKnowledgeBase(RuleBaseConfigurationProvider.createRuleBaseConfiguration(false));
            kieSessionsPool = kieBase.newKieSessionsPool(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getDroolsFile() {
        return "rules.drl";
    }

    @Override
    public void process(Verb verb) {
        final StatefulKnowledgeSessionImpl kieSession = (StatefulKnowledgeSessionImpl) kieSessionsPool.newKieSession();
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
