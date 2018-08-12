package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.deduction.Facts;
import com.github.liebharc.JavaRules.verbs.StudentJoinsAClass;
import com.github.liebharc.JavaRules.verbs.StudentResignsFromClass;
import com.github.liebharc.JavaRules.verbs.Verb;

public class SignUpSignOff implements InterferenceStep {
    private final Logger logger = new Logger(this);

    @Override
    public void process(Verb verb, Facts facts) {
        final boolean isSignOn  = verb instanceof StudentJoinsAClass;
        final boolean isSignOff  = verb instanceof StudentResignsFromClass;
        final boolean isChange = isSignOn || isSignOff;
        if (!isChange) {
            return ;
        }

        if (isSignOn) {
            signOn((StudentJoinsAClass) verb, facts.getStore());
        }

        if (isSignOff) {
            signOff((StudentResignsFromClass) verb, facts.getStore());
        }
    }

    private void signOff(StudentResignsFromClass verb, DataAccess store) {
        logger.log("Student " + verb.getStudent() + " signed off from class " + verb.getClassId());
        store.markStudentAsInactive(verb.getClassId(), verb.getStudent());
        store.unassignStudent( verb.getClassId(), verb.getStudent());
    }

    private void signOn(StudentJoinsAClass verb, DataAccess store) {
        logger.log("Student " + verb.getStudent() + " signed on to class " + verb.getClassId());
        store.assignStudent( verb.getClassId(), verb.getStudent());
        store.markStudentAsActive( verb.getClassId(), verb.getStudent());
    }
}
