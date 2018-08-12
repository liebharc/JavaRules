package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.deduction.*;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.verbs.Verb;

import java.util.Collection;

public class ClassCompletion implements InterferenceStep {
    private final Logger logger = new Logger(this);

    @Override
    public void process(Verb verb, Facts facts) {
        Collection<AggregatedTimeUpdate> timeUpdates = facts.getFacts(AggregatedTimeUpdate.class);
        for (AggregatedTimeUpdate update : timeUpdates) {
            if (update.getAttendedTime() > 10) {
                logger.log("Student " + update.getStudent() + " completed his studies");
                unassignFromAllClasses(update.getStudent(), facts.getStore());
            }
        }

        Collection<StudentMissedClass> misses = facts.getFacts(StudentMissedClass.class);

        for (StudentMissedClass miss : misses) {
            if (miss.getMisses() >= 5) {
                logger.log("Student " + miss.getStudent() + " missed too many classes");
                unassignFromAllClasses(miss.getStudent(), facts.getStore());

            }
        }
    }

    private void unassignFromAllClasses(Student student, DataAccess store) {
        for (SchoolClass schoolClass : store.getAssignedClasses(student.getId())) {
            store.markStudentAsInactive(schoolClass.getId(), student.getId());
            store.unassignStudent(schoolClass.getId(), student.getId());
        }
    }
}
