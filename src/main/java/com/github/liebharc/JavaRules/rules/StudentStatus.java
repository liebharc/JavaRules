package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.DataStore;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.verbs.*;

public class StudentStatus implements Rule {
    private DataStore store;

    public StudentStatus(DataStore status) {
        this.store = status;
    }

    @Override
    public void process(Verb verb) {
        final boolean sicknessStarts  = verb instanceof StudentBecomesSick;
        final boolean sicknessEnds  = verb instanceof StudentReturnsFromSickness;
        final boolean hasAttended  = verb instanceof StudentAttendsAClass;

        if (sicknessStarts) {
            setInactive((StudentBecomesSick) verb);
        }

        if (sicknessEnds) {
            setActive((StudentReturnsFromSickness) verb);
        }

        if (hasAttended) {
            markAsAttended((StudentAttendsAClass)verb);
        }
    }

    private void setInactive(StudentBecomesSick verb) {
        for (SchoolClass schoolClass : store.getAssignedClasses(verb.getStudent())) {
            store.markStudentAsInactive(schoolClass.getId(), verb.getStudent());
        }
    }

    private void setActive(StudentReturnsFromSickness verb) {
        for (SchoolClass schoolClass : store.getAssignedClasses(verb.getStudent())) {
            store.markStudentAsActive(schoolClass.getId(), verb.getStudent());
        }
    }

    private void markAsAttended(StudentAttendsAClass verb) {
        for (SchoolClass schoolClass : store.getAssignedClasses(verb.getStudent())) {
            store.markAsAttended(schoolClass.getId(), verb.getStudent());
        }
    }
}
