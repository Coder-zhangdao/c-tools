package com.bixuebihui.tablegen.generator;

import com.bixuebihui.tablegen.GenException;
import com.bixuebihui.tablegen.TableGen;
import com.bixuebihui.tablegen.entry.ColumnData;
import com.github.jknack.handlebars.Handlebars;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.bixuebihui.tablegen.NameUtils.columnNameToConstantName;
import static com.bixuebihui.tablegen.NameUtils.firstUp;
import static com.bixuebihui.tablegen.TableGen.INDENT;
import static com.bixuebihui.tablegen.generator.ViewGenerator.VIEW_DIR;

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
