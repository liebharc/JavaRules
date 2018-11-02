package com.github.liebharc.JavaRules;

import org.drools.core.RuleBaseConfiguration;
import org.kie.api.conf.EventProcessingOption;
import org.kie.internal.conf.IndexPrecedenceOption;

import java.lang.reflect.Field;

public class RuleBaseConfigurationProvider {
    public static RuleBaseConfiguration createRuleBaseConfiguration(boolean stateful) {
        RuleBaseConfiguration configuration = new RuleBaseConfiguration();
       if (!stateful) {
            configuration.setSequential(true);
        }
        configuration.setSequentialAgenda(RuleBaseConfiguration.SequentialAgenda.DYNAMIC);
        return configuration;
    }
}
