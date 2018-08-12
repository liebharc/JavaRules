package com.github.liebharc.JavaRules.sharedknowledge;

import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DataAccessImpl implements DataAccess {

    private DataStore store;

    private List<SchoolClass> activeClasses;

    private List<Student> activeStudents;

    public DataAccessImpl(DataStore store) {
        this.store = store;
    }

    @Override
    public List<SchoolClass> getAssignedClasses(long studentId) {
        return store.getAssignedClasses(studentId);
    }

    @Override
    public List<Student> getActiveStudents(long classId) {
        return store.getActiveStudents(classId);
    }

    @Override
    public List<Student> getActiveStudents() {
        if (activeStudents == null) {
           activeStudents = store.getActiveStudents();
        }

        return activeStudents;
    }

    @Override
    public List<SchoolClass> getActiveClasses() {
        if (activeClasses == null) {
            activeClasses = store.getActiveClasses();
        }
        return activeClasses;
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
    public List<Student> getAttendees(long schoolClass) {
        return store.getAttendees(schoolClass);
    }

    @Override
    public void markStudentAsActive(long schoolClass, long student) {
        activeStudents = null;
        store.markStudentAsActive(schoolClass, student);
    }

    @Override
    public void markStudentAsInactive(long schoolClass, long student) {
        activeStudents = null;
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

    public void writeThrough() {
        // Just as illustration
    }
}
