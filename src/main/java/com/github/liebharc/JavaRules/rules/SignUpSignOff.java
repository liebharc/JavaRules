package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.DataStore;
import com.github.liebharc.JavaRules.Logger;
import com.github.liebharc.JavaRules.verbs.StudentJoinsAClass;
import com.github.liebharc.JavaRules.verbs.StudentResignsFromClass;
import com.github.liebharc.JavaRules.verbs.Verb;

public class SignUpSignOff implements Rule {
    private final Logger logger = new Logger(this);

    private DataStore store;

    public SignUpSignOff(DataStore status) {
        this.store = status;
    }

    @Override
    public void process(Verb verb) {
        final boolean isSignOn  = verb instanceof StudentJoinsAClass;
        final boolean isSignOff  = verb instanceof StudentResignsFromClass;
        final boolean isChange = isSignOn || isSignOff;
        if (!isChange) {
            return ;
        }

        if (isSignOn) {
            signOn((StudentJoinsAClass) verb);
        }

        if (isSignOff) {
            signOff((StudentResignsFromClass) verb);
        }
    }

    private void signOff(StudentResignsFromClass verb) {
        logger.log("Student " + verb.getStudent() + " signed off from class " + verb.getClassId());
        store.markStudentAsInactive(verb.getClassId(), verb.getStudent());
        store.unassignStudent( verb.getClassId(), verb.getStudent());
    }

    private void signOn(StudentJoinsAClass verb) {
        logger.log("Student " + verb.getStudent() + " signed on to class " + verb.getClassId());
        store.assignStudent( verb.getClassId(), verb.getStudent());
        store.markStudentAsActive( verb.getClassId(), verb.getStudent());
    }
}
