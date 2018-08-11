package com.github.liebharc.JavaRules.rules;


import com.github.liebharc.JavaRules.deduction.Fact;
import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.deduction.StudentAttendedClass;
import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.verbs.Verb;

import java.util.Collection;

public class ReportWriter implements InterferenceStep {
    private ReportStore store;

    public ReportWriter(ReportStore store) {

        this.store = store;
    }

    @Override
    public void process(Verb verb, Facts facts) {
        for (StudentAttendedClass studentAttendedClass : facts.getFacts(StudentAttendedClass.class)) {
            Student student = studentAttendedClass.getStudent();
            SchoolClass schoolClass = studentAttendedClass.getSchoolClass();
            store.writeReport(student.getId(), student.getFirstName() + " attended class " + schoolClass.getName());
        }
    }
}
