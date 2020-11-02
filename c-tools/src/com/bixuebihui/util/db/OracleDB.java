// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:58
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   OracleDB.java

package com.bixuebihui.util.db;

import com.bixuebihui.util.other.CMyException;
import com.bixuebihui.util.other.CMyString;

import java.sql.Connection;

// Referenced classes of package com.bixuebihui.util.database:
//            DBType, DataType

public class OracleDB extends DBType {

    public OracleDB() {
        super("Oracle", "oracle.jdbc.driver.OracleDriver", true);
    }

    public OracleDB(String _sName, String _sDriverClass) {
        super(_sName, _sDriverClass, true);
    }

    public String encodeStrToWrite(String _strSrc) {
        return _strSrc;
    }

    public boolean canWriteTextDirectly() {
        return false;
    }

    public DataType[] getAllDataTypes() {
        return m_allDataTypes;
    }

    public DataType[] getSupportedDataTypes() {
        return m_supportedDataTypes;
    }

    public String sqlConcatStr(String _strSQL1, String _strSQL2) {
        return "CONCAT(" + _strSQL1 + "," + _strSQL2 + ")";
    }

    public String sqlConcatStr(String _strSQL1, String _strSQL2, String _strSQL3) {
        return "CONCAT(CONCAT(" + _strSQL1 + "," + _strSQL2 + ")," + _strSQL3 + ")";
    }

    public String sqlConcatStr(String _strSQLs[]) {
        String sRet = "CONCAT(" + _strSQLs[0] + "," + _strSQLs[1] + ")";
        for (int i = 2; i < _strSQLs.length; i++)
            sRet = "CONCAT(" + sRet + "," + _strSQLs[i] + ")";

        return sRet;
    }

    public String sqlFilterForClob(String _sFieldName, String _sValue) {
        return " dbms_lob.instr(" + _sFieldName + ",'" + CMyString.filterForSQL(_sValue) + "',1,1)>0 ";
    }

    public String sqlAddField(String _sTableName, String _sFieldName, String _sFieldType, int _nMaxLength, boolean _bNullable, String _sDefaultValue, int _nScale) {
        String strSQL = "ALTER TABLE " + _sTableName + " ADD( " + _sFieldName + " " + _sFieldType;
        DataType dataType = getDataType(_sFieldType);
        if (dataType == null)
            return null;
        if (dataType.isLengthDefinedByUser())
            if (_nScale > 0)
                strSQL = strSQL + "(" + _nMaxLength + ", " + _nScale + ")";
            else
                strSQL = strSQL + "(" + _nMaxLength + ")";
        if (_bNullable) {
            strSQL = strSQL + " NULL";
        } else {
            if (_sDefaultValue != null) {
                strSQL = strSQL + " DEFAULT ";
                if (dataType.isCharData())
                    strSQL = strSQL + "'" + CMyString.filterForSQL(_sDefaultValue) + "'";
                else
                    strSQL = strSQL + _sDefaultValue;
            }
            strSQL = strSQL + " NOT NULL ";
        }
        return strSQL + ")";
    }

    public String sqlDropField(String _sTableName, String _sFieldNames)
            throws Exception {
        return "ALTER TABLE " + _sTableName + " DROP( " + _sFieldNames + " )";
    }

    public String sqlGetSysDate() {
        return "SYSDATE";
    }

    public String sqlFilterOneDay(String _sFieldName, String _sDateTime, String _sFormat) {
        return _sFieldName + " like to_date('" + _sDateTime + "','" + _sFormat + "')";
    }

    public String sqlDateTime(String _sDateTime, String _sFormat) {
        return "to_date('" + _sDateTime + "','" + _sFormat + "')";
    }

    public String sqlDate(String _sDateTime) {
        return sqlDateTime(_sDateTime, "yyyy-MM-dd HH24:MI:SS");
    }

    public String sqlDateField(String _sDateField) {
        return _sDateField;
    }

    public String initQuerySQL(String _strSql, int _nStartIndex, int _nSize) {
        StringBuffer querySQL = new StringBuffer();
        // OracleDB _tmp = this;
        if (_nSize != 9999)
            querySQL.append("select * from (select my_table.*,rownum as my_rownum from(").append(_strSql).append(") my_table where rownum<").append(_nStartIndex + _nSize).append(") where my_rownum>=").append(_nStartIndex);
        else
            querySQL.append("select * from (select my_table.*,rownum as my_rownum from(").append(_strSql).append(") my_table ").append(") where my_rownum>=").append(_nStartIndex);
        return querySQL.toString();
    }

    public static final DataType CHAR;
    public static final DataType VARCHAR2;
    public static final DataType NUMBER;
    public static final DataType LONG;
    public static final DataType ROWID;
    public static final DataType DATE;
    public static final DataType RAW;
    public static final DataType LONGRAW;
    public static final DataType CLOB;
    public static final DataType BLOB;
    public static final DataType NCLOB;
    public static final DataType BFILE;
    private static final DataType m_allDataTypes[];
    static final DataType m_supportedDataTypes[];

    static {
        CHAR = new DataType("CHAR", 1, -4000);
        VARCHAR2 = new DataType("VARCHAR2", 12, -4000);
        NUMBER = new DataType("NUMBER", 2, -38);
        LONG = new DataType("LONG", -1);
        ROWID = new DataType("ROWID", -2);
        DATE = new DataType("DATE", 91);
        RAW = new DataType("RAW", -3);
        LONGRAW = new DataType("LONGRAW", -4);
        CLOB = new DataType("CLOB", 2005);
        BLOB = new DataType("BLOB", 2004);
        NCLOB = new DataType("NCLOB", 2005);
        BFILE = new DataType("BFILE", -4);
        m_allDataTypes = (new DataType[]{
                CHAR, VARCHAR2, NUMBER, LONG, ROWID, DATE, RAW, LONGRAW, CLOB, BLOB,
                NCLOB, BFILE
        });
        m_supportedDataTypes = (new DataType[]{
                NUMBER, VARCHAR2, CLOB, DATE
        });
    }

    public boolean setClob(Connection connection, String s, String s1, String s2, String s3, String s4) throws CMyException {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean setClob(Connection connection, String s, String s1, String s2, String[] as) throws CMyException {
        // TODO Auto-generated method stub
        return false;
    }
}
