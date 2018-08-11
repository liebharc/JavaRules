package com.github.liebharc.JavaRules.providers;

import com.github.liebharc.JavaRules.model.Student;

import java.util.HashMap;

public interface MissedClassesProvider {
    HashMap<Student, Integer> getMisses();
}
