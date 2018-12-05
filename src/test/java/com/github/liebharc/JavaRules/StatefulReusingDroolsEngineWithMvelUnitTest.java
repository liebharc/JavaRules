package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;

public class StatefulReusingDroolsEngineWithMvelUnitTest extends UnitTestBase {
    @Override
    protected Engine createEngine(ReportStore reports) {
        return new StatefulReusingDroolsEngineWithMvel(dataStore, reports);
    }
}
