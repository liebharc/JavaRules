package com.github.liebharc.JavaRules.verbs;

public class ClassVerb implements Verb {
    private long classId;

    public ClassVerb(long classId) {

        this.classId = classId;
    }

    public long getClassId() {
        return classId;
    }

}
