package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.DataStore;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.providers.AggregatedTimeProvider;
import com.github.liebharc.JavaRules.providers.AttendedClassesProvider;
import com.github.liebharc.JavaRules.verbs.ASchoolDayHasPassed;
import com.github.liebharc.JavaRules.verbs.Verb;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class TimeAggregation implements Rule, AggregatedTimeProvider {
    private final Logger logger = new Logger(this);
    private DataStore store;
    private AttendedClassesProvider attendedClassesProvidera;
    private Map<Student, Integer> timeUpdates = new HashMap<>();

    public TimeAggregation(DataStore status, AttendedClassesProvider attendedClassesProvidera) {
        this.store = status;
        this.attendedClassesProvidera = attendedClassesProvidera;
    }

    @Override
    public void process(Verb verb) {
        final boolean hasTimePassed = verb instanceof ASchoolDayHasPassed;
        if (!hasTimePassed) {
            return;
        }

        Map<Student, Integer> timeUpdates = new HashMap<>();
        for (Pair<Student, SchoolClass> studentSchoolClassPair : attendedClassesProvidera.getAttendedClasses()) {
            Student student = studentSchoolClassPair.getKey();
            SchoolClass schoolClass = studentSchoolClassPair.getValue();
            logger.log(student + " attended class "  + schoolClass + " and got accounted "+ schoolClass.getHoursADay());
            store.addStudyTime(student.getId(), schoolClass.getHoursADay());
            timeUpdates.put(student, store.getStudyTime(student.getId()));
        }
        this.timeUpdates = timeUpdates;
    }

    @Override
    public Map<Student, Integer> getTimeUpdates() {
        return timeUpdates;
    }
}
