package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.verbs.ASchoolDayHasPassed;
import com.github.liebharc.JavaRules.verbs.Verb;

public class InitRule implements InterferenceStep {
    private final Logger logger = new Logger(this);
    private DataStore store;

    public InitRule(DataStore status) {
        this.store = status;
    }

    @Override
    public void process(Verb verb, Facts facts) {
        if (verb instanceof ASchoolDayHasPassed) {
            logger.log("Clearing store");
            for (SchoolClass schoolClass : store.getActiveClasses()) {
                store.clearAttendees(schoolClass.getId());
            }
        }
    }
}
