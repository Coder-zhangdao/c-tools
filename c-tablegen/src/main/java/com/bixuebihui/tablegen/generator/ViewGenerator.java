package com.bixuebihui.tablegen.generator;

import java.io.File;

public class ViewGenerator extends PojoGenerator{
    @Override
    public String getTargetFileName(String tableName) {
        return  getTargetFileName("view", tableName);
    }



}
