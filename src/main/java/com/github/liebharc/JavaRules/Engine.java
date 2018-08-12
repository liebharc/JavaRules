package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.rules.*;
import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.sharedknowledge.DataAccessImpl;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.verbs.Verb;

public class Engine {

    private DataStore store;

    private InterferenceStep[] rules;

    public Engine(DataStore store, ReportStore reports) {

        this.store = store;

        rules = new InterferenceStep[] {
                new SignUpSignOff(),
                new StudentStatus(),
                new MissedClassesAggregation(),
                new TimeAggregation(),
                new ClassCompletion(),
                new InitRule(),
                new ReportWriter(reports)
        };
    }

    public void process(Verb verb) {
        final DataAccessImpl dataAccess = new DataAccessImpl(store);
        final Facts facts = new Facts(dataAccess);
        for (InterferenceStep rule : rules) {
            rule.process(verb, facts);
        }
        dataAccess.writeThrough();
    }
}

