package com.github.liebharc.JavaRules.model;

import java.util.HashMap;
import java.util.Map;

public class ReportStore {
    private Map<Long, StringBuilder> reports = new HashMap<>();

    public void writeReport(long studentId, String text) {
        boolean firstCall = false;
        if (!reports.containsKey(studentId)) {
            firstCall = true;
            reports.put(studentId, new StringBuilder());
        }

        StringBuilder builder = reports.get(studentId);
        if (!firstCall) {
            builder.append("\n");
        }
        builder.append(text);
    }

    public String getReport(long studentId) {
        final StringBuilder report = reports.get(studentId);
        if (report == null) {
            return "";
        }

        return report.toString();
    }
}
