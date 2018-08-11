package com.github.liebharc.JavaRules.providers;

import com.github.liebharc.JavaRules.model.Student;

import java.util.Map;

public interface AggregatedTimeProvider {
    Map<Student, Integer> getTimeUpdates();
}
