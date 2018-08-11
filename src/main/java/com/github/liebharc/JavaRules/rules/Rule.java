package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.verbs.Verb;

public interface Rule {
    void process(Verb verb);
}
