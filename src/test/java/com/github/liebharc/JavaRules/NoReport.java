package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;

public class NoReport implements ReportStore {
    @Override
    public void writeReport(long studentId, String text) {

    }
}
