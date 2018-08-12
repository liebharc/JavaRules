package com.github.liebharc.JavaRules.rules;


import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.deduction.StudentAttendedClass;
import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
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
    }
}
