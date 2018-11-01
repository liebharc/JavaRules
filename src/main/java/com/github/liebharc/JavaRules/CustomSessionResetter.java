package com.github.liebharc.JavaRules;

import org.drools.core.common.InternalAgenda;
import org.drools.core.common.InternalWorkingMemoryEntryPoint;
import org.drools.core.common.NodeMemories;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.StatefulKnowledgeSessionImpl;
import org.drools.core.spi.FactHandleFactory;
import org.drools.core.spi.GlobalResolver;
import org.drools.core.time.TimerService;
import org.kie.api.event.kiebase.KieBaseEventListener;
import org.kie.internal.runtime.StatefulKnowledgeSession;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Custom session reset method based on reflection. Only to be used to experiment with the session reset without
 * having to create a new snapshot Drools build.
 */
public class CustomSessionResetter {

    private final Field nodeMemoriesField;
    private final Field agendaField;
    private final Field globalResolverField;
    private final Field kieBaseEventListenersField;
    private final Field handleFactoryField;
    private final Field propagationIdCounterField;
    private final Field opCounterField;
    private final Field lastIdleTimestampField;
    private final Field defaultEntryPointField;
    private final Field timerServiceField;
    private final Field processRuntimeField;
    private final Field initialFactHandleField;
    private final Field aliveField;
    private InternalKnowledgeBase kieBase;

    public CustomSessionResetter(InternalKnowledgeBase kieBase) {
        try {
            this.kieBase = kieBase;
            nodeMemoriesField = this.getField("nodeMemories");
            agendaField = this.getField("agenda");
            globalResolverField = this.getField("globalResolver");
            kieBaseEventListenersField = this.getField("kieBaseEventListeners");
            handleFactoryField = this.getField("handleFactory");
            propagationIdCounterField = this.getField("propagationIdCounter");
            opCounterField = this.getField("opCounter");
            lastIdleTimestampField = this.getField("lastIdleTimestamp");
            defaultEntryPointField = this.getField("defaultEntryPoint");
            timerServiceField = this.getField("timerService");
            processRuntimeField = this.getField("processRuntime");
            initialFactHandleField = this.getField("initialFactHandle");
            aliveField = this.getField("alive");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public void reset(StatefulKnowledgeSessionImpl session) {
        try {
            NodeMemories memories = this.getValue(session, nodeMemoriesField);
            if (memories != null ) {
                memories.resetAllMemories(session);
            }

            InternalAgenda agenda = this.getValue(session, agendaField);
            //agenda.reset();

            GlobalResolver globalResolver = this.getValue(session, globalResolverField);
            //globalResolver.clear();

            List<KieBaseEventListener> kieBaseEventListeners = this.getValue(session, kieBaseEventListenersField);
           // kieBaseEventListeners.clear();

            FactHandleFactory handleFactory = this.getValue(session, handleFactoryField);
            //handleFactory.clear( 0, 0 );

            AtomicLong propagationIdCounter = this.getValue(session, propagationIdCounterField);
            //propagationIdCounter.set(0);

            AtomicLong opCounter = this.getValue(session, opCounterField);
            //opCounter.set(0);

            AtomicLong lastIdleTimestamp = this.getValue(session, lastIdleTimestampField);
            //lastIdleTimestamp.set(0);

            InternalWorkingMemoryEntryPoint defaultEntryPoint = this.getValue(session, defaultEntryPointField);
            defaultEntryPoint.reset();

            //session.updateEntryPointsCache();

            TimerService timerService = this.getValue(session, timerServiceField);
           // timerService.reset();

           // processRuntimeField.set(session, null);

            //initialFactHandleField.set(session, session.initInitialFact(kieBase, null));

            aliveField.set(session, true);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private <T> T getValue(StatefulKnowledgeSession session, Field field) throws NoSuchFieldException, IllegalAccessException {
        return (T)field.get(session);
    }

    private Field getField(String name) throws NoSuchFieldException {
        Field field = StatefulKnowledgeSessionImpl.class.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }
}
