package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;

public class StatefulReusingDroolsEngineWithRuleImprovementsUnitTest extends UnitTestBase {
    @Override
    protected Engine createEngine(ReportStore reports) {
        return new StatefulReusingDroolsEngineWithRuleImprovements(dataStore, reports);
    }
}
