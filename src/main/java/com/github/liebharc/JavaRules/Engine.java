package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.rules.*;
import com.github.liebharc.JavaRules.verbs.Verb;

public class Engine {

    private DataStore store;

    public Engine(DataStore store, ReportStore reports) {

        this.store = store;

        MissedClassesAggregation missedClassesAggregation = new MissedClassesAggregation(this.store);
        TimeAggregation timeAggregation = new TimeAggregation(this.store, missedClassesAggregation);
        rules = new Rule[] {
                new SignUpSignOff(this.store),
                new StudentStatus(this.store),
                missedClassesAggregation,
                timeAggregation,
                new ClassCompletion(this.store, timeAggregation, missedClassesAggregation),
                new InitRule(this.store),
                new ReportWriter(reports)
        };
    }

    private Rule[] rules;

    public void process(Verb verb) {
        for (Rule rule : rules) {
            rule.process(verb);
        }
    }
}

