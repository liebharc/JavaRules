package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
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

    private DataStore store;

    public MissedClassesAggregation(DataStore status) {
        this.store = status;
    }

    @Override
    public void process(Verb verb, Facts facts) {
        final boolean hasTimePassed = verb instanceof ASchoolDayHasPassed;
        if (!hasTimePassed) {
            return;
        }

        for (SchoolClass schoolClass : store.getActiveClasses()) {
            List<Student> activeStudents = store.getActiveStudents(schoolClass.getId());
            List<Student> attendees = store.getAttendees(schoolClass.getId());
            for (Student attendee : new HashSet<>(attendees)) {
                logger.log(attendee + " has attended class " + schoolClass);
                facts.add(new StudentAttendedClass(attendee, schoolClass));
            }

            List<Student> misses = new ArrayList<>(activeStudents);
            misses.removeAll(attendees);
            for (Student miss : misses) {
                logger.log(miss + " has missed class " + schoolClass);
                store.incrementClassesMissed(miss.getId());
                facts.add(new StudentMissedClass(miss, store.getNumberOfMissedClasses(miss.getId())));
            }
        }
    }
}
