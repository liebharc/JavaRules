package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.LazyDataStore;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.verbs.TimeHasPassed;
import com.github.liebharc.JavaRules.verbs.Verb;

public class TimeAggregation implements  Rule{
    private LazyDataStore store;

    public TimeAggregation(LazyDataStore status) {
        this.store = status;
    }

    @Override
    public Result process(Verb verb) {
        final boolean hasTimePassed = verb instanceof TimeHasPassed;
        if (!hasTimePassed) {
            return Result.NoAction;
        }

        final TimeHasPassed time = (TimeHasPassed)verb;

        for (SchoolClass schoolClass : store.getActiveClasses().get()) {
            for (Student student : store.getActiveStudents(schoolClass.getId()).get()) {
                store.addStudyTime(student.getId(), schoolClass.getHoursADay() * time.getDays());
            }
        }

        return Result.NoAction;
    }
}
