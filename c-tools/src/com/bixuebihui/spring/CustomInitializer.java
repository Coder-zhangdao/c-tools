package com.bixuebihui.spring;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class CustomInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final String path = Constants.DEFAULT_ZOOKEEPER_PROPERTY_PATH;

    public void initialize(ConfigurableApplicationContext ctx) {
        ZookeeperPropertySource zkPropertySource = new ZookeeperPropertySource(path);
        ctx.getEnvironment().getPropertySources().addFirst(zkPropertySource);
    }
}
