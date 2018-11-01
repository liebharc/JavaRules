package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;

public class JbpmDroolsEngineJavaOnlyUnitTest extends UnitTestBase {
    @Override
    protected Engine createEngine(ReportStore reports) {
        return new JbpmDroolsEngineJavaOnly(dataStore, reports);
    }
}
