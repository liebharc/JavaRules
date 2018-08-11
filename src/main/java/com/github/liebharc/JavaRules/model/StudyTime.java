package com.github.liebharc.JavaRules.model;

public class StudyTime {
    private final long student;
    private final long time;

    public StudyTime(long student, long time) {

        this.student = student;
        this.time = time;
    }

    public long getStudent() {
        return student;
    }

    public long getTime() {
        return time;
    }
}
