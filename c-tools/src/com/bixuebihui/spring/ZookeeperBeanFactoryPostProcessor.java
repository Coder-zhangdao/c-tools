package com.bixuebihui.spring;

import com.bixuebihui.util.Config;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

public class ZookeeperBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    private String path = Constants.DEFAULT_ZOOKEEPER_PROPERTY_PATH;

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Properties p = Config.getZooProperties(path);
        PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
        cfg.setProperties(p);
        cfg.postProcessBeanFactory(beanFactory);
    }

}
