package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.deduction.StudentAttendedClass;
import com.github.liebharc.JavaRules.deduction.StudentMissedClass;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.verbs.ASchoolDayHasPassed;
import com.github.liebharc.JavaRules.verbs.Verb;

import java.util.*;

public class MissedClassesAggregation implements InterferenceStep {
    private final Logger logger = new Logger(this);

    @Override
    public void process(Verb verb, Facts facts) {
        final boolean hasTimePassed = verb instanceof ASchoolDayHasPassed;
        if (!hasTimePassed) {
            return;
        }

        DataAccess store = facts.getStore();
        for (SchoolClass schoolClass : store.getActiveClasses()) {
            Collection<Student> activeStudents = store.getActiveStudents(schoolClass.getId());
            Collection<Student> attendees = store.getAttendees(schoolClass.getId());
            for (Student attendee : new HashSet<>(attendees)) {
                logger.log(attendee + " has attended class " + schoolClass);
                facts.add(new StudentAttendedClass(attendee, schoolClass));
            }

            List<Student> misses = new ArrayList<>(activeStudents);
            misses.removeAll(attendees);
            for (Student miss : misses) {
                logger.log(miss + " has missed class " + schoolClass);
                store.incrementClassesMissed(miss.getId());
                facts.add(new StudentMissedClass(miss, schoolClass, store.getNumberOfMissedClasses(miss.getId())));
            }
        }
    }
}
