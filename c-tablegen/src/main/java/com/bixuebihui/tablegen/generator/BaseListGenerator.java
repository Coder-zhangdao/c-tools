package com.bixuebihui.tablegen.generator;

import java.io.File;

/**
 * @author xwx
 */
public class BaseListGenerator extends BaseGenerator{


    @Override
    public String getFileName(String tableName) {
        String baseDir = config.getBaseSrcDir();

        String fileName = baseDir + File.separator + "BaseList.java";
        return fileName;
    }

    @Override
    public String getClassName(String tableName) {
        return "BaseList";
    }
}
