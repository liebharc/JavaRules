package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.DataStore;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.verbs.ASchoolDayHasPassed;
import com.github.liebharc.JavaRules.verbs.Verb;

import java.util.ArrayList;
import java.util.List;

public class MissedClassesAggregation implements Rule {
    private DataStore store;

    public MissedClassesAggregation(DataStore status) {
        this.store = status;
    }

    @Override
    public void process(Verb verb) {
        final boolean hasTimePassed = verb instanceof ASchoolDayHasPassed;
        if (!hasTimePassed) {
            return;
        }

        for (SchoolClass schoolClass : store.getActiveClasses()) {
            List<Student> activeStudents = store.getActiveStudents(schoolClass.getId());
            List<Student> attendees = store.getAttendees(schoolClass.getId());
            List<Student> misses = new ArrayList<>(activeStudents);
            misses.removeAll(attendees);
            for (Student miss : misses) {
                store.incrementClassesMissed(miss.getId());
            }

            store.clearAttendes(schoolClass.getId());
        }
    }
}
