package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.model.StudyTime;
import com.github.liebharc.JavaRules.verbs.Verb;

import java.util.*;
import java.util.stream.Collectors;

public class DataStore {
    private Map<Long, SchoolClass> classes = new HashMap<>();

    private Map<Long, Student> students = new HashMap<>();

    private Map<Long, List<Student>> assignedStudents = new HashMap<>();

    private Map<Long, List<Student>> activeStudents = new HashMap<>();

    public Map<Long, StudyTime> studyTimes = new HashMap<>();

    public List<SchoolClass> getAssignedClasses(long studentId)  {
        return getClassesForStudent(assignedStudents, studentId);
    }

    private  List<SchoolClass> getClassesForStudent( Map<Long, List<Student>> map, long studentId) {
        // Bad performance here is actually a good example to avoid reevaluation of the method
        final List<SchoolClass> result = new ArrayList<>();

        for (Long classId : map.keySet()) {
            final List<Student> students = map.get(classId);
            if (students.stream().anyMatch(s -> s.getId() == studentId)) {
                result.add(classes.get(classId));
            }
        }

        return result;
    }

    public List<Student> getActiveStudents(long classId)  {
        List<Student> students = activeStudents.get(classId);
        if (students == null) {
            return new ArrayList<>();
        }

        return students;
    }

    public List<SchoolClass> getActiveClasses() {
        return classes.values().stream().collect(Collectors.toList());
    }

    public void assignStudent(long schoolClass, long student) {
        addToMap(assignedStudents, classes.get(schoolClass), students.get(student));
    }

    public void unassignStudent(long schoolClass, long student) {
        removeFromMap(assignedStudents, classes.get(schoolClass), students.get(student));
    }

    public void markStudentAsActive(long schoolClass, long student) {
        addToMap(activeStudents, classes.get(schoolClass), students.get(student));
    }

    public void markStudentAsInactive(long schoolClass, long student) {
        removeFromMap(activeStudents, classes.get(schoolClass), students.get(student));
    }

    public void addStudyTime(long student, long time) {
        if (studyTimes.containsKey(student)) {
            time += studyTimes.get(student).getTime();
        }

        studyTimes.put(student, new StudyTime(student, time));
    }

    public long getStudyTime(long student) {
        StudyTime studyTime = studyTimes.get(student);
        if (studyTime == null) {
            return 0;
        }
        return studyTime.getTime();
    }

    public boolean isAssigned(Long student, Long schoolClass) {
        return contains(assignedStudents, student, schoolClass);
    }

    public boolean isActive(Long student, Long schoolClass) {
        return contains(activeStudents, student, schoolClass);
    }

    private static void addToMap(Map<Long, List<Student>> map, SchoolClass schoolClass, Student student) {
        if (map.containsKey(schoolClass.getId())) {
            map.get(schoolClass.getId()).add(student);
        }
        else {
            final List<Student> students = new ArrayList<>();
            students.add(student);
            map.put(schoolClass.getId(), students);
        }
    }

    private static  void removeFromMap(Map<Long, List<Student>> map, SchoolClass schoolClass, Student student) {
        if (map.containsKey(schoolClass.getId())) {
            List<Student> students = map.get(schoolClass.getId());
            students.remove(student);
            if (students.size() == 0) {
                map.remove(schoolClass.getId());
            }
        }
        // Ignoring error handling in this example
    }

    public long store(SchoolClass schoolClass) {
        classes.put(schoolClass.getId(), schoolClass);
        return schoolClass.getId();
    }

    public long store(Student student) {
        students.put(student.getId(), student);
        return student.getId();
    }

    private static boolean contains( Map<Long, List<Student>> map, Long student, Long schoolClass) {
        List<Student> students = map.get(schoolClass);
        if (students == null) {
            return false;
        }

        return students.stream().anyMatch(s -> s.getId() == student);
    }
}
