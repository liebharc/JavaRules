package com.github.liebharc.JavaRules;


import com.github.liebharc.JavaRules.TestBase;
import com.github.liebharc.JavaRules.model.ModelFactory;
import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.verbs.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public abstract class UnitTestBase extends TestBase {

    @Test
    public void buildAClass() {

        signUpAllStudents();

        Assert.assertTrue(dataStore.isAssigned(david, schoolClass));
        Assert.assertTrue(dataStore.isAssigned(matt, schoolClass));
        Assert.assertTrue(dataStore.isAssigned(peter, schoolClass));
        Assert.assertTrue(dataStore.isAssigned(jodie, schoolClass));

        Assert.assertTrue(dataStore.isActive(david, schoolClass));
        Assert.assertTrue(dataStore.isActive(matt, schoolClass));
        Assert.assertTrue(dataStore.isActive(peter, schoolClass));
        Assert.assertTrue(dataStore.isActive(jodie, schoolClass));
    }

    @Test
    public void studentsAreDoneWithClass() {

        signUpAllStudents();
        signOffAllStudents();

        Assert.assertFalse(dataStore.isAssigned(david, schoolClass));
        Assert.assertFalse(dataStore.isAssigned(matt, schoolClass));
        Assert.assertFalse(dataStore.isAssigned(peter, schoolClass));
        Assert.assertFalse(dataStore.isAssigned(jodie, schoolClass));

        Assert.assertFalse(dataStore.isActive(david, schoolClass));
        Assert.assertFalse(dataStore.isActive(matt, schoolClass));
        Assert.assertFalse(dataStore.isActive(peter, schoolClass));
        Assert.assertFalse(dataStore.isActive(jodie, schoolClass));
    }

    @Test
    public void studentBecomesSick() {
        signUpAllStudents();
        engine.process(new StudentBecomesSick(david));

        Assert.assertTrue(dataStore.isAssigned(david, schoolClass));
        Assert.assertFalse(dataStore.isActive(david, schoolClass));

        Assert.assertFalse(dataStore.isAssigned(david, anotherSchoolClass));
        Assert.assertFalse(dataStore.isActive(david, anotherSchoolClass));
    }

    @Test
    public void studentReturnsFromSickness() {
        signUpAllStudents();
        engine.process(new StudentBecomesSick(david));
        engine.process(new StudentReturnsFromSickness(david));

        Assert.assertTrue(dataStore.isAssigned(david, schoolClass));
        Assert.assertTrue(dataStore.isActive(david, schoolClass));

        Assert.assertFalse(dataStore.isAssigned(david, anotherSchoolClass));
        Assert.assertFalse(dataStore.isActive(david, anotherSchoolClass));
    }

    @Test
    public void timeAggregation() {
        signUpAllStudents();

        engine.process(new StudentAttendsAClass(david, schoolClass));
        engine.process(new ASchoolDayHasPassed());
        Assert.assertEquals(2, dataStore.getStudyTime(david));

        engine.process(new StudentBecomesSick(david));
        engine.process(new ASchoolDayHasPassed());
        Assert.assertEquals(2, dataStore.getStudyTime(david));

        engine.process(new StudentReturnsFromSickness(david));
        engine.process(new StudentAttendsAClass(david, schoolClass));
        engine.process(new ASchoolDayHasPassed());
        Assert.assertEquals(4, dataStore.getStudyTime(david));


        engine.process(new StudentJoinsAClass(david, anotherSchoolClass));
        engine.process(new StudentAttendsAClass(david, schoolClass));
        engine.process(new StudentAttendsAClass(david, anotherSchoolClass));
        engine.process(new ASchoolDayHasPassed());
        Assert.assertEquals(9, dataStore.getStudyTime(david));

        engine.process(new StudentResignsFromClass(david, schoolClass));
        engine.process(new StudentAttendsAClass(david, anotherSchoolClass));
        engine.process(new ASchoolDayHasPassed());
        Assert.assertEquals(12, dataStore.getStudyTime(david));

    }

    @Test
    public void missesDoNotGiveTime() {
        signUpAllStudents();

        engine.process(new ASchoolDayHasPassed());
        Assert.assertEquals(0, dataStore.getStudyTime(david));
    }

    @Test
    public void countClassMisses() {
        signUpAllStudents();

        engine.process(new StudentAttendsAClass(david, schoolClass));
        engine.process(new ASchoolDayHasPassed());
        Assert.assertEquals(0, dataStore.getNumberOfMissedClasses(david));

        engine.process(new StudentBecomesSick(david));
        engine.process(new ASchoolDayHasPassed());
        Assert.assertEquals(0, dataStore.getNumberOfMissedClasses(david));

        engine.process(new StudentReturnsFromSickness(david));
        engine.process(new ASchoolDayHasPassed());
        Assert.assertEquals(1, dataStore.getNumberOfMissedClasses(david));


        engine.process(new StudentJoinsAClass(david, anotherSchoolClass));
        engine.process(new StudentAttendsAClass(david, schoolClass));
        engine.process(new StudentAttendsAClass(david, anotherSchoolClass));
        engine.process(new ASchoolDayHasPassed());
        Assert.assertEquals(1, dataStore.getNumberOfMissedClasses(david));

        engine.process(new StudentResignsFromClass(david, schoolClass));
        engine.process(new StudentAttendsAClass(david, anotherSchoolClass));
        engine.process(new ASchoolDayHasPassed());
        Assert.assertEquals(1, dataStore.getNumberOfMissedClasses(david));

    }

    @Test
    public void PassAClass() {
        engine.process(new StudentJoinsAClass(david, anotherSchoolClass));

        engine.process(new StudentAttendsAClass(david, anotherSchoolClass));
        engine.process(new ASchoolDayHasPassed());

        engine.process(new StudentAttendsAClass(david, anotherSchoolClass));
        engine.process(new ASchoolDayHasPassed());

        engine.process(new StudentAttendsAClass(david, anotherSchoolClass));
        engine.process(new ASchoolDayHasPassed());

        Assert.assertTrue(dataStore.isAssigned(david, anotherSchoolClass));
        Assert.assertTrue(dataStore.isActive(david, anotherSchoolClass));

        engine.process(new StudentAttendsAClass(david, anotherSchoolClass));
        engine.process(new ASchoolDayHasPassed());

        Assert.assertFalse(dataStore.isAssigned(david, anotherSchoolClass));
        Assert.assertFalse(dataStore.isActive(david, anotherSchoolClass));
    }

    @Test
    public void studentsMissedTooManyClasses() {

        signUpAllStudents();
        engine.process(new ASchoolDayHasPassed());
        engine.process(new ASchoolDayHasPassed());
        engine.process(new ASchoolDayHasPassed());
        engine.process(new ASchoolDayHasPassed());

        Assert.assertTrue(dataStore.isAssigned(david, schoolClass));
        Assert.assertTrue(dataStore.isActive(david, schoolClass));

        engine.process(new ASchoolDayHasPassed());

        Assert.assertFalse(dataStore.isAssigned(david, schoolClass));
        Assert.assertFalse(dataStore.isActive(david, schoolClass));
    }

    @Test
    public void ongoingReport() {
        engine.process(new StudentJoinsAClass(david, schoolClass));
        engine.process(new StudentAttendsAClass(david, schoolClass));
        engine.process(new ASchoolDayHasPassed());
        Assert.assertEquals("David attended class 5-1\nA day passed", reports.getReport(david));
    }

    @Test
    public void successfulCompletionReport() {
        engine.process(new StudentJoinsAClass(david, anotherSchoolClass));

        engine.process(new StudentAttendsAClass(david, anotherSchoolClass));
        engine.process(new ASchoolDayHasPassed());

        engine.process(new StudentAttendsAClass(david, anotherSchoolClass));
        engine.process(new ASchoolDayHasPassed());

        engine.process(new StudentAttendsAClass(david, anotherSchoolClass));
        engine.process(new ASchoolDayHasPassed());

        Assert.assertTrue(dataStore.isAssigned(david, anotherSchoolClass));
        Assert.assertTrue(dataStore.isActive(david, anotherSchoolClass));

        engine.process(new StudentAttendsAClass(david, anotherSchoolClass));
        engine.process(new ASchoolDayHasPassed());

        final String expected =
                "David attended class 5-2\n" +
                        "A day passed\n" +
                        "David attended class 5-2\n" +
                        "A day passed\n" +
                        "David attended class 5-2\n" +
                        "A day passed\n" +
                        "David attended class 5-2\n" +
                        "David completed his classes :)";

        Assert.assertEquals(expected, reports.getReport(david));
    }

    @Test
    public void failureCompletionReport() {
        signUpAllStudents();
        engine.process(new ASchoolDayHasPassed());
        engine.process(new ASchoolDayHasPassed());
        engine.process(new ASchoolDayHasPassed());
        engine.process(new ASchoolDayHasPassed());
        engine.process(new ASchoolDayHasPassed());

        final String expected =
                "David missed class 5-1\n"
                + "A day passed\n"
                + "David missed class 5-1\n"
                + "A day passed\n"
                + "David missed class 5-1\n"
                + "A day passed\n"
                + "David missed class 5-1\n"
                + "A day passed\n"
                + "David missed class 5-1\n"
                + "David got expelled :(";

        Assert.assertEquals(expected, reports.getReport(david));
    }

    private void signUpAllStudents() {
        engine.process(new StudentJoinsAClass(david, schoolClass));
        engine.process(new StudentJoinsAClass(matt, schoolClass));
        engine.process(new StudentJoinsAClass(peter, schoolClass));
        engine.process(new StudentJoinsAClass(jodie, schoolClass));
    }

    private void signOffAllStudents() {
        engine.process(new StudentResignsFromClass(david, schoolClass));
        engine.process(new StudentResignsFromClass(matt, schoolClass));
        engine.process(new StudentResignsFromClass(peter, schoolClass));
        engine.process(new StudentResignsFromClass(jodie, schoolClass));
    }
}