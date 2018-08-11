package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.DataStore;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.providers.AggregatedTimeProvider;
import com.github.liebharc.JavaRules.providers.MissedClassesProvider;
import com.github.liebharc.JavaRules.verbs.Verb;

import java.util.HashMap;
import java.util.Map;

public class ClassCompletion implements Rule {
    private final Logger logger = new Logger(this);
    private DataStore store;
    private AggregatedTimeProvider timeProvider;
    private MissedClassesProvider missedClassesProvider;

    public ClassCompletion(DataStore status, AggregatedTimeProvider timeProvider, MissedClassesProvider missedClassesProvider) {
        this.store = status;
        this.timeProvider = timeProvider;
        this.missedClassesProvider = missedClassesProvider;
    }

    @Override
    public void process(Verb verb) {
        Map<Student, Integer> timeUpdates = timeProvider.getTimeUpdates();
        for (Student student : timeUpdates.keySet()) {
            if (timeUpdates.get(student) > 10) {
                logger.log("Student " + student + " completed his studies");
                unassignFromAllClasses(student);
            }
        }

        HashMap<Student, Integer> misses = missedClassesProvider.getMisses();

        for (Student student : misses.keySet()) {
            if (misses.get(student) >= 5) {
                logger.log("Student " + student + " missed too many classes");
                unassignFromAllClasses(student);

            }
        }
    }

    private void unassignFromAllClasses(Student student) {
        for (SchoolClass schoolClass : store.getAssignedClasses(student.getId())) {
            store.markStudentAsInactive(schoolClass.getId(), student.getId());
            store.unassignStudent(schoolClass.getId(), student.getId());
        }
    }
}
