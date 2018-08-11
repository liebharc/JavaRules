package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.DataStore;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.verbs.ASchoolDayHasPassed;
import com.github.liebharc.JavaRules.verbs.Verb;

public class TimeAggregation implements  Rule{
    private DataStore store;

    public TimeAggregation(DataStore status) {
        this.store = status;
    }

    @Override
    public void process(Verb verb) {
        final boolean hasTimePassed = verb instanceof ASchoolDayHasPassed;
        if (!hasTimePassed) {
            return;
        }

        for (SchoolClass schoolClass : store.getActiveClasses()) {
            for (Student student : store.getActiveStudents(schoolClass.getId())) {
                store.addStudyTime(student.getId(), schoolClass.getHoursADay());
            }
        }
    }
}
