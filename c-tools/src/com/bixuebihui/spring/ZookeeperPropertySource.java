package com.bixuebihui.spring;

import com.bixuebihui.util.Config;
import org.springframework.core.env.PropertySource;

import java.util.Properties;

public class ZookeeperPropertySource extends PropertySource<Properties> {

    public ZookeeperPropertySource(String path) {
        super(path, Config.getZooProperties(path));
    }

    @Override
    public Object getProperty(String key) {
        return this.source.getProperty(key);
    }

}
