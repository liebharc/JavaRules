package com.github.liebharc.JavaRules.verbs;


public class StudentJoinsAClass extends Verb {
    private final long student;

    public StudentJoinsAClass(long student, long classId) {
        super(classId);
        this.student = student;
    }

    public long getStudent() {
        return student;
    }
}
