package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;

public class StatelessSequentialDroolsEnginePerfTest extends PerformanceTestBase {
    @Override
    protected Engine createEngine(ReportStore reports) {
        return new StatelessSequentialDroolsEngine(dataStore, reports);
    }
}
