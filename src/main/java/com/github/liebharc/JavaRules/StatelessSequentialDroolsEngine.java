package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.verbs.Verb;
import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.core.RuleBaseConfiguration;
import org.drools.core.impl.KnowledgeBaseImpl;
import org.kie.api.command.Command;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSessionsPool;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.command.CommandFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StatelessSequentialDroolsEngine extends StatelessDroolsEngine {


    public StatelessSequentialDroolsEngine(DataStore store, ReportStore reports) {
        super(store, reports);
    }

    @Override
    protected RuleBaseConfiguration createRuleBaseConfiguration() {
        return RuleBaseConfigurationProvider.createRuleBaseConfiguration(true);
    }
}
