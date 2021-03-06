package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.sharedknowledge.SnapshotDataStore;

public class SnapshotDataStoreUnitTest extends UnitTestBase {
    @Override
    protected Engine createEngine(ReportStore reports) {
        return new StatefulReusingDroolsEngineWithRuleImprovements(dataStore, reports);
    }

    @Override
    protected DataAccess createDataStore() {
        return new SnapshotDataStore();
    }
}
