package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;

public class StatefulReusingDroolsEngineWithRuleImprovementsPerfTest extends PerformanceTestBase {
    @Override
    protected Engine createEngine(ReportStore reports) {
        return new StatefulReusingDroolsEngineWithRuleImprovements(dataStore, reports);
    }
}
