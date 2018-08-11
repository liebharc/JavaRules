package com.github.liebharc.JavaRules.model;

import java.util.Objects;

public class SchoolClass {
    private Long id;
    private String name;
    private int hoursADay;

    public SchoolClass(Long id, String name, int hoursADay) {
        this.id = id;

        this.name = name;
        this.hoursADay = hoursADay;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public int getHoursADay() {
        return hoursADay;
    }

    @Override
    public String toString() {
        return "SchoolClass{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolClass that = (SchoolClass) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
