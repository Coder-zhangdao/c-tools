package com.bixuebihui.tablegen.entry;

import com.bixuebihui.algorithm.LRULinkedHashMap;
import com.bixuebihui.generated.tablegen.business.T_metatableManager;
import com.bixuebihui.generated.tablegen.pojo.T_metacolumn;
import com.bixuebihui.generated.tablegen.pojo.T_metatable;
import com.bixuebihui.jdbc.BaseDao;
import com.bixuebihui.jdbc.IDbHelper;
import com.bixuebihui.jdbc.JDBCUtils;
import com.bixuebihui.jdbc.SqlFileExecutor;
import com.bixuebihui.tablegen.ForeignKeyDefinition;
import com.bixuebihui.tablegen.PojoPropertiesParser;
import com.bixuebihui.tablegen.ProjectConfig;
import com.bixuebihui.tablegen.TableUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * @author xwx
 */
public class TableSetInfo {
    private static final Log LOG = LogFactory.getLog(TableSetInfo.class);
    public static final String DB_ERROR = "[GEN]Error setup database";


    private final Map<String, List<ForeignKeyDefinition>> foreignKeyImCache = new LRULinkedHashMap<>(
            400);
    private final Map<String, List<String>> keyCache = new LRULinkedHashMap<>(500);
    private final Map<String, List<String>> indexCache = new LRULinkedHashMap<>(200);
    private final Map<String, List<ForeignKeyDefinition>> foreignKeyExCache = new LRULinkedHashMap<>(
            400);
    private  Map<String, T_metatable> tableDataExt;
    private LinkedHashMap<String, TableInfo> tableInfos;

    public Map<String, T_metatable> getTableDataExt() {
        return tableDataExt;
    }

    public void setTableDataExt(Map<String, T_metatable> tableDataExt) {
        this.tableDataExt = tableDataExt;
    }

    public Map<String, List<ForeignKeyDefinition>> getForeignKeyImCache() {
        return foreignKeyImCache;
    }

    public Map<String, List<String>> getKeyCache() {
        return keyCache;
    }

    public Map<String, List<String>> getIndexCache() {
        return indexCache;
    }

    public Map<String, List<ForeignKeyDefinition>> getForeignKeyExCache() {
        return foreignKeyExCache;
    }

    public LinkedHashMap<String, TableInfo> getTableInfos() {
        return tableInfos;
    }

    public void setTableInfos(LinkedHashMap<String, TableInfo> tableInfos) {
        this.tableInfos = tableInfos;
    }

    public List<ColumnData> getTableCols(String tableName){
       return this.getTableInfos().get(tableName).getFields();
    }


    public String tableName2ClassName(String tableName) {
        if (tableDataExt != null && tableDataExt.get(tableName) != null) {
            String alias = tableDataExt.get(tableName).getClassname();

            LOG.debug("Pojo class alias is: " + alias);

            if (StringUtils.isNotEmpty(alias)) {
                return alias.trim();
            }
        }
        return tableName;
    }

    private T_metatable getTableDetail(String tableName, ProjectConfig config) {
        T_metatable res;
        if (tableDataExt != null) {
            res = tableDataExt.get(tableName);
        } else {
            res = new T_metatable();
            boolean any = false;
            if (config.getPojo_node_interface_list().contains(tableName)) {
                res.setIsnode(true);
                any = true;
            }
            if (config.getPojo_state_interface_list().contains(tableName)) {
                res.setIsstate(true);
                any = true;
            }
            if (config.getPojo_version_interface_list().contains(tableName)) {
                res.setIsversion(true);
                any = true;
            }
            if (config.getPojo_modifydate_interface_list().contains(tableName)) {
                res.setIsmodifydate(true);
                any = true;
            }
            if (config.getPojo_uuid_interface_list().contains(tableName)) {
                res.setIsuuid(true);
                any = true;
            }
            if (!any) {
                res = null;
            }
        }
        return res;
    }


    public String getInterface(String tableName, ProjectConfig config) {
        StringBuilder sb = new StringBuilder();
        sb.append(" implements Serializable");

        T_metatable tab = getTableDetail(tableName, config);
        if (tab != null) {
            if (tab.getIsnode()) {
                sb.append(", ").append(config.getPojo_node_interface());
            }
            if (tab.getIsstate()) {
                sb.append(", ").append(config.getPojo_state_interface());
            }
            if (tab.getIsversion()) {
                sb.append(", ").append(config.getPojo_version_interface());
            }
            if (tab.getIsmodifydate()) {
                sb.append(", ").append(config.getPojo_modifydate_interface());
            }
            if (tab.getIsuuid()) {
                sb.append(", ").append(config.getPojo_uuid_interface());
            }
            if (StringUtils.isNotBlank(tab.getExtrainterfaces())) {
                sb.append(", ").append(tab.getExtrainterfaces());
            }
        }
        return sb.toString();
    }

