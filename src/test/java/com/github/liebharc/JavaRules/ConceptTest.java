package com.github.liebharc.JavaRules;


import com.github.liebharc.JavaRules.model.ModelFactory;
import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.verbs.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ConceptTest {

    private DataStore dataStore;
    private ModelFactory registration;
    private long schoolClass;
    private long anotherSchoolClass;
    private long david;
    private long matt;
    private long peter;
    private long jodie;
    private Engine engine;
    private ReportStore reports;

    @Before
    public void setupAClass() {
        dataStore = new DataStore();
        registration = new ModelFactory();
        schoolClass = dataStore.store(registration.newClass("5-1", 2));
        anotherSchoolClass = dataStore.store(registration.newClass("5-2", 3));
        david = dataStore.store(registration.newStudent("David" ,"Tennant"));
        matt = dataStore.store(registration.newStudent("Matt" ,"Smith"));
        peter = dataStore.store(registration.newStudent("Peter" ,"Capaldi"));;
        jodie = dataStore.store(registration.newStudent("Jodie" ,"Whittaker"));
        reports = new ReportStore();
        engine = new Engine(dataStore, reports);
    }

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
    @Ignore
    public void ongoingReport() {
        engine.process(new StudentJoinsAClass(david, schoolClass));
        Assert.assertEquals("David attended class 5-1", reports.getReport(david));
    }

    @Test
    public void successfulCompletionReport() {

    }

    @Test
    public void failureCompletionReport() {

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