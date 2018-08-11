package com.github.liebharc.JavaRules.verbs;

public class StudentClassVerb extends ClassVerb {
    private final long student;

    public StudentClassVerb(long student, long classId) {
        super(classId);
        this.student = student;
    }

    public long getStudent() {
        return student;
    }
}
