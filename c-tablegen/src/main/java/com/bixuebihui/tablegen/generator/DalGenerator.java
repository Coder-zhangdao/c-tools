package com.bixuebihui.tablegen.generator;

import com.bixuebihui.tablegen.GenException;
import com.bixuebihui.tablegen.ProjectConfig;
import com.bixuebihui.tablegen.TableUtils;
import com.bixuebihui.tablegen.entry.ColumnData;
import com.github.jknack.handlebars.Handlebars;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * @author xwx
 */
public class DalGenerator extends BaseGenerator{
    private static final Log LOG = LogFactory.getLog(DalGenerator.class);

    public static boolean isNotEmpty(Collection<?> col) {
        return !CollectionUtils.isEmpty(col);
    }

    public static String getFirstKeyType(List<String> keyData2, List<ColumnData> columnData) throws GenException {
        return (isNotEmpty(keyData2)) ? getColType(keyData2.get(0), columnData) : "Object";
    }

    public static String getFirstKeyName(List<String> keyData2) {
        if (isNotEmpty(keyData2)) {
            return keyData2.get(0);
        }
        return null;
    }

    /**
     * Selects the type of a particular column name. Cannot use Hashtables to
     * store columns as it screws up the ordering, so we have to do a crap
     * search. (and yes I know it could be better - it's good enough).
     */
    public static String getColType(String key, List<ColumnData> columnData) throws GenException {
        String type = "unknown";
        for (ColumnData tmp : columnData) {
            if (tmp.getName().equalsIgnoreCase(key)) {
                type = tmp.getJavaType();
                break;
            }
        }
        if ("unknown".equals(type)) {
            LOG.error("unknown type of key:" + key);
            throw new GenException("error unknown type of key:" + key);
        }

        return type;
    }

    @Override
    String getTargetFileName(String tableName) {
        return
                config.getBaseSrcDir()+ File.separator + "dal" + File.separator + getPojoClassName(tableName)
                + "List.java";
    }

    @Override
    String getTemplateFileName() {
        return "dal.java";
    }

    @Override
    String getClassName(String tableName) {
        return getPojoClassName(tableName) + "List";
    }

    @Override
    protected void additionalSetting(Handlebars handlebars){
        super.additionalSetting(handlebars);
        // usage: {{firstKeyType tableName}}
        handlebars.registerHelper("firstKeyType", (tableName, options) -> this.getFirstKeyType((String) tableName));

        // usage: {{firstKeyName tableName}}
        handlebars.registerHelper("firstKeyName", (tableName, options) -> this.getFirstKeyName((String) tableName));

    }

    String getFirstKeyType(String tableName)  {
        try {
            List<String>  keyData = setInfo.getTableKeys(tableName);
            List<ColumnData> columnData = setInfo.getTableInfos().get(tableName).getFields();
            return getFirstKeyType(keyData, columnData);
        } catch (SQLException | GenException e) {
            e.printStackTrace();
        }
        return "";
    }

    String getFirstKeyName(String tableName)  {
        try {
            List<String>  keyData = setInfo.getTableKeys(tableName);
            return getFirstKeyName(keyData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

}
