package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.deduction.AggregatedTimeUpdate;
import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.deduction.StudentAttendedClass;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.verbs.ASchoolDayHasPassed;
import com.github.liebharc.JavaRules.verbs.Verb;

public class TimeAggregation implements InterferenceStep {
    private final Logger logger = new Logger(this);

    @Override
    public void process(Verb verb, Facts facts) {
        final boolean hasTimePassed = verb instanceof ASchoolDayHasPassed;
        if (!hasTimePassed) {
            return;
        }

        DataAccess store = facts.getStore();
        for (StudentAttendedClass studentSchoolClassPair : facts.getFacts(StudentAttendedClass.class)) {
            Student student = studentSchoolClassPair.getStudent();
            SchoolClass schoolClass = studentSchoolClassPair.getSchoolClass();
            logger.log(student + " attended class "  + schoolClass + " and got accounted "+ schoolClass.getHoursADay());
            store.addStudyTime(student.getId(), schoolClass.getHoursADay());
            facts.add(new AggregatedTimeUpdate(student, store.getStudyTime(student.getId())));
        }
    }
}
