package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;

public class StatefulReusingDroolsEngineWithMvel extends StatefulReusingDroolsEngine {

    public StatefulReusingDroolsEngineWithMvel(DataAccess store, ReportStore reports) {
        super(store, reports);
    }

    @Override
    protected String getDroolsFile() {
        return "rules_mvel.drl";
    }
}
