package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.model.Student;
import com.github.liebharc.JavaRules.sharedknowledge.DataStore;
import com.github.liebharc.JavaRules.verbs.ASchoolDayHasPassed;
import com.github.liebharc.JavaRules.verbs.StudentJoinsAClass;
import com.github.liebharc.JavaRules.verbs.Verb;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ScriptTaskTest extends JbpmJUnitBaseTestCase {

    public static final TestRule RULE = new TestRule();

    @Test
    public void testProcess() {

        final KieSession kieSession =
                createRuntimeManager("Flow.bpmn2")
                        .getRuntimeEngine(null)
                        .getKieSession();

        final Logger logger = new Logger(TestRule.class);
        final Verb verb = new StudentJoinsAClass(1, 2);
        final DataStore store = new DataStore();
        final ReportStore reports = new NoReport();

        //kieSession.setGlobal("logger", logger);
        kieSession.insert(logger);
        kieSession.insert(verb);
        kieSession.insert(store);
        kieSession.insert(reports);
        //kieSession.setGlobal("reports", reports);
        kieSession.startProcess("com.github.liebharc.JavaRules.ScriptTaskTest");

        assertEquals(1,store.getStudyTime(1));
        assertEquals(2L, kieSession.getObjects().stream().filter(o -> Long.class.isInstance(o)).findFirst().get());
    }

    public static class TestRule extends RuleBase {

        @Override
        protected void process(Session session) {
            for (StudentJoinsAClass studentWithId1 : session.findAll(StudentJoinsAClass.class, s -> s.getStudent() == 1)) {
                for (DataStore store : session.findAll(DataStore.class)) {
                    store.addStudyTime(studentWithId1.getStudent(), 1);
                    session.insert(2L);
                }
            }
        }
    }


}
