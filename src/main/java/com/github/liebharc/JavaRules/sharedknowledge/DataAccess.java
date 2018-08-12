package com.github.liebharc.JavaRules.sharedknowledge;

import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;

import java.util.List;

public interface DataAccess  {
    List<SchoolClass> getAssignedClasses(long studentId);

    List<Student> getActiveStudents(long classId);

    List<SchoolClass> getActiveClasses();

    void assignStudent(long schoolClass, long student);

    void unassignStudent(long schoolClass, long student);

    void markAsAttended(long schoolClass, long student);

    void clearAttendees(long schoolClass);

    List<Student> getAttendees(long schoolClass);

    void markStudentAsActive(long schoolClass, long student);

    void markStudentAsInactive(long schoolClass, long student);

    void addStudyTime(long student, int time);

    int getStudyTime(long student);

    void incrementClassesMissed(long student);

    int getNumberOfMissedClasses(long student);
}
