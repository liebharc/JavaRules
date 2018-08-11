package com.github.liebharc.JavaRules.rules;

import com.github.liebharc.JavaRules.LazyDataStore;
import com.github.liebharc.JavaRules.verbs.StudentJoinsAClass;
import com.github.liebharc.JavaRules.verbs.StudentResignsFromClass;
import com.github.liebharc.JavaRules.verbs.Verb;

public class SignUpSignOff implements Rule {

    private LazyDataStore status;

    public SignUpSignOff(LazyDataStore status) {
        this.status = status;
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
            signOff((StudentJoinsAClass) verb);
        }

        return Result.Done;
    }

    private void signOff(StudentJoinsAClass verb) {
        final StudentJoinsAClass studentJoinsAClass = verb;
        status.markStudentAsInactive(studentJoinsAClass.getClassId(), studentJoinsAClass.getStudent());
        status.unassignStudent( studentJoinsAClass.getClassId(), studentJoinsAClass.getStudent());
    }

    private void signOn(StudentJoinsAClass verb) {
        final StudentJoinsAClass studentJoinsAClass = verb;
        status.assignStudent( studentJoinsAClass.getClassId(), studentJoinsAClass.getStudent());
        status.markStudentAsActive( studentJoinsAClass.getClassId(), studentJoinsAClass.getStudent());
    }
}
