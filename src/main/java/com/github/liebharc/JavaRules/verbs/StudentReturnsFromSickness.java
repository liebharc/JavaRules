package com.github.liebharc.JavaRules.verbs;

public class StudentReturnsFromSickness implements Verb {
    private long student;

    public StudentReturnsFromSickness(long student) {

        this.student = student;
    }

    public long getStudent() {
        return student;
    }
}
