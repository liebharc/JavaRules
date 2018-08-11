package com.github.liebharc.JavaRules.verbs;

public class StudentVerb implements Verb {
    private long student;

    public StudentVerb(long student) {

        this.student = student;
    }

    public long getStudent() {
        return student;
    }
}
