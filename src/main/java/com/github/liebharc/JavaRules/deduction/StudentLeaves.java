package com.github.liebharc.JavaRules.deduction;

import com.github.liebharc.JavaRules.model.Student;

public class StudentLeaves implements Fact {
    private final Student student;
    private final boolean successfulCompletion;

    public StudentLeaves(Student student, boolean successfulCompletion) {

        this.student = student;
        this.successfulCompletion = successfulCompletion;
    }

    public Student getStudent() {
        return student;
    }

    public boolean isSuccessfulCompletion() {
        return successfulCompletion;
    }
}
