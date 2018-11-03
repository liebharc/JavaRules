package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;

public class StatelessSequentialDroolsEngineUnitTest extends UnitTestBase {
    @Override
    protected Engine createEngine(ReportStore reports) {
        return new StatelessSequentialDroolsEngine(dataStore, reports);
    }
}
