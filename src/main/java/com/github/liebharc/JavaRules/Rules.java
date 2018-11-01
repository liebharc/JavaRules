package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.verbs.*;
import com.github.liebharc.JavaRules.model.*;
import com.github.liebharc.JavaRules.deduction.*;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.sharedknowledge.*;
import com.github.liebharc.JavaRules.Token;
import java.util.*;

public class Rules extends RuleBase {

    public static final Rules INSTANCE = new Rules();

    @Override
    protected void process(Session session) {
        // SignUpSignOff
        ruleSignOn(session);
        ruleSignOff(session);

        // StudentStatus
        ruleSetInactive(session);
        ruleSetActive(session);
        ruleMarkAsAttended(session);

        // MissedClassesAggregation
        ruleMissedClassesAggregation(session);

        // TimeAggregation
        ruleTimeAggregation(session);

        // ClassCompletion
        ruleSuccessfulCompletion(session);
        ruleFailureToComplete(session);

        // InitRule
        ruleInit(session);

        // ReportWriter
        ruleReportAttendance(session);
        ruleReportMiss(session);
        ruleReportLeave(session);
        ruleReportTime(session);
    }

    private void ruleSignOn(Session session) {
        final Logger logger = session.getLogger();
        final DataAccess store = session.getDataAccess();
        for (StudentJoinsAClass studentJoinsAClass : session.findAll(StudentJoinsAClass.class)) {
            long student = studentJoinsAClass.getStudent();
            long classId = studentJoinsAClass.getClassId();
            session.insertToken("SignOn", student);
            logger.log("Student " + student + " signed on to class " + classId);
            store.assignStudent( classId, student);
            store.markStudentAsActive( classId, student);
        }
    }

    private void ruleMissedClassesAggregation(Session session) {
        final Logger logger = session.getLogger();
        final DataAccess store = session.getDataAccess();
        for (ASchoolDayHasPassed aSchoolDayHasPassed : session.findAll(ASchoolDayHasPassed.class)) {
            session.insertDebugToken("MissedClassesAggregation");
            for (SchoolClass schoolClass : store.getActiveClasses()) {
                List<Student> activeStudents = store.getActiveStudents(schoolClass.getId());
                List<Student> attendees = store.getAttendees(schoolClass.getId());
                for (Student attendee : new HashSet<>(attendees)) {
                    logger.log(attendee + " has attended class " + schoolClass);
                    session.insert(new StudentAttendedClass(attendee, schoolClass));
                }

                List<Student> misses = new ArrayList<>(activeStudents);
                misses.removeAll(attendees);
                for (Student miss : misses) {
                    logger.log(miss + " has missed class " + schoolClass);
                    store.incrementClassesMissed(miss.getId());
                    session.insert(new StudentMissedClass(miss, schoolClass, store.getNumberOfMissedClasses(miss.getId())));
                }
            }
        }
    }

    private void ruleSetInactive(Session session) {
        final Logger logger = session.getLogger();
        final DataAccess store = session.getDataAccess();
        for (StudentBecomesSick studentBecomesSick : session.findAll(StudentBecomesSick.class)) {
            long student = studentBecomesSick.getStudent();
            session.insertDebugToken("setInactive", student);
            logger.log("Student " + student + " became sick");
            for (SchoolClass schoolClass : store.getAssignedClasses(student)) {
                store.markStudentAsInactive(schoolClass.getId(), student);
            }
        }
    }

    private void ruleSetActive(Session session) {
        final Logger logger = session.getLogger();
        final DataAccess store = session.getDataAccess();
        for (StudentReturnsFromSickness studentReturnsFromSickness : session.findAll(StudentReturnsFromSickness.class)) {
            long student = studentReturnsFromSickness.getStudent();
            session.insertDebugToken("setActive", student);
            logger.log("Student " + student + " returned to classes");
            for (SchoolClass schoolClass : store.getAssignedClasses(student)) {
                store.markStudentAsActive(schoolClass.getId(), student);
            }
        }
    }

    private void ruleMarkAsAttended(Session session) {
        final Logger logger = session.getLogger();
        final DataAccess store = session.getDataAccess();
        for (StudentAttendsAClass studentAttendsAClass : session.findAll(StudentAttendsAClass.class)) {
            long student = studentAttendsAClass.getStudent();
            session.insertDebugToken("markAsAttended", student);
            logger.log("Student " + student + " has attended class");
            for (SchoolClass schoolClass : store.getAssignedClasses(student)) {
                store.markAsAttended(schoolClass.getId(), student);
            }
        }
    }

