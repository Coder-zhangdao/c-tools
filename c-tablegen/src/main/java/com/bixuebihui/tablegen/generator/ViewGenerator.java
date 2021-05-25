package com.bixuebihui.tablegen.generator;

import java.io.File;

public class ViewGenerator extends PojoGenerator{
    public ViewGenerator() {
        super.isView = true;
    }

    @Override
    public String getTargetFileName(String tableName) {
        return  getTargetFileName(VIEW_DIR+ File.separator+"pojo", tableName);
    }


}
