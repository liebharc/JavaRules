package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.rules.*;
import com.github.liebharc.JavaRules.verbs.Verb;

public class Engine {

    private DataStore store;

    private InterferenceStep[] rules;

    public Engine(DataStore store, ReportStore reports) {

        this.store = store;

        rules = new InterferenceStep[] {
                new SignUpSignOff(this.store),
                new StudentStatus(this.store),
                new MissedClassesAggregation(this.store),
                new TimeAggregation(this.store),
                new ClassCompletion(this.store),
                new InitRule(this.store),
                new ReportWriter(reports)
        };
    }

    public void process(Verb verb) {
        final Facts facts = new Facts();
        for (InterferenceStep rule : rules) {
            rule.process(verb, facts);
        }
    }
}

