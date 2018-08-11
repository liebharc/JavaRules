package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.DataStore;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.verbs.ASchoolDayHasPassed;
import com.github.liebharc.JavaRules.verbs.Verb;

import java.util.List;

public class InitRule implements Rule {
    private DataStore store;

    public InitRule(DataStore status) {
        this.store = status;
    }

    @Override
    public void process(Verb verb) {
        if (verb instanceof ASchoolDayHasPassed) {
            for (SchoolClass schoolClass : store.getActiveClasses()) {
                store.clearAttendes(schoolClass.getId());
            }
        }
    }
}
