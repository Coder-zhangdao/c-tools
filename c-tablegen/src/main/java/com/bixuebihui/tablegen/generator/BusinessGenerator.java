package com.bixuebihui.tablegen.generator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * @author xwx
 */
public class BusinessGenerator extends BaseGenerator {
    protected final static String CLASS_SUFFIX = "Business";

    private static final Log LOG = LogFactory.getLog(BusinessGenerator.class);


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
