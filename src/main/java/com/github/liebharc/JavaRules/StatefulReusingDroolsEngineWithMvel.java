package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;

public class StatefulReusingDroolsEngineWithMvel extends StatefulReusingDroolsEngine {

    public StatefulReusingDroolsEngineWithMvel(DataStore store, ReportStore reports) {
        super(store, reports);
    }

    @Override
    protected String getDroolsFile() {
        return "rules_mvel.drl";
    }
}
