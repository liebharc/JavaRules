package com.github.liebharc.JavaRules.sharedknowledge;

import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataAccessImpl implements DataAccess {

    private DataAccess store;
    public DataAccessImpl(DataAccess store) {
        this.store = store;
    }

    @Override
    public Collection<SchoolClass> getAssignedClasses(long studentId) {
        return store.getAssignedClasses(studentId);
    }

    @Override
    public Collection<Student> getActiveStudents(long classId) {
        return store.getActiveStudents(classId);
    }

    @Override
    public Collection<Student> getActiveStudents() {
        return store.getActiveStudents();
    }

    @Override
    public Collection<SchoolClass> getActiveClasses() {
        return store.getActiveClasses();
    }

    @Override
    public void assignStudent(long schoolClass, long student) {
        store.assignStudent(schoolClass, student);
    }

    @Override
    public void unassignStudent(long schoolClass, long student) {
        store.unassignStudent(schoolClass, student);
    }

    @Override
    public void markAsAttended(long schoolClass, long student) {
        store.markAsAttended(schoolClass, student);
    }

    @Override
    public void clearAttendees(long schoolClass) {
        store.clearAttendees(schoolClass);
    }

    @Override
    public Collection<Student> getAttendees(long schoolClass) {
        return store.getAttendees(schoolClass);
    }

    @Override
    public void markStudentAsActive(long schoolClass, long student) {
        store.markStudentAsActive(schoolClass, student);
    }

    @Override
    public void markStudentAsInactive(long schoolClass, long student) {
        store.markStudentAsInactive(schoolClass, student);
    }

    @Override
    public void addStudyTime(long student, int time) {
        store.addStudyTime(student, time);
    }

    @Override
    public int getStudyTime(long student) {
        return store.getStudyTime(student);
    }

    @Override
    public void incrementClassesMissed(long student) {
        store.incrementClassesMissed(student);
    }

    @Override
    public int getNumberOfMissedClasses(long student) {
        return store.getNumberOfMissedClasses(student);
    }

    @Override
    public boolean isAssigned(Long student, Long schoolClass) {
        return store.isAssigned(student, schoolClass);
    }

    @Override
    public boolean isActive(Long student, Long schoolClass) {
        return store.isActive(student, schoolClass);
    }

    @Override
    public long store(SchoolClass schoolClass) {
        return store.store(schoolClass);
    }

    @Override
    public long store(Student student) {
        return store.store(student);
    }

    public void writeThrough() {
        // Just as illustration
    }
}
