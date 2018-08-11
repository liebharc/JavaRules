package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.DataStore;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.providers.AggregatedTimeProvider;
import com.github.liebharc.JavaRules.verbs.Verb;

import java.util.Map;

public class ClassCompletion implements Rule {
    private final Logger logger = new Logger(this);
    private DataStore store;
    private AggregatedTimeProvider timeProvider;

    public ClassCompletion(DataStore status, AggregatedTimeProvider timeProvider) {
        this.store = status;
        this.timeProvider = timeProvider;
    }

    @Override
    public void process(Verb verb) {
        Map<Student, Integer> timeUpdates = timeProvider.getTimeUpdates();
        for (Student student : timeUpdates.keySet()) {
            if (timeUpdates.get(student) > 10) {
                logger.log("Student " + student + " completed his studies");
                for (SchoolClass schoolClass : store.getAssignedClasses(student.getId())) {
                    store.markStudentAsInactive(schoolClass.getId(), student.getId());
                    store.unassignStudent(schoolClass.getId(), student.getId());
                }
            }
        }
    }
}
