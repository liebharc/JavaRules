package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.verbs.Verb;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

import java.io.IOException;

public class JbpmDroolsEngineJavaOnly implements Engine {

    private final DataAccess store;
    private final ReportStore reports;

    private Logger logger = new Logger(StatefulReusingDroolsEngine.class);

    public JbpmDroolsEngineJavaOnly(DataAccess store, ReportStore reports) {
        this.store = store;
        this.reports = reports;
    }

    @Override
    public void process(Verb verb) {
        Rules.INSTANCE.process(verb, store, logger, reports);
    }
}
