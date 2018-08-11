package com.github.liebharc.JavaRules.verbs;

public class TimeHasPassed implements Verb {
    private int days;

    public TimeHasPassed(int days) {

        this.days = days;
    }

    public int getDays() {
        return days;
    }
}
