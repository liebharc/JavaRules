package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.verbs.Verb;
import org.drools.core.SessionConfiguration;
import org.drools.core.command.runtime.rule.FireAllRulesCommand;
import org.drools.core.command.runtime.rule.InsertObjectCommand;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.kie.api.KieBase;
import org.kie.api.command.Command;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.KieSessionsPool;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderConfiguration;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.command.CommandFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class StatelessDroolsEngine implements Engine {

    private final DataStore store;
    private final ReportStore reports;
    private final KieSessionsPool kieSessionsPool;

    private KnowledgeBaseImpl kieBase;

    private Logger logger = new Logger(StatelessDroolsEngine.class);

    public StatelessDroolsEngine(DataStore store, ReportStore reports) {

        this.store = store;
        this.reports = reports;

        try {
            KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            URL resource = classloader.getResource("rules.drl");
            kbuilder.add(ResourceFactory.newInputStreamResource(resource.openStream()), ResourceType.DRL);
            if (kbuilder.hasErrors()) {
                throw new IllegalStateException("Can not initialize Drools: " + kbuilder.getErrors().toString());
            }

            kieBase = (KnowledgeBaseImpl)kbuilder.newKieBase();
            kieSessionsPool = kieBase.newKieSessionsPool(1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void process(Verb verb) {
        StatelessKieSession kieSession = kieSessionsPool.newStatelessKieSession();
        final List<Command> commands = new ArrayList<>(5);
        commands.add(CommandFactory.newSetGlobal("logger", logger));
        commands.add(CommandFactory.newSetGlobal("reports", reports));
        commands.add(CommandFactory.newInsert(verb));
        commands.add(CommandFactory.newInsert(store));
        commands.add(CommandFactory.newFireAllRules());
        kieSession.execute(CommandFactory.newBatchExecution(commands));
    }
}
