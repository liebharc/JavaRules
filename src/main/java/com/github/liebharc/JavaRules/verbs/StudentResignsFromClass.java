package com.github.liebharc.JavaRules.verbs;

public class StudentResignsFromClass extends ClassVerb {
    private final long student;

    public StudentResignsFromClass(long student, long classId) {
        super(classId);
        this.student= student;
    }

    public long getStudent() {
        return student;
    }
}
