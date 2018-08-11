package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.DataStore;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.deduction.AggregatedTimeUpdate;
import com.github.liebharc.JavaRules.deduction.Fact;
import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.deduction.StudentAttendedClass;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.verbs.ASchoolDayHasPassed;
import com.github.liebharc.JavaRules.verbs.Verb;
import javafx.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TimeAggregation implements InterferenceStep {
    private final Logger logger = new Logger(this);
    private DataStore store;

    public TimeAggregation(DataStore status) {
        this.store = status;
    }

    @Override
    public void process(Verb verb, Facts facts) {
        final boolean hasTimePassed = verb instanceof ASchoolDayHasPassed;
        if (!hasTimePassed) {
            return;
        }

        for (StudentAttendedClass studentSchoolClassPair : facts.getFacts(StudentAttendedClass.class)) {
            Student student = studentSchoolClassPair.getStudent();
            SchoolClass schoolClass = studentSchoolClassPair.getSchoolClass();
            logger.log(student + " attended class "  + schoolClass + " and got accounted "+ schoolClass.getHoursADay());
            store.addStudyTime(student.getId(), schoolClass.getHoursADay());
            facts.add(new AggregatedTimeUpdate(student, store.getStudyTime(student.getId())));
        }
    }
}
