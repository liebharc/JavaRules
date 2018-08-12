package com.github.liebharc.JavaRules.rules;


import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.deduction.StudentAttendedClass;
import com.github.liebharc.JavaRules.deduction.StudentLeaves;
import com.github.liebharc.JavaRules.deduction.StudentMissedClass;
import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.verbs.ASchoolDayHasPassed;
import com.github.liebharc.JavaRules.verbs.Verb;

public class ReportWriter implements InterferenceStep {
    private ReportStore report;

    public ReportWriter(ReportStore report) {

        this.report = report;
    }

    @Override
    public void process(Verb verb, Facts facts) {
        for (StudentAttendedClass studentAttendedClass : facts.getFacts(StudentAttendedClass.class)) {
            Student student = studentAttendedClass.getStudent();
            SchoolClass schoolClass = studentAttendedClass.getSchoolClass();
            report.writeReport(student.getId(), student.getFirstName() + " attended class " + schoolClass.getName());
        }

        for (StudentMissedClass studentMissedClass : facts.getFacts(StudentMissedClass.class)) {
            Student student = studentMissedClass.getStudent();
            SchoolClass schoolClass = studentMissedClass.getSchoolClass();
            report.writeReport(student.getId(), student.getFirstName() + " missed class " + schoolClass.getName());
        }

        for (StudentLeaves leave : facts.getFacts(StudentLeaves.class)) {
            Student student = leave.getStudent();
            if ( leave.isSuccessfulCompletion()) {
                report.writeReport(student.getId(), student.getFirstName() + " completed his classes :)");
            }
            else {
                report.writeReport(student.getId(), student.getFirstName() + " got expelled :(");
            }
        }

        if (verb instanceof ASchoolDayHasPassed) {
            DataAccess store = facts.getStore();
            for (Student student : store.getActiveStudents()) {
                report.writeReport(student.getId(), "A day passed");
            }

        }
    }
}
