// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:59
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   TableInfo.java

package com.bixuebihui.util.db;

import java.util.Enumeration;
import java.util.Hashtable;

// Referenced classes of package com.bixuebihui.util.database:
//            FieldInfo

public class TableInfo {

    public TableInfo() {
        sTableName = null;
        hFieldInfos = null;
        hFieldInfos = new Hashtable();
    }

    protected void finalize() {
        clear();
    }

    public String getTableName() {
        return sTableName;
    }

    public void setTableName(String _sTableName) {
        sTableName = _sTableName;
    }

    public void clear() {
        hFieldInfos.clear();
    }

    public void removeFieldInfo(String _sFieldName) {
        if (hFieldInfos.containsKey(_sFieldName))
            hFieldInfos.remove(_sFieldName);
    }

    public void putFieldInfo(String _sFieldName, FieldInfo _fieldInfo) {
        hFieldInfos.put(_sFieldName, _fieldInfo);
    }

    public FieldInfo getFieldInfo(String _sFieldName) {
        if (_sFieldName == null)
            return null;
        else
            return (FieldInfo) hFieldInfos.get(_sFieldName.trim().toUpperCase());
    }

    public boolean isField(String _sFieldName) {
        return null != getFieldInfo(_sFieldName);
    }

    public int getFieldCount() {
        return hFieldInfos.size();
    }

    public Enumeration getFieldNames() {
        return hFieldInfos.keys();
    }

    private String sTableName;
    private Hashtable hFieldInfos;
}
