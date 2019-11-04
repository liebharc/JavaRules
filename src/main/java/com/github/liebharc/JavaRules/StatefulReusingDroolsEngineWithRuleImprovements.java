package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.verbs.Verb;
import org.drools.compiler.builder.impl.KnowledgeBuilderImpl;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;
import java.net.URL;

public class StatefulReusingDroolsEngineWithRuleImprovements extends StatefulReusingDroolsEngine {

    public StatefulReusingDroolsEngineWithRuleImprovements(DataAccess store, ReportStore reports) {
        super(store, reports);
    }

    @Override
    protected String getDroolsFile() {
        return "rules_improved.drl";
    }
}
