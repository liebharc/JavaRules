package com.github.liebharc.JavaRules;


import com.github.liebharc.JavaRules.model.ReportStore;
import com.github.liebharc.JavaRules.sharedknowledge.DataAccess;
import com.github.liebharc.JavaRules.verbs.Verb;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessContext;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class RuleBase {

    public final void process(ProcessContext processContext) {
        final KieSession kieSession = (StatefulKnowledgeSession) processContext.getKieRuntime();
        final Session session = new Session(kieSession);
        process(session);
    }

    protected abstract void process(Session session);

    protected static final class Session {
        private KieSession session;

        private DataAccess dataAccess = null;

        private List<Token> tokens = new ArrayList<>();

        private Session(KieSession session) {
            this.session = session;
        }

        public<T> List<T> findAll(Class<T> clazz) {
            return (List<T> )session.getObjects().stream().filter(o -> clazz.isInstance(o)).collect(Collectors.toList());
        }

        public<T> List<T> findAll(Class<T> clazz, Predicate<? super T> predicate) {
            return (List<T> )session
                    .getObjects()
                    .stream()
                    .filter(o -> clazz.isInstance(o) && predicate.test((T)o))
                    .collect(Collectors.toList());
        }

        public boolean notExistsToken(Predicate<Token> predicate) {
            return tokens
                    .stream()
                    .anyMatch(o -> o instanceof Token && predicate.test(o));
        }

        public void insert(Object obj) {
            session.insert(obj);
        }

        public Logger getLogger() {
            return (Logger)session.getGlobal("logger");
        }

        public void insertToken(String type, long id) {
            tokens.add(new Token(type, id));
        }

        public void insertDebugToken(String missedClassesAggregation) {
        }

        public void insertDebugToken(String missedClassesAggregation, long id) {
        }

        public DataAccess getDataAccess() {
            if (dataAccess == null) {
                dataAccess = findDataAccess();
            }

            return dataAccess;
        }

        private DataAccess findDataAccess() {
            for (Object o : session.getObjects()) {
                if (o instanceof  DataAccess) {
                    return (DataAccess)o;
                }
            }

            return null;
        }

        public ReportStore getReportStore() {
            return (ReportStore)session.getGlobal("reports");
        }
    }
}
