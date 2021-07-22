package com.bixuebihui.tablegen.generator;

import java.io.File;

/**
 * @author xwx
 */
public class BusinessViewGenerator extends BusinessGenerator implements Generator {
    public BusinessViewGenerator() {
        super.isView = true;
    }

    @Override
    public String getTargetFileName(String tableName) {
        return getTargetFileName(VIEW_DIR+ File.separator+"business", tableName);
    }

}
