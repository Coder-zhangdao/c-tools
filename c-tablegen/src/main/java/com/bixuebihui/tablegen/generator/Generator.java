package com.bixuebihui.tablegen.generator;

import com.bixuebihui.dbcon.DatabaseConfig;
import com.bixuebihui.tablegen.ProjectConfig;
import com.bixuebihui.tablegen.entry.TableSetInfo;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public interface Generator {
    void init(ProjectConfig config, DatabaseConfig dbConfig, TableSetInfo setInfo);

    String generate(String tableName) throws IOException;

    String getTargetFileName(String tableName);

    default void generateToFile(String tableName) throws IOException {

        String fileName = getTargetFileName(tableName);
        File file = new File(fileName);
        if (file.getParentFile()!=null && !file.getParentFile().exists()) {
            boolean res = file.getParentFile().mkdirs();
            if (!res) {
                throw new IOException("fail to create directory");
            }
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName))) {
            IOUtils.write(generate(tableName), writer);
            System.out.println(tableName+": "+ fileName);
        }

    }
}
