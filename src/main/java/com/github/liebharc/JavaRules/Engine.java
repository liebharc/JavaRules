package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.rules.Rule;
import com.github.liebharc.JavaRules.rules.SignUpSignOff;
import com.github.liebharc.JavaRules.verbs.Verb;
import com.github.liebharc.JavaRules.verbs.TimeHasPassed;
import com.github.liebharc.JavaRules.verbs.StudentBecomesSick;

import java.util.List;

public class Engine {

    private LazyDataStore store;

    public Engine(DataStore store) {

        this.store = new LazyDataStore(store);

        rules = new Rule[] {
                new SignUpSignOff(this.store)
        };
    }

    private Rule[] rules;

    public void process(Verb verb) {
        for (Rule rule : rules) {
            rule.process(verb);
        }
    }
}
