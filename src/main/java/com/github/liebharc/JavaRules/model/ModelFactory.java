package com.github.liebharc.JavaRules.model;

public class ModelFactory {
    private long idGenerator = 0;

    public SchoolClass newClass(String name, int hoursADay) {
        return new SchoolClass(idGenerator++, name, hoursADay);
    }

    public Student newStudent(String firstName, String lastName) {
        return new Student(idGenerator++, firstName, lastName);
    }
}
