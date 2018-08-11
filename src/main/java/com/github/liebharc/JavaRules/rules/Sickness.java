package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.LazyDataStore;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.verbs.*;

public class Sickness implements Rule {
    private LazyDataStore store;

    public Sickness(LazyDataStore status) {
        this.store = status;
    }

    @Override
    public Result process(Verb verb) {
        final boolean sicknessStarts  = verb instanceof StudentBecomesSick;
        final boolean sicknessEnds  = verb instanceof StudentReturnsFromSickness;
        final boolean isChange = sicknessStarts || sicknessEnds;
        if (!isChange) {
            return Result.NoAction;
        }

        if (sicknessStarts) {
            setInactive((StudentBecomesSick) verb);
        }

        if (sicknessEnds) {
            setActive((StudentReturnsFromSickness) verb);
        }

        return Result.Done;
    }

    private void setInactive(StudentBecomesSick verb) {
        for (SchoolClass schoolClass : store.getAssignedClasses(verb.getStudent()).get()) {
            store.markStudentAsInactive(schoolClass.getId(), verb.getStudent());
        }
    }

    private void setActive(StudentReturnsFromSickness verb) {
        for (SchoolClass schoolClass : store.getAssignedClasses(verb.getStudent()).get()) {
            store.markStudentAsActive(schoolClass.getId(), verb.getStudent());
        }
    }
}
