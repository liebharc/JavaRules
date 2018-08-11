package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.LazyDataStore;
import com.github.liebharc.JavaRules.verbs.Verb;

public class TimeAggregation implements  Rule{
    private LazyDataStore store;

    public TimeAggregation(LazyDataStore status) {
        this.store = status;
    }

    @Override
    public Result process(Verb verb) {
        return Result.NoAction;
    }
}
