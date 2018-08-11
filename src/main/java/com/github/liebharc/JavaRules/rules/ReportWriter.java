package com.github.liebharc.JavaRules.rules;


import com.github.liebharc.JavaRules.deduction.Fact;
import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.verbs.Verb;

import java.util.Collection;

public class ReportWriter implements InterferenceStep {
    public ReportWriter(ReportStore store) {

    }

    @Override
    public void process(Verb verb, Facts facts) {
        // For all students we need to find which classes a student attended or missed
        // We need to find if a student passed or got expelled
        // => Do we need more events?

    }
}
