package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.verbs.*;

public class StudentStatus implements InterferenceStep {
    private final Logger logger = new Logger(this);

    @Override
    public void process(Verb verb, Facts facts) {
        final boolean sicknessStarts  = verb instanceof StudentBecomesSick;
        final boolean sicknessEnds  = verb instanceof StudentReturnsFromSickness;
        final boolean hasAttended  = verb instanceof StudentAttendsAClass;

        if (sicknessStarts) {
            setInactive((StudentBecomesSick) verb, facts.getStore());
        }

        if (sicknessEnds) {
            setActive((StudentReturnsFromSickness) verb, facts.getStore());
        }

        if (hasAttended) {
            markAsAttended((StudentAttendsAClass)verb, facts.getStore());
        }
    }

    private void setInactive(StudentBecomesSick verb, DataAccess store) {
        logger.log("Student " + verb.getStudent() + " became sick");
        for (SchoolClass schoolClass : store.getAssignedClasses(verb.getStudent())) {
            store.markStudentAsInactive(schoolClass.getId(), verb.getStudent());
        }
    }

    private void setActive(StudentReturnsFromSickness verb, DataAccess store) {
        logger.log("Student " + verb.getStudent() + " returned to classes");
        for (SchoolClass schoolClass : store.getAssignedClasses(verb.getStudent())) {
            store.markStudentAsActive(schoolClass.getId(), verb.getStudent());
        }
    }

    private void markAsAttended(StudentAttendsAClass verb, DataAccess store) {
        logger.log("Student " + verb.getStudent() + " has attended class");
        for (SchoolClass schoolClass : store.getAssignedClasses(verb.getStudent())) {
            store.markAsAttended(schoolClass.getId(), verb.getStudent());
        }
    }
}
