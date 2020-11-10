package com.ssll;

import com.bixuebihui.tablegen.TableGen;
import org.apache.maven.plugin.AbstractMojo;

import java.io.File;
import java.sql.SQLException;


/**
 * @author xwx
 * @goal gen
 */
public class TableGenMojo extends AbstractMojo {
    String tableFilter;
    String genPath;
    String packageName;

    @Override
    public void execute() {
        TableGen gen = new TableGen();
        getLog().info("Starting gen tables...");

        String propPath = System.getProperty("user.dir") + File.separator + "tablegen.prop";

        getLog().info("propPath " + propPath);
        try {
            gen.run(propPath);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

