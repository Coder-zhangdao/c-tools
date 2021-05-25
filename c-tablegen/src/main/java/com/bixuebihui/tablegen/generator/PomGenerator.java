package com.bixuebihui.tablegen.generator;

import java.io.IOException;

public class PomGenerator extends BaseGenerator implements Generator {


    @Override
    public String getTargetFileName(String tableName) {
        return "pom.xml";
    }

    @Override
    String getTemplateFileName() {
        return "pom.xml";
    }

    @Override
    String getClassName(String tableName) {
        return null;
    }

    @Override
    public String generate(String tableName) throws IOException {
        return "pom.xml";
    }
}
