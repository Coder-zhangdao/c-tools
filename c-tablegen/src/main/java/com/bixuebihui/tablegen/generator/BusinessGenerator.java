package com.bixuebihui.tablegen.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author xwx
 */
public class BusinessGenerator extends BaseGenerator {
    protected final static String CLASS_SUFFIX = "Manager";

    private static final Logger LOG = LoggerFactory.getLogger(BusinessGenerator.class);


    String getClassSuffix(){return CLASS_SUFFIX;}

    @Override
    String getTemplateFileName() {
        return "business.java";
    }

    @Override
    public String getTargetFileName(String tableName) {
        return getTargetFileName("business", tableName);
    }

    @Override
    String getClassName(String tableName) {
        return getPojoClassName(tableName) + CLASS_SUFFIX;
    }


    @Override
    protected Map<String, Object> getContextMap(String tableName) {
        Map<String, Object> v = super.getContextMap(tableName);
        v.put("firstKeyType", this.getFirstKeyType(tableName));
        return v;
    }
}
