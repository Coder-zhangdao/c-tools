package com.bixuebihui.tablegen.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author xwx
 */
public class DalViewGenerator extends DalGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(DalViewGenerator.class);

    public DalViewGenerator() {
        super.isView = true;
    }

    @Override
    public String getTargetFileName(String tableName) {
        return getTargetFileName(VIEW_DIR+ File.separator+"dal", tableName);
    }


}
