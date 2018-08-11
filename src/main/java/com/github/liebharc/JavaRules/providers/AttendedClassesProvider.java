package com.github.liebharc.JavaRules.providers;

import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import javafx.util.Pair;

import java.util.List;

public interface AttendedClassesProvider {
    List<Pair<Student, SchoolClass>> getAttendedClasses();
}
