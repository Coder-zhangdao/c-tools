// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:59
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   TableInfo.java

package com.bixuebihui.util.db;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author xwx
 */
public class TableInfo {

    private Map<String, FieldInfo> hFieldInfos;

    public String getTableName() {
        return sTableName;
    }

    public TableInfo() {
        sTableName = null;
        hFieldInfos = null;
        hFieldInfos = new ConcurrentHashMap<>();
    }

    public void clear() {
        hFieldInfos.clear();
    }

    public void setTableName(String tableName) {
        sTableName = tableName;
    }

    public void putFieldInfo(String sFieldName, FieldInfo fieldInfo) {
        hFieldInfos.put(sFieldName, fieldInfo);
    }


    private String sTableName;

    public FieldInfo getFieldInfo(String sFieldName) {
        if (sFieldName == null) {
            return null;
        } else {
            return hFieldInfos.get(sFieldName.trim().toUpperCase());
        }
    }
}
