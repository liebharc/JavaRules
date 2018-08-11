package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.deduction.Fact;
import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.verbs.Verb;

import java.util.Collection;

public interface InterferenceStep {
    void process(Verb verb, Facts facts);
}
