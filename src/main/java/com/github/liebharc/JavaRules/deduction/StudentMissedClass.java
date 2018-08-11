package com.github.liebharc.JavaRules.deduction;

import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;

import java.util.Objects;

public class StudentMissedClass implements Fact {
    private final Student student;
    private int misses;

    public StudentMissedClass(Student student, int misses) {
        this.student = student;
        this.misses = misses;
    }

    public Student getStudent() {
        return student;
    }

    public int getMisses() {
        return misses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentMissedClass that = (StudentMissedClass) o;
        return Objects.equals(student, that.student);
    }

    @Override
    public int hashCode() {

        return Objects.hash(student);
    }
}