package com.github.liebharc.JavaRules;


import com.github.liebharc.JavaRules.model.ModelFactory;
import com.github.liebharc.JavaRules.model.SchoolClass;
import com.github.liebharc.JavaRules.verbs.StudentJoinsAClass;
import org.junit.Assert;
import org.junit.Test;

public class ConceptTest {

    @Test
    public void buildAClass() {
        final DataStore dataStore = new DataStore();
        final ModelFactory registration = new ModelFactory();
        final long schoolClass = dataStore.store(registration.newClass("5-1"));
        final long david = dataStore.store(registration.newStudent("David" ,"Tennant"));
        final long matt = dataStore.store(registration.newStudent("Matt" ,"Smith"));
        final long peter = dataStore.store(registration.newStudent("Peter" ,"Capaldi"));;
        final long jodie = dataStore.store(registration.newStudent("Jodie" ,"Whittaker"));
        final Engine engine = new Engine(dataStore);
        engine.process(new StudentJoinsAClass(david, schoolClass));
        engine.process(new StudentJoinsAClass(matt, schoolClass));
        engine.process(new StudentJoinsAClass(peter, schoolClass));
        engine.process(new StudentJoinsAClass(jodie, schoolClass));

        Assert.assertTrue(dataStore.isAssigned(david, schoolClass));
        Assert.assertTrue(dataStore.isAssigned(matt, schoolClass));
        Assert.assertTrue(dataStore.isAssigned(peter, schoolClass));
        Assert.assertTrue(dataStore.isAssigned(jodie, schoolClass));

        Assert.assertTrue(dataStore.isActive(david, schoolClass));
        Assert.assertTrue(dataStore.isActive(matt, schoolClass));
        Assert.assertTrue(dataStore.isActive(peter, schoolClass));
        Assert.assertTrue(dataStore.isActive(jodie, schoolClass));
    }
}