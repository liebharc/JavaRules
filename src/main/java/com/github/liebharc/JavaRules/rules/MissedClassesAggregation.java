package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.DataStore;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.providers.AttendedClassesProvider;
import com.github.liebharc.JavaRules.providers.MissedClassesProvider;
import com.github.liebharc.JavaRules.verbs.ASchoolDayHasPassed;
import com.github.liebharc.JavaRules.verbs.Verb;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MissedClassesAggregation implements Rule, AttendedClassesProvider, MissedClassesProvider {
    private final Logger logger = new Logger(this);

    private DataStore store;

    private List<Pair<Student, SchoolClass>> attendedClasses = new ArrayList<>();

    private HashMap<Student, Integer> misses = new HashMap<>();

    public MissedClassesAggregation(DataStore status) {
        this.store = status;
    }

    @Override
    public void process(Verb verb) {
        final boolean hasTimePassed = verb instanceof ASchoolDayHasPassed;
        if (!hasTimePassed) {
            return;
        }

        List<Pair<Student, SchoolClass>> attendedClasses = new ArrayList<>();
        HashMap<Student, Integer> allMisses = new HashMap<>();
        for (SchoolClass schoolClass : store.getActiveClasses()) {
            List<Student> activeStudents = store.getActiveStudents(schoolClass.getId());
            List<Student> attendees = store.getAttendees(schoolClass.getId());
            for (Student attendee : new HashSet<>(attendees)) {
                logger.log(attendee + " has attended class " + schoolClass);
                attendedClasses.add(new Pair<>(attendee, schoolClass));
            }

            List<Student> misses = new ArrayList<>(activeStudents);
            misses.removeAll(attendees);
            for (Student miss : misses) {
                logger.log(miss + " has missed class " + schoolClass);
                store.incrementClassesMissed(miss.getId());
                allMisses.put(miss, store.getNumberOfMissedClasses(miss.getId()));
            }
        }

        this.attendedClasses = attendedClasses;
        this.misses = allMisses;
    }

    @Override
    public List<Pair<Student, SchoolClass>> getAttendedClasses() {
        return attendedClasses;
    }

    @Override
    public HashMap<Student, Integer> getMisses() {
        return misses;
    }
}
