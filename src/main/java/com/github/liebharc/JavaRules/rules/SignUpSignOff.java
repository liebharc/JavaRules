package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.LazyDataStore;
import com.github.liebharc.JavaRules.verbs.StudentJoinsAClass;
import com.github.liebharc.JavaRules.verbs.StudentResignsFromClass;
import com.github.liebharc.JavaRules.verbs.Verb;

public class SignUpSignOff implements Rule {

    private LazyDataStore store;

    public SignUpSignOff(LazyDataStore status) {
        this.store = status;
    }

    @Override
    public Result process(Verb verb) {
        final boolean isSignOn  = verb instanceof StudentJoinsAClass;
        final boolean isSignOff  = verb instanceof StudentResignsFromClass;
        final boolean isChange = isSignOn || isSignOff;
        if (!isChange) {
            return Result.NoAction;
        }

        if (isSignOn) {
            signOn((StudentJoinsAClass) verb);
        }

        if (isSignOff) {
            signOff((StudentResignsFromClass) verb);
        }

        return Result.Done;
    }

    private void signOff(StudentResignsFromClass verb) {
        store.markStudentAsInactive(verb.getClassId(), verb.getStudent());
        store.unassignStudent( verb.getClassId(), verb.getStudent());
    }

    private void signOn(StudentJoinsAClass verb) {
        store.assignStudent( verb.getClassId(), verb.getStudent());
        store.markStudentAsActive( verb.getClassId(), verb.getStudent());
    }
}
