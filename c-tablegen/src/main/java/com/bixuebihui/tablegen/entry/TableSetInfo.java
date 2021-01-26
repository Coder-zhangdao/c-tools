package com.bixuebihui.tablegen.entry;

import com.bixuebihui.algorithm.LRULinkedHashMap;
import com.bixuebihui.generated.tablegen.pojo.T_metacolumn;
import com.bixuebihui.generated.tablegen.pojo.T_metatable;
import com.bixuebihui.tablegen.ForeignKeyDefinition;
import com.bixuebihui.tablegen.ProjectConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xwx
 */
public class TableSetInfo {
    private static final Log LOG = LogFactory.getLog(TableSetInfo.class);


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



}
