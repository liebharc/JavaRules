package com.github.liebharc.JavaRules;

import com.github.liebharc.JavaRules.verbs.ASchoolDayHasPassed;
import com.github.liebharc.JavaRules.verbs.StudentAttendsAClass;
import com.github.liebharc.JavaRules.verbs.StudentJoinsAClass;
import com.github.liebharc.JavaRules.verbs.Verb;
import org.junit.Test;

public class PerformanceTest extends TestBase {
    private static long memoryUsed() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    @Test
    public void measure() {
        Logger.setQuiteMode(true);
        engine =  createEngine(new NoReport());

        long[] allStudents = new long[] { david, matt, peter, jodie };
        long[] allClasses = new long[] {
            dataStore.store(registration.newClass("Optional class 1", 0)),
            dataStore.store(registration.newClass("Optional class 2", 0)),
            dataStore.store(registration.newClass("Optional class 3", 0)),
            dataStore.store(registration.newClass("Optional class 4", 0)),
            dataStore.store(registration.newClass("Optional class 5", 0))
        };

        final Measurement measurement = new Measurement(engine);
        final long iterations = 100000;
        System.gc();

        for (int i = 0; i < allClasses.length; i++) {
            for (int j = 0; j < allStudents.length; j++) {
                measurement.process(new StudentJoinsAClass(allStudents[j], allClasses[i]));
            }
        }

        for (int iteration = 0; iteration < iterations; iteration++) {
            for (int i = 0; i < allClasses.length; i++) {
                for (int j = 0; j < allStudents.length; j++) {
                    measurement.process(new StudentAttendsAClass(allStudents[j], allClasses[i]));
                }
            }

            measurement.process(new ASchoolDayHasPassed());
        }

        System.out.println(measurement);
    }

    private static class Measurement {

        private Engine engine;

        private long memory = 0;
        private long time = 0;
        private long iterations = 0;

        public Measurement(Engine engine) {

            this.engine = engine;
        }

        public void process(Verb verb) {
            final long memoryStart = memoryUsed();
            final long timeStart = System.currentTimeMillis();
            engine.process(verb);
            final long timeEnd = System.currentTimeMillis();
            final long memoryEnd = memoryUsed();
            memory += (memoryEnd - memoryStart);
            time += (timeEnd - timeStart);
            iterations++;
        }

        public String convertBytes(long bytes) {
            String[] units = new String[] { "Bytes", "KB", "MB", "GB"};
            int i = 0;
            while (bytes > 1024 && i < units.length) {
                bytes /= 1024;
                i++;
            }

            return bytes + " " + units[i];
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("Memory total: " + convertBytes(memory));
            builder.append("\nTime total: " + (time) + " ms");

            builder.append("\nMemory per iteration: " + convertBytes(memory / iterations) + "/Iteration");
            builder.append("\nTime total per 1000 iterations: " + ((time) * 1000 / iterations)+" ms/1000Iterations");
            return builder.toString();
        }
    }
}
