package com.github.liebharc.JavaRules.model;

public class ModelFactory {
    private long idGenerator = 0;

    public SchoolClass newClass(String name) {
        return new SchoolClass(idGenerator++, name);
    }

    public Student newStudent(String firstName, String lastName) {
        return new Student(idGenerator++, firstName, lastName);
    }
}