    public Map<String, T_metacolumn> getColumnsExtInfo(String tableName){
        if (getTableDataExt() != null && getTableDataExt().get(tableName) != null) {
            return getTableDataExt().get(tableName).getColumns();
        }
        return Collections.emptyMap();
    }


    /**
     * Retrieves all the table data required.
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
    public synchronized void getTableData(ProjectConfig config, IDbHelper dbHelper, @NotNull DatabaseMetaData metaData) throws SQLException, InstantiationException, IllegalAccessException, IOException {

        LinkedHashMap<String, TableInfo> col = getTableInfos();

        try(Connection cn = dbHelper.getConnection()) {
            if (CollectionUtils.isEmpty(col)) {

                //表名
                List<String> tableNames =
                        TableUtils.getTableData(metaData, config.getCatalog(), config.getSchema(),
                                config.getTableOwner(), config.getTablesList(), config.getExcludeTablesList());
                LinkedHashMap<String, TableInfo> tables = new LinkedHashMap<>();
                for (String name : tableNames) {
                    TableInfo table = new TableInfo(name);

                    // Fill columns info
                    TableInfo newInfo = TableUtils.getColumnData(metaData, config.getCatalog(), config.getSchema(), name);
                    table.setFields(newInfo.getFields());
                    tables.put(name, table);

                    // Primary keys info
                    List<String> keyData = TableUtils.getTableKeys(metaData, config.getCatalog(), config.getSchema(), name);
                    getKeyCache().put(name, keyData);

                    // foreign keys import info
                    initImportKeys(config, dbHelper, metaData, name);

                    // foreign keys export info
                    initExportKeys(config, dbHelper, metaData, name);

                    // index info
                    initIndex(config, dbHelper, metaData, name);

                    // table & column's  comment
                    if(isMysql(metaData)) {
                        fillComment(cn, table);
                    }

                }


                setTableInfos(tables);
            } else {
                LOG.info("tableNames already set, retrieve from db skipped.");
            }

            // Custom meta info from db table
            if (config.isUseCustomMetaTable()) {
                setTableDataExt(getTableDataExt(dbHelper, getTableInfos()));
            }

            // Custom meta info from xml
            if (StringUtils.isNotEmpty(config.getExtra_setting())) {
                setTableDataExt(getExtraTableDataFromXml(
                        config.getBaseDir() + config.getExtra_setting(), getTableDataExt()));
            }
        }

    }



    public Map<String, T_metatable> getTableDataExt(IDbHelper dbHelper, LinkedHashMap<String, TableInfo> tables)
            throws SQLException {
        HashMap<String, T_metatable> ht = new HashMap<>();
        T_metatableManager daoMetaTable = new T_metatableManager();
        daoMetaTable.setDbHelper(dbHelper);

        setupMetatable(dbHelper, daoMetaTable);
        List<String> names = new ArrayList<>(32);
        for(TableInfo table: tables.values()){
            names.add(table.getName());
        }


        Collection<T_metatable> c = daoMetaTable.getTableDataExt(names);
        for (T_metatable t : c) {
            ht.put(t.getTname(), t);
        }
        return ht;
    }


    public boolean setupMetatable(IDbHelper dbHelper, T_metatableManager daoMetaTable) {
        Connection conn = null;
        boolean res = false;
        try {
            LOG.info("[CYC]数据库安装...");
            conn = dbHelper.getConnection();
            if (!JDBCUtils.tableOrViewExists(null, null, daoMetaTable.getTableName(), conn)) {
                SqlFileExecutor ex = new SqlFileExecutor();
                String filename;

                if (daoMetaTable.getDbType() == BaseDao.POSTGRESQL) {
                    filename = "postgresql";
                } else {
                    filename = "mysql";
                }

                DefaultResourceLoader loader = new DefaultResourceLoader();
                String beanConfigFile = "classpath:dbscript/ext." + filename + ".sql";
                LOG.info(loader.getResource(beanConfigFile).getFilename());
                ex.execute(conn, loader.getResource(beanConfigFile).getInputStream());

                res = initTableData(getTableInfos(), daoMetaTable);
            }
            if (res) {
                LOG.info("[CYC]数据库已成功安装");
            } else {
                LOG.info(DB_ERROR);
            }
        } catch (Exception e) {
            LOG.info(DB_ERROR, e);
        } finally {
            JDBCUtils.close(conn);
        }
        return false;
    }


    public boolean insertOrUpdateMetatable(T_metatable metatable, T_metatableManager daoMetaTable) throws SQLException {
        if (metatable.getTid() <= 0) {
            return daoMetaTable.insertAutoNewKey(metatable);
        } else {
            return daoMetaTable.updateByKey(metatable);
        }
    }


    public boolean initTableData(LinkedHashMap<String, TableInfo> tableNames,  T_metatableManager daoMetaTable) throws SQLException {
        if (tableNames != null) {
            T_metatable[] infos = new T_metatable[tableNames.size()];
            int i = 0;
            for (TableInfo tableInfo : tableNames.values()) {
                T_metatable t = new T_metatable();
                t.setTname(tableInfo.getName());
                t.setDescription(tableInfo.getComment());
                t.setIsnode(false);
                t.setIsstate(false);
                t.setIsversion(false);
                t.setIsmodifydate(false);
                t.setIsuuid(false);
                t.setTid(daoMetaTable.getNextKey());

                infos[i] = t;
                i++;
            }
            return daoMetaTable.insertBatch(infos, null);
        } else {
            return false;
        }
    }



    public Map<String, T_metatable> getExtraTableDataFromXml(String extraSettingFileName,
                                                                Map<String, T_metatable> settingFromDb) throws IOException {
        File file = new File(extraSettingFileName);
        if (file.exists()) {
            LOG.info("Find extra setting file: " + extraSettingFileName);
            if (settingFromDb == null) {
                settingFromDb = new HashMap<>();
            }
            String xml = FileUtils.readFileToString(file, "UTF-8");

            Map<String, T_metatable> res = PojoPropertiesParser.parse(xml);

            return PojoPropertiesParser.mergeTableSetting(settingFromDb, res);
        } else {
            LOG.info("WARNING: File for value extra_setting not exists:" + extraSettingFileName);
            return settingFromDb;
        }
    }


    private void initIndex(ProjectConfig config, IDbHelper dbHelper, DatabaseMetaData metaData, String tableName) {
        // use a hashmap to temporarily store the indexes as well
        // so we can avoid duplicate values.
        HashMap<String, String> checkIndexes = new HashMap<>();
        String index;
        short indexType;
        List<String> indexData;
        indexData = new ArrayList<>();

        try( ResultSet r = metaData.getIndexInfo(config.getCatalog(), config.getSchema(), tableName, false, false) ) {
            while (r.next()) {
                indexType = r.getShort(7);
                index = r.getString(9);
                if (indexType != DatabaseMetaData.tableIndexStatistic &&
                        // ensure that it is not a duplicate value.
                        checkIndexes.get(index) == null) {
                    indexData.add(index);
                    checkIndexes.put(index, index);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getIndexCache().put(tableName, indexData);
    }

    private boolean isMysql(DatabaseMetaData metaData) throws SQLException {
        return metaData.getDriverName().toLowerCase().contains("mysql");
    }

    private void initExportKeys(ProjectConfig config, IDbHelper dbHelper, DatabaseMetaData metaData, String tableName) throws SQLException {
        if(isMysql(metaData)){
            //针对mysql的优化, 一次加载全部外键
            if (getForeignKeyExCache().isEmpty()) {
                getForeignKeyExCache().putAll(TableUtils.getAllMySQLExportKeys(dbHelper,config.getTableOwner()));
            }
            if(getForeignKeyExCache().containsKey(tableName)) {
                LOG.info(tableName +" has  ExportKey in cache:"+ getForeignKeyExCache().get(tableName));
                return;
            }else{
                LOG.debug(tableName +" does not have a ExportKey in cache");
            }
            return;
        }
        List<ForeignKeyDefinition> foreignKeyData = TableUtils.getTableExportedKeys(metaData, config.getCatalog(), config.getTableOwner(), tableName);
        getForeignKeyExCache().put(tableName, foreignKeyData);
    }

    private  void initImportKeys(ProjectConfig config, IDbHelper dbHelper, DatabaseMetaData metaData, String tableName) throws SQLException {
        if(isMysql(metaData)){
            //针对mysql的优化, 一次加载全部外键
            if (getForeignKeyImCache().isEmpty()) {
                getForeignKeyImCache().putAll(TableUtils.getAllMySQLImportKeys(dbHelper,config.getTableOwner()));
            }
            if(getForeignKeyImCache().containsKey(tableName)) {
                LOG.info(tableName +" has  ImportKey in cache:"+ getForeignKeyImCache().get(tableName));
                return;
            }else{
                LOG.debug(tableName +"have not a ImportKey in cache");
                return;
            }
        }
        List<ForeignKeyDefinition> foreignKeyData = TableUtils.getTableImportedKeys(metaData, config.getCatalog(), config.getTableOwner(), tableName);
        getForeignKeyImCache().put(tableName, foreignKeyData);
    }



    /**
     * this below two methods are valid only for MySQL
     * @param conn
     * @param tableInfo
     * @return
     */
    private static void fillComment(Connection conn, TableInfo tableInfo ) {
        ResultSet showTableResultSet = null;
        Statement showTableStatement = null;
        String tableName =tableInfo.getName();
        try {
            showTableStatement = conn.createStatement();
            showTableResultSet = showTableStatement.executeQuery("show create table " + tableName);
            showTableResultSet.next();
            String createTableSql = showTableResultSet.getString(2);
            fillFieldComment(tableName, tableInfo.getFields(), createTableSql);
            String comment =  fillTableComment(createTableSql);
            tableInfo.setComment(comment);
        } catch (Exception e) {
            LOG.warn("fail to execute: show create table "+tableName);
            LOG.warn(e.getMessage(), e);
        } finally {
            try {
                if (showTableResultSet != null) {
                    showTableResultSet.close();
                }
                if (showTableStatement != null) {
                    showTableStatement.close();
                }
            } catch (Exception e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }


    private static  void fillFieldComment(String tableName, List<ColumnData> fields, String tableSql) {
        String fieldSql = tableSql.substring(tableSql.indexOf("(") + 1, tableSql.lastIndexOf(")"));
        String[] fieldDescs = org.apache.commons.lang3.StringUtils.split(fieldSql, "\n");
        Map<String, ColumnData> commentMap = new HashMap<>();
        for (ColumnData fieldInfo : fields) {
            commentMap.put(fieldInfo.getName().toUpperCase(), fieldInfo);
        }
        for (String fieldDesc : fieldDescs) {
            String trim = org.apache.commons.lang.StringUtils.trim(fieldDesc);
            String fieldName = org.apache.commons.lang.StringUtils.split(trim, " ")[0].toUpperCase();
            fieldName = replace(fieldName);
            String upper = fieldDesc.toUpperCase();
            if (upper.contains("AUTO_INCREMENT")) {
                if (Arrays.asList(org.apache.commons.lang.StringUtils.split(upper, " ")).contains("AUTO_INCREMENT")) {
                    commentMap.get(fieldName).setAutoIncrement(true);
                }
            }
            if (!fieldDesc.contains("COMMENT")) {
                continue;
            }
            String[] splits = org.apache.commons.lang.StringUtils.split(trim, "COMMENT");
            String comment = splits[splits.length - 1];
            comment = replace(comment);
            if (commentMap.containsKey(fieldName)) {
                commentMap.get(fieldName).setComment(comment);
            } else {
                LOG.info("table:"+tableName+",fileName:"+fieldDesc);
            }
        }
    }


    private static String fillTableComment(String tableSql) {
        String classCommentTmp = tableSql.substring(tableSql.lastIndexOf("COMMENT=") + 8).trim();
        classCommentTmp = replace(classCommentTmp);
        classCommentTmp = org.apache.commons.lang3.StringUtils.trim(classCommentTmp);
        return classCommentTmp;
    }

    private static String replace(String classCommentTmp) {
        classCommentTmp = org.apache.commons.lang3.StringUtils.split(classCommentTmp, " ")[0];
        classCommentTmp = org.apache.commons.lang3.StringUtils.replace(classCommentTmp, "'", "");
        classCommentTmp = org.apache.commons.lang3.StringUtils.replace(classCommentTmp, ";", "");
        classCommentTmp = org.apache.commons.lang3.StringUtils.replace(classCommentTmp, ",", "");
        classCommentTmp = org.apache.commons.lang3.StringUtils.replace(classCommentTmp, "`", "");
        classCommentTmp = org.apache.commons.lang3.StringUtils.replace(classCommentTmp, "\n", "");
        classCommentTmp = org.apache.commons.lang3.StringUtils.replace(classCommentTmp, "\t", "");
        classCommentTmp = org.apache.commons.lang3.StringUtils.trim(classCommentTmp);
        return classCommentTmp;
    }


}
