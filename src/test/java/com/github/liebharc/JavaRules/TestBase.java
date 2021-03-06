package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ModelFactory;
import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.model.ReportStoreImpl;
import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.sharedknowledge.SnapshotDataStore;
import org.junit.*;

public abstract class TestBase {

    protected DataAccess dataStore;
    protected ModelFactory registration;
    protected long schoolClass;
    protected long anotherSchoolClass;
    protected long david;
    protected long matt;
    protected long peter;
    protected long jodie;
    protected Engine engine;
    protected ReportStoreImpl reports;

    @Before
    public void setupAClass() {
        Logger.setQuiteMode(false);
        dataStore = createDataStore();
        registration = new ModelFactory();
        schoolClass = dataStore.store(registration.newClass("5-1", 2));
        anotherSchoolClass = dataStore.store(registration.newClass("5-2", 3));
        david = dataStore.store(registration.newStudent("David", "Tennant"));
        matt = dataStore.store(registration.newStudent("Matt", "Smith"));
        peter = dataStore.store(registration.newStudent("Peter", "Capaldi"));
        jodie = dataStore.store(registration.newStudent("Jodie", "Whittaker"));
        reports = new ReportStoreImpl();
        engine = createEngine(reports);
    }

    protected abstract Engine createEngine(ReportStore reports);

    protected DataAccess createDataStore() {
        return new DataStore();
    }
}
