package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.Engine;
import com.github.liebharc.JavaRules.JbpmDroolsEngine;
import com.github.liebharc.JavaRules.JbpmDroolsEngineJavaOnly;
import com.github.liebharc.JavaRules.PerformanceTestBase;
import com.github.liebharc.JavaRules.model.ReportStore;

public class JbpmDroolsEngineJavaOnlyPerfTest extends PerformanceTestBase {
    @Override
    protected Engine createEngine(ReportStore reports) {
        return new JbpmDroolsEngineJavaOnly(dataStore, reports);
    }
}
