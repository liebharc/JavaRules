package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.rules.InterferenceStep;

public class Logger {
    private static boolean quiteMode = false;

    public static boolean isQuiteMode() {
        return quiteMode;
    }

    public static void setQuiteMode(boolean quiteMode) {
        Logger.quiteMode = quiteMode;
    }

    private Class<?> caller;

    public Logger(Class<?> caller) {

        this.caller = caller;
    }

    public Logger(InterferenceStep caller) {

        this.caller = caller.getClass();
    }


    public void log( String message) {
        if (!quiteMode) { System.out.println(caller.getSimpleName() + ": " + message); }
    }
}
