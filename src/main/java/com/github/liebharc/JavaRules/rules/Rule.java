package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.verbs.Verb;

public interface Rule {
    enum Result {
        NoAction,
        Done
    }

    Result process(Verb verb);
}
