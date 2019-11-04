package com.github.liebharc.JavaRules.sharedknowledge;

import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;

import java.util.Collection;
import java.util.List;

public interface DataAccess  {
    Collection<SchoolClass> getAssignedClasses(long studentId);

    Collection<Student> getActiveStudents(long classId);

    Collection<Student> getActiveStudents();

    Collection<SchoolClass> getActiveClasses();

    void assignStudent(long schoolClass, long student);

    void unassignStudent(long schoolClass, long student);

    void markAsAttended(long schoolClass, long student);

    void clearAttendees(long schoolClass);

    Collection<Student> getAttendees(long schoolClass);

    void markStudentAsActive(long schoolClass, long student);

    void markStudentAsInactive(long schoolClass, long student);

    void addStudyTime(long student, int time);

    int getStudyTime(long student);

    void incrementClassesMissed(long student);

    int getNumberOfMissedClasses(long student);

    boolean isAssigned(Long student, Long schoolClass);

    boolean isActive(Long student, Long schoolClass);

    long store(SchoolClass schoolClass);

    long store(Student student);
}
