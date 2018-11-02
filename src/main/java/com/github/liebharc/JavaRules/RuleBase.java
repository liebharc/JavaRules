package com.github.liebharc.JavaRules;


import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessContext;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class RuleBase {

    public final void process(ProcessContext processContext) {
        final KieSession kieSession = (StatefulKnowledgeSession) processContext.getKieRuntime();
        final DroolsSession session = new DroolsSession(kieSession);
        process(session);
    }

    public final void process(Object verb, DataAccess dataAccess, Logger logger, ReportStore reports) {
        final List<Object> facts = new ArrayList<>(1);
        facts.add(verb);
        final JavaSession session = new JavaSession(facts, dataAccess, logger, reports);
        process(session);
    }

    protected abstract void process(Session session);

    protected static final class JavaSession implements com.github.liebharc.JavaRules.Session {

        private final List<Object> facts;

        private final DataAccess dataAccess;

        private final List<Token> tokens = new ArrayList<>();

        private final Logger logger;

        private final ReportStore reports;

        private JavaSession(List<Object> facts, DataAccess dataAccess, Logger logger, ReportStore reports) {

            this.facts = facts;
            this.dataAccess = dataAccess;
            this.logger = logger;
            this.reports = reports;
        }

        @Override
        public<T> Iterable<T> findAll(Class<T> clazz) {
            return (List<T> )facts.stream().filter(o -> clazz.isInstance(o)).collect(Collectors.toList());
        }

        @Override
        public<T> Iterable<T> findAll(Class<T> clazz, Predicate<? super T> predicate) {
            return (List<T> )facts
                    .stream()
                    .filter(o -> clazz.isInstance(o) && predicate.test((T)o))
                    .collect(Collectors.toList());
        }

        @Override
        public boolean notExistsToken(Predicate<Token> predicate) {
            return !tokens
                    .stream()
                    .anyMatch(o -> o instanceof Token && predicate.test(o));
        }

        @Override
        public void insert(Object obj) {
            facts.add(obj);
        }

        @Override
        public Logger getLogger() {
            return logger;
        }

        @Override
        public void insertToken(String type, long id) {
            tokens.add(new Token(type, id));
        }

        @Override
        public void insertDebugToken(String missedClassesAggregation) {

        }

        @Override
        public void insertDebugToken(String missedClassesAggregation, long id) {

        }

        @Override
        public DataAccess getDataAccess() {
            return dataAccess;
        }

        @Override
        public ReportStore getReportStore() {
            return reports;
        }
    }


    protected static final class DroolsSession implements com.github.liebharc.JavaRules.Session {
        private KieSession session;

        private DataAccess dataAccess = null;

        private List<Token> tokens = new ArrayList<>();

        private List<Object> objectsCache = null;

        private ReportStore reportStore = null;

        private Logger logger = null;

        private DroolsSession(KieSession session) {
            this.session = session;
        }

        @Override
        public<T> Iterable<T> findAll(Class<T> clazz) {
            return (Iterable<T> )this.getObjectsStream().filter(o -> clazz.isInstance(o)).collect(Collectors.toList());
        }

        @Override
        public<T> Iterable<T> findAll(Class<T> clazz, Predicate<? super T> predicate) {
            return (List<T> )this.getObjectsStream()
                    .filter(o -> clazz.isInstance(o) && predicate.test((T)o))
                    .collect(Collectors.toList());
        }

        private Stream<Object> getObjectsStream() {
            if (objectsCache == null) {
                objectsCache = new ArrayList<>(session.getObjects());
            }

            return objectsCache.stream();
        }

        @Override
        public boolean notExistsToken(Predicate<Token> predicate) {
            return !tokens
                    .stream()
                    .anyMatch(o -> o instanceof Token && predicate.test(o));
        }

        @Override
        public void insert(Object obj) {
            session.insert(obj);
            if (objectsCache != null) {
                objectsCache.add(obj);
            }
        }

        @Override
        public Logger getLogger() {
            if (logger == null) {
                logger = (Logger) session.getGlobal("logger");
            }

            return logger;
        }

        @Override
        public void insertToken(String type, long id) {
            tokens.add(new Token(type, id));
        }

        @Override
        public void insertDebugToken(String missedClassesAggregation) {
        }

        @Override
        public void insertDebugToken(String missedClassesAggregation, long id) {
        }

        @Override
        public DataAccess getDataAccess() {
            if (dataAccess == null) {
                dataAccess = findDataAccess();
            }

            return dataAccess;
        }

        private DataAccess findDataAccess() {
            return (DataAccess)this.getObjectsStream()
                    .filter(o -> o instanceof  DataAccess)
                    .findAny()
                    .orElse(null);
        }

        @Override
        public ReportStore getReportStore() {
            if (reportStore == null) {
                reportStore = (ReportStore)session.getGlobal("reports");
            }

            return reportStore;
        }
    }
}
