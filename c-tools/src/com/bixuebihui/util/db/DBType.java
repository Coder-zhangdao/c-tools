// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:58
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   DBType.java

package com.bixuebihui.util.db;

import com.bixuebihui.util.other.CMyException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Hashtable;


// Referenced classes of package com.bixuebihui.util.database:
//            DataType

public abstract class DBType {

    public DBType(String _sName, String _sDriverClass, boolean _bSupportStoredProc) {
        sName = _sName;
        sDriverClass = _sDriverClass;
        bSupportStoredProc = _bSupportStoredProc;
        DataType allDataTypes[] = getAllDataTypes();
        hDataTypes = new Hashtable(allDataTypes.length);
        for (int i = 0; i < allDataTypes.length; i++)
            hDataTypes.put(allDataTypes[i].getName(), allDataTypes[i]);

    }

    public String getName() {
        return sName;
    }

    public String getDriverClass() {
        return sDriverClass;
    }

    public DBType setDriverClass(String _sDriverClass) {
        sDriverClass = _sDriverClass;
        return this;
    }

    public boolean isSupportStoredProc() {
        return bSupportStoredProc;
    }

    public abstract String encodeStrToWrite(String s);

    public abstract boolean canWriteTextDirectly();

    public abstract DataType[] getAllDataTypes();

    public abstract DataType[] getSupportedDataTypes();

    public DataType getDataType(String _name) {
        if (_name == null) {
            return null;
        } else {
            DataType dataType = (DataType) hDataTypes.get(_name.toUpperCase());
            return dataType != null ? dataType : OTHER;
        }
    }

    public abstract String sqlConcatStr(String s, String s1);

    public abstract String sqlConcatStr(String s, String s1, String s2);

    public abstract String sqlConcatStr(String as[]);

    public abstract String sqlFilterForClob(String s, String s1);

    public String sqlAddField(String _sTableName, String _sFieldName, String _sFieldType, int _nMaxLength, boolean _bNullable) {
        return sqlAddField(_sTableName, _sFieldName, _sFieldType, _nMaxLength, _bNullable, null, 0);
    }

    public abstract String sqlAddField(String s, String s1, String s2, int i, boolean flag, String s3, int j);

    public abstract String sqlDropField(String s, String s1)
            throws Exception;

    public abstract String sqlGetSysDate();

    public abstract String sqlFilterOneDay(String s, String s1, String s2);

    public abstract String sqlDateTime(String s, String s1);

    public abstract String sqlDate(String s);

    public abstract String sqlDateField(String s);

    public abstract String initQuerySQL(String s, int i, int j);

    private String sName;
    private String sDriverClass;
    private boolean bSupportStoredProc;
    private Hashtable hDataTypes;
    public static final int MAX_PAGE_SIZE = 9999;
    public static final DataType OTHER = new DataType("", 1111);

    public void setStringFieldValue(PreparedStatement _oPreStmt, int _nIndex, String _sValue)
            throws Exception {
        _oPreStmt.setString(_nIndex, _sValue);
    }

    public abstract boolean setClob(Connection connection, String s, String s1, String s2, String s3, String s4)
            throws CMyException;

    public abstract boolean setClob(Connection connection, String s, String s1, String s2, String as[])
            throws CMyException;

}
