package com.github.liebharc.JavaRules.model;

public class SchoolClass {
    private Long id;
    private String name;

    public SchoolClass(Long id, String name) {
        this.id = id;

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "SchoolClass{" +
                "name='" + name + '\'' +
                '}';
    }
}
