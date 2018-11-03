package com.github.liebharc.JavaRules;

import org.drools.core.RuleBaseConfiguration;
import org.kie.api.conf.EventProcessingOption;
import org.kie.internal.conf.IndexPrecedenceOption;

import java.lang.reflect.Field;

public class RuleBaseConfigurationProvider {
    public static RuleBaseConfiguration createRuleBaseConfiguration(boolean sequential) {
        RuleBaseConfiguration configuration = new RuleBaseConfiguration();
        configuration.setSequential(sequential);
        configuration.setSequentialAgenda(RuleBaseConfiguration.SequentialAgenda.SEQUENTIAL);
        return configuration;
    }
}
