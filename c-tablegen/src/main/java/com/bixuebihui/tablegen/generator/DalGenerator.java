package com.bixuebihui.tablegen.generator;

import com.bixuebihui.tablegen.GenException;
import com.bixuebihui.tablegen.TableGen;
import com.bixuebihui.tablegen.entry.ColumnData;
import com.github.jknack.handlebars.Handlebars;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;

import static com.bixuebihui.tablegen.NameUtils.columnNameToConstantName;
import static com.bixuebihui.tablegen.NameUtils.firstUp;
import static com.bixuebihui.tablegen.TableGen.INDENT;

/**
 * @author xwx
 */
public class DalGenerator extends BaseGenerator {
    public final static String UNKNOWN_TYPE = "unknown";
    private final static String CLASS_SUFFIX = "List";

    private static final Log LOG = LogFactory.getLog(DalGenerator.class);

    public static boolean isNotEmpty(Collection<?> col) {
        return !CollectionUtils.isEmpty(col);
    }

    /**
     * Selects the type of a particular column name. Cannot use Hashtables to
     * store columns as it screws up the ordering, so we have to do a crap
     * search. (and yes I know it could be better - it's good enough).
     */
    public static String getColType(String key, List<ColumnData> columnData) {
        String type = UNKNOWN_TYPE;
        for (ColumnData tmp : columnData) {
            if (tmp.getName().equalsIgnoreCase(key)) {
                type = tmp.getJavaType();
                break;
            }
        }
        if (UNKNOWN_TYPE.equals(type)) {
            LOG.error("unknown type of key:" + key);
        }
        return type;
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

    public static boolean containsVersion(List<ColumnData> cols, String versionColName) {
        for (ColumnData col : cols) {
            if (col.getName().equalsIgnoreCase(versionColName)) {
                return true;
            }
        }
        return false;
    }

    public static String makeInsertObjects(boolean useAutoincrement, List<ColumnData> columnData, String versionColName) {
        return makeInsertObjects(useAutoincrement, false, columnData, versionColName);
    }

    @Override
    String getTemplateFileName() {
        return "dal.java";
    }

    private static String makeInsertObjects(boolean useAutoincrement, boolean skipVersionColumn, List<ColumnData> columnData, String versionColName) {
        StringBuilder objs = new StringBuilder();
        boolean useVersion = skipVersionColumn && containsVersion(columnData, versionColName);
        Iterator<ColumnData> iterator = columnData.iterator();
        while (iterator.hasNext()) {
            ColumnData cd = iterator.next();
            if (useAutoincrement && cd.isAutoIncrement()
                    || (useVersion && cd.getName().equalsIgnoreCase(versionColName))) {
                continue;
            }

            objs.append("info.get").append(firstUp(cd.getName())).append("()");

            if (iterator.hasNext()) {
                objs.append(",");
            }
        }
        if (objs.lastIndexOf(",") == objs.length() - 1) {
            objs.deleteCharAt(objs.length() - 1);
        }
        return objs.toString();
    }

    public static String makeUpdateObjects(List<String> params, List<ColumnData> columnData, boolean use_autoincrement, String versionColName) {
        return "" + makeInsertObjects(use_autoincrement, true, columnData, versionColName) + "," +
                createKeyObjects(params);
    }

    public static String createKeyObjects(List<String> params) {
        StringBuilder objs = new StringBuilder();
        if (params != null) {
            String key;
            for (Iterator<String> e = params.iterator(); e.hasNext(); ) {
                key = e.next();

                objs.append("info.get").append(firstUp(key)).append("()");

                if (e.hasNext()) {
                    objs.append(" , ");
                }
            }

        } else {
            objs = null;
        }

        return objs == null ? null : objs.toString();
    }

    /**
     * Writes out the mapRow method. The mapRow method interprets the returned
     * result set from a Select and update the object with those values.
     */
    public static String mapRow(String tableName, List<ColumnData> columnData, String pojoClassName) {
        String col;
        String colType;
        List<String> gets = new ArrayList<>();
        String get;

        for (ColumnData cd : columnData) {
            col = cd.getName();
            colType = cd.getJavaType();

            // work out which data type we are getting for each variable
            String ucol = "F." + columnNameToConstantName(col);

            if (colType.compareTo("String") == 0) {
                get = "(r.getString(" + ucol + "));";
            } else if (colType.compareTo("byte") == 0 || colType.compareTo("Byte") == 0) {
                get = "(r.getByte(" + ucol + "));";
            } else if (colType.compareTo("long") == 0 || colType.compareTo("Long") == 0) {
                get = "(r.getLong(" + ucol + "));";
            } else if (colType.compareTo("int") == 0 || colType.compareTo("Integer") == 0) {
                get = "(r.getInt(" + ucol + "));";
            } else if (colType.compareTo("short") == 0 || colType.compareTo("Short") == 0) {
                get = "(r.getShort(" + ucol + "));";
            } else if (colType.compareTo("float") == 0 || colType.compareTo("Float") == 0) {
                get = "(r.getFloat(" + ucol + "));";
            } else if (colType.compareTo("double") == 0 || colType.compareTo("Double") == 0) {
                get = "(r.getDouble(" + ucol + "));";
            } else if (colType.compareTo("Date") == 0) {
                get = "(r.getDate(" + ucol + "));";
            } else if (colType.compareTo("Timestamp") == 0) {
                get = "(r.getTimestamp(" + ucol + "));";
            } else if (colType.compareTo("Time") == 0) {
                get = "(r.getTime(" + ucol + "));";
            } else if (colType.compareTo("char") == 0 || colType.compareTo("Char") == 0) {
                get = "(r.getString(" + ucol + ").charAt(0);";
            } else if (colType.compareTo("byte[]") == 0) {
                get = "(r.getBytes(" + ucol + "));";
            } else if (colType.compareTo("Boolean") == 0) {
                get = "(r.getBoolean(" + ucol + "));";
            } else if (colType.compareTo("Byte[]") == 0) {
                get = "(org.apache.commons.lang.ArrayUtils.toObject(r.getBytes(" + ucol + ")));";
            } else if (colType.compareTo("com.bixuebihui.jdbc.ClobString") == 0) {
                get = "(new com.bixuebihui.jdbc.ClobString(com.bixuebihui.jdbc.JDBCUtils.oracleClob2Str((Clob)r.getObject("
                        + ucol + "))));";
            } else {
                get = "(r.getObject(" + ucol + "));";
                LOG.error("Warning! Unknown type : " + colType + " in write mapRow");
            }

            get = firstUp(cd.getName()) + get;
            gets.add(get);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(INDENT).append("/**\n")
                .append(INDENT).append("  * Updates the object from a selected ResultSet.\n")
                .append(INDENT).append("  */\n")
                .append(INDENT).append("@Override\n")
                .append(INDENT).append("public " + pojoClassName + " mapRow (ResultSet r, int index) throws SQLException\n")
                .append(INDENT).append("{\n")
                .append(INDENT + pojoClassName + " res = new " + pojoClassName + "();\n");

        for (String e : gets) {
            sb.append(INDENT).append(INDENT).append("res.set" + e);
        }

        sb.append(INDENT).append(INDENT).append("return res;\n");
        sb.append(INDENT).append("}\n");
        return sb.toString();
    }

    @Override
    protected Map<String, Object> getContextMap(String tableName) {
        Map<String, Object> v = super.getContextMap(tableName);

        v.put("hasKey", isNotEmpty(this.setInfo.getTableKeys(tableName)));
        v.put("hasVersionCol", containsVersion(this.setInfo.getTableCols(tableName), config.getVersionColName()));
        v.put("firstKeyType", this.getFirstKeyType(tableName));
        v.put("firstKeyName", this.getFirstKeyName(tableName));
        v.put("whereNoLike", TableGen.createPreparedWhereClause(this.setInfo.getTableKeys(tableName),
                false, this.setInfo.getTableCols(tableName)));
        v.put("insertObjects", makeInsertObjects(config.isUse_autoincrement(), setInfo.getTableCols(tableName), config.getVersionColName()));
        v.put("updateObjects", makeUpdateObjects(setInfo.getTableKeys(tableName), setInfo.getTableCols(tableName), config.isUse_autoincrement(), config.getVersionColName()));
        v.put("keyObjects", createKeyObjects(setInfo.getTableKeys(tableName)));
        v.put("mapRow", mapRow(tableName, setInfo.getTableCols(tableName), this.getPojoClassName(tableName)));
        return v;
    }

    @Override
    String getTargetFileName(String tableName) {
        return
                config.getBaseSrcDir() + File.separator + "dal" + File.separator + getPojoClassName(tableName)
                        + CLASS_SUFFIX + ".java";
    }

    @Override
    String getClassName(String tableName) {
        return getPojoClassName(tableName) + CLASS_SUFFIX;
    }

    @Override
    protected void additionalSetting(Handlebars handlebars) {
        super.additionalSetting(handlebars);
        // usage: {{type tableName colName}}
        handlebars.registerHelper("type", (tableName, options) -> getColType(options.param(0), this.setInfo.getTableCols((String)tableName)));
        // usage: {{where tableName}}
        handlebars.registerHelper("where", (tableName, options) ->
        {
            boolean like = options.param(0);
            return TableGen.createPreparedWhereClause(this.setInfo.getTableKeys((String) tableName), like, this.setInfo.getTableCols((String) tableName));
        });

    }

    String getFirstKeyType(String tableName) {
        try {
            List<String> keyData = setInfo.getTableKeys(tableName);
            List<ColumnData> columnData = setInfo.getTableInfos().get(tableName).getFields();
            return getFirstKeyType(keyData, columnData);
        } catch (GenException e) {
            e.printStackTrace();
        }
        return "";
    }

    String getFirstKeyName(String tableName) {
        List<String> keyData = setInfo.getTableKeys(tableName);
        return getFirstKeyName(keyData);
    }

}