    private void ruleTimeAggregation(Session session) {
        final Logger logger = session.getLogger();
        final DataAccess store = session.getDataAccess();
        for (ASchoolDayHasPassed aSchoolDayHasPassed : session.findAll(ASchoolDayHasPassed.class)) {
            for (StudentAttendedClass studentSchoolClassPair : session.findAll(StudentAttendedClass.class)) {
                Student student = studentSchoolClassPair.getStudent();
                SchoolClass schoolClass = studentSchoolClassPair.getSchoolClass();
                logger.log(student + " attended class "  + schoolClass + " and got accounted "+ schoolClass.getHoursADay());
                store.addStudyTime(student.getId(), schoolClass.getHoursADay());
                session.insert(new AggregatedTimeUpdate(student, store.getStudyTime(student.getId())));
            }
        }
    }

    private void ruleSuccessfulCompletion(Session session) {
        final Logger logger = session.getLogger();
        final DataAccess store = session.getDataAccess();
        for (AggregatedTimeUpdate aggregatedTimeUpdate : session.findAll(AggregatedTimeUpdate.class)) {
            Student student = aggregatedTimeUpdate.getStudent();
            long time = aggregatedTimeUpdate.getAttendedTime();
            if (time > 10) {
                logger.log("Student " + student + " completed his studies");
                for (SchoolClass schoolClass : store.getAssignedClasses(student.getId())) {
                    store.markStudentAsInactive(schoolClass.getId(), student.getId());
                    store.unassignStudent(schoolClass.getId(), student.getId());
                }
                session.insert(new StudentLeaves(student, true));
            }
        }
    }

    private void ruleFailureToComplete(Session session) {
        final Logger logger = session.getLogger();
        final DataAccess store = session.getDataAccess();
        for (StudentMissedClass studentMissedClass : session.findAll(StudentMissedClass.class)) {
            Student student = studentMissedClass.getStudent();
            long misses = studentMissedClass.getMisses();
            if (misses >= 5) {
                logger.log("Student " + student + " missed too many classes");
                for (SchoolClass schoolClass : store.getAssignedClasses(student.getId())) {
                    store.markStudentAsInactive(schoolClass.getId(), student.getId());
                    store.unassignStudent(schoolClass.getId(), student.getId());
                }
                session.insert(new StudentLeaves(student, false));
            }
        }
    }

    private void ruleReportAttendance(Session session) {
        final ReportStore reports = session.getReportStore();
        for (StudentAttendedClass studentAttendedClass : session.findAll(StudentAttendedClass.class)) {
            Student student = studentAttendedClass.getStudent();
            SchoolClass schoolClass = studentAttendedClass.getSchoolClass();
            reports.writeReport(student.getId(), student.getFirstName() + " attended class " + schoolClass.getName());
        }
    }

    private void ruleReportMiss(Session session) {
        final ReportStore reports = session.getReportStore();
        for (StudentMissedClass studentMissedClass : session.findAll(StudentMissedClass.class)) {
            Student student = studentMissedClass.getStudent();
            SchoolClass schoolClass = studentMissedClass.getSchoolClass();
            reports.writeReport(student.getId(), student.getFirstName() + " missed class " + schoolClass.getName());
        }
    }

    private void ruleReportLeave(Session session) {
        final ReportStore reports = session.getReportStore();
        for (StudentLeaves leave : session.findAll(StudentLeaves.class)) {
            Student student = leave.getStudent();
            if ( leave.isSuccessfulCompletion()) {
                reports.writeReport(student.getId(), student.getFirstName() + " completed his classes :)");
            }
            else {
                reports.writeReport(student.getId(), student.getFirstName() + " got expelled :(");
            }
        }
    }

    private void ruleReportTime(Session session) {
        final ReportStore reports = session.getReportStore();
        final DataAccess store = session.getDataAccess();
        for (ASchoolDayHasPassed aSchoolDayHasPassed : session.findAll(ASchoolDayHasPassed.class)) {
            for (Student student : store.getActiveStudents()) {
                reports.writeReport(student.getId(), "A day passed");
            }
        }
    }

    private void ruleInit(Session session) {
        final Logger logger = session.getLogger();
        final DataAccess store = session.getDataAccess();
        for (ASchoolDayHasPassed aSchoolDayHasPassed : session.findAll(ASchoolDayHasPassed.class)) {
            session.insertDebugToken("Init");
            logger.log("Clearing store");
            for (SchoolClass schoolClass : store.getActiveClasses()) {
                store.clearAttendees(schoolClass.getId());
            }
        }
    }

    private void ruleSignOff(Session session) {
        final Logger logger = session.getLogger();
        final DataAccess store = session.getDataAccess();
        for (StudentResignsFromClass studentResignsFromClass : session.findAll(StudentResignsFromClass.class)) {
            if (session.notExistsToken(token -> token.getType().equals("SignOn")
                    && token.getId() == studentResignsFromClass.getStudent())) {
                long student = studentResignsFromClass.getStudent();
                long classId = studentResignsFromClass.getClassId();
                session.insertDebugToken("SignOff", student);
                logger.log("Student " + student + " signed off to class " + classId);
                store.markStudentAsInactive( classId, student);
                store.unassignStudent( classId, student);
            }
        }
    }

}
