package com.github.liebharc.JavaRules.deduction;

import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Facts {

    private final Set<Fact> facts = new HashSet<>();
    private DataAccess store;

    public Facts(DataAccess store) {

        this.store = store;
    }

    public void add(Fact fact) {
        // Remove duplicates with same hash code
        if (facts.contains(fact)) {
            facts.remove(fact);
        }

        facts.add(fact);
    }

    public DataAccess getStore() {
        return store;
    }

    public<T extends Fact> Collection<T> getFacts(Class<T> factClass) {
        return facts.stream().filter(f -> factClass.isInstance(f)).map(f -> (T)f).collect(Collectors.toList());
    }
}
