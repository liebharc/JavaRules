package com.github.liebharc.JavaRules.verbs;

public class StudentBecomesSick implements Verb {
    private long student;

    public StudentBecomesSick(long student) {

        this.student = student;
    }

    public long getStudent() {
        return student;
    }
}
