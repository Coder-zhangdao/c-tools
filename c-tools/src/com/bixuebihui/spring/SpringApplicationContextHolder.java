package com.bixuebihui.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

public class SpringApplicationContextHolder implements ApplicationContextAware {
    private static final Log log = LogFactory.getLog(SpringApplicationContextHolder.class);

    private ApplicationContext ctx;
    private String path = Constants.DEFAULT_ZOOKEEPER_PROPERTY_PATH;

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ctx = context;
        MutablePropertySources sources = ((ConfigurableEnvironment) ctx.getEnvironment()).getPropertySources();
        sources.addFirst(new ZookeeperPropertySource(path));
        log.info(path + " set!");

    }

    public ApplicationContext getApplicationContext() {
        return ctx;
    }


}
