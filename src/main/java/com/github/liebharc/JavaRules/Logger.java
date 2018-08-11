package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.rules.Rule;
import sun.rmi.runtime.Log;

public class Logger {
    private Class<?> caller;

    public Logger(Class<?> caller) {

        this.caller = caller;
    }

    public Logger(Rule caller) {

        this.caller = caller.getClass();
    }


    public void log( String message) {
        System.out.println(caller.getSimpleName() + ": " + message);
    }
}
