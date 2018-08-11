package com.github.liebharc.JavaRules.deduction;

import com.github.liebharc.JavaRules.model.Student;

import java.util.Objects;

public class AggregatedTimeUpdate implements Fact {
    private final Student student;
    private final int attendedTime;

    public AggregatedTimeUpdate(Student student, int attendedTime) {

        this.student = student;
        this.attendedTime = attendedTime;
    }

    public Student getStudent() {
        return student;
    }

    public int getAttendedTime() {
        return attendedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregatedTimeUpdate that = (AggregatedTimeUpdate) o;
        return Objects.equals(student, that.student);
    }

    @Override
    public int hashCode() {

        return Objects.hash(student);
    }
}

