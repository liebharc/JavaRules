package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;

import java.util.List;

public class LazyDataStore {
    private DataStore dataStore;

    public LazyDataStore(DataStore dataStore) {

        this.dataStore = dataStore;
    }

    public Lazy<List<SchoolClass>> getAssignedClasses(long studentId) {
        return new Lazy<>(() -> dataStore.getAssignedClasses(studentId));
    }

    public  Lazy<List<Student>> getActiveStudents(long classId) {
        return new Lazy(() -> dataStore.getActiveStudents(classId));
    }

    public  Lazy<List<SchoolClass>> getActiveClasses() {
        return  new Lazy(() -> dataStore.getActiveClasses());
    }

    public void assignStudent(long schoolClass, long student) {
        dataStore.assignStudent(schoolClass, student);
    }

    public void unassignStudent(long schoolClass, long student) {
        dataStore.unassignStudent(schoolClass, student);
    }

    public void markStudentAsActive(long schoolClass, long student) {
        dataStore.markStudentAsActive(schoolClass, student);
    }

    public void markStudentAsInactive(long schoolClass, long student) {
        dataStore.markStudentAsInactive(schoolClass, student);
    }

    public boolean isAssigned(Long student, Long schoolClass) {
        return dataStore.isAssigned(student, schoolClass);
    }

    public boolean isActive(Long student, Long schoolClass) {
        return dataStore.isActive(student, schoolClass);
    }

    public long store(SchoolClass schoolClass) {
        return dataStore.store(schoolClass);
    }

    public long store(Student student) {
        return dataStore.store(student);
    }

    public void addStudyTime(long student, long time) {
        dataStore.addStudyTime(student, time);
    }

    public long getStudyTime(long student) {
        return dataStore.getStudyTime(student);
    }
}
