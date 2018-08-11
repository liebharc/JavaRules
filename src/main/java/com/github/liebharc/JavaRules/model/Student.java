package com.github.liebharc.JavaRules.model;

import java.util.Objects;

public class Student {
    private Long id;
    private final String firstName;
    private final String lastName;
    
    public Student(Long id, String firstName, String lastName) {
        this.id = id;

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
