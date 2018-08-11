package com.github.liebharc.JavaRules.deduction;

import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;

import java.util.Objects;

public class StudentAttendedClass implements Fact {
    private final Student student;
    private final SchoolClass schoolClass;

    public StudentAttendedClass(Student student, SchoolClass schoolClass) {
        this.student = student;
        this.schoolClass = schoolClass;
    }

    public Student getStudent() {
        return student;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentAttendedClass that = (StudentAttendedClass) o;
        return Objects.equals(student, that.student) &&
                Objects.equals(schoolClass, that.schoolClass);
    }

    @Override
    public int hashCode() {

        return Objects.hash(student, schoolClass);
    }
}
