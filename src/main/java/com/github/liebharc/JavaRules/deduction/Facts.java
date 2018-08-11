package com.github.liebharc.JavaRules.deduction;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Facts {

    private final Set<Fact> facts = new HashSet<>();

    public void add(Fact fact) {
        // Remove duplicates with same hash code
        if (facts.contains(fact)) {
            facts.remove(fact);
        }

        facts.add(fact);
    }

    public<T extends Fact> Collection<T> getFacts(Class<T> factClass) {
        return facts.stream().filter(f -> factClass.isInstance(f)).map(f -> (T)f).collect(Collectors.toList());
    }
}
