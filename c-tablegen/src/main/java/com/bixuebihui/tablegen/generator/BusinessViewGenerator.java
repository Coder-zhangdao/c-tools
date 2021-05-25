package com.bixuebihui.tablegen.generator;

import java.io.File;

public class BusinessViewGenerator extends BusinessGenerator implements Generator {
    public BusinessViewGenerator() {
        super.isView = true;
    }

    public String getTargetFileName(String tableName) {
        return getTargetFileName(VIEW_DIR+ File.separator+"business", tableName);
    }

}
