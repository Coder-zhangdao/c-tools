// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:58
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   DB2UDB.java

package com.bixuebihui.util.db;

import com.bixuebihui.util.other.CMyException;
import com.bixuebihui.util.other.CMyString;

import java.sql.Connection;

// Referenced classes of package com.bixuebihui.util.database:
//            DBType, DataType

public class DB2UDB extends DBType {

    public DB2UDB() {
        super("DB2 Universal Database", "COM.ibm.db2.jdbc.app.DB2Driver", false);
    }

    public DB2UDB(String _sName, String _sDriverClass, boolean _bSupportStoredProc) {
        super(_sName, _sDriverClass, false);
    }

    public String sqlFilterForClob(String _sFieldName, String _sValue) {
        return "";
    }

    public String sqlConcatStr(String _strSQL1, String _strSQL2) {
        return "CONCAT(" + _strSQL1 + "," + _strSQL2 + ")";
    }

    public String sqlAddField(String _sTableName, String _sFieldName, String _sFieldType, int _nMaxLength, boolean _bNullable, String _sDefaultValue, int _nScale) {
        String strSQL = "ALTER TABLE " + _sTableName + " ADD " + _sFieldName + " " + _sFieldType;
        DataType dataType = getDataType(_sFieldType);
        if (dataType == null)
            return null;
        if (dataType.isLengthDefinedByUser())
            if (_nScale > 0)
                strSQL = strSQL + "(" + _nMaxLength + ", " + _nScale + ")";
            else
                strSQL = strSQL + "(" + _nMaxLength + ")";
        if (!_bNullable) {
            strSQL = strSQL + " NOT NULL ";
            if (_sDefaultValue != null) {
                strSQL = strSQL + " DEFAULT ";
                if (dataType.isCharData())
                    strSQL = strSQL + "'" + CMyString.filterForSQL(_sDefaultValue) + "'";
                else
                    strSQL = strSQL + _sDefaultValue;
            }
        }
        return strSQL;
    }

    public String sqlConcatStr(String _strSQLs[]) {
        String sRet = "CONCAT(" + _strSQLs[0] + "," + _strSQLs[1] + ")";
        for (int i = 2; i < _strSQLs.length; i++)
            sRet = "CONCAT(" + sRet + "," + _strSQLs[i] + ")";

        return sRet;
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

    public String sqlFilterOneDay(String _sFieldName, String _sDateTime, String _sFormat) {
        return "DAY(DATE(" + _sFieldName + ")-DATE('" + _sDateTime + "'))=0";
    }

    public String sqlDateTime(String _sDateTime, String _sFormat) {
        if (_sFormat.equalsIgnoreCase("YYYY-MM-DD"))
            return "TIMESTAMP('" + _sDateTime + " 00:00:00')";
        else
            return "TIMESTAMP('" + _sDateTime + "')";
    }

    public String sqlDate(String _sDateTime) {
        return "DATE('" + _sDateTime + "')";
    }

    public String sqlDateField(String _sDateField) {
        return "DATE(" + _sDateField + ")";
    }

    public String sqlConcatStr(String _strSQL1, String _strSQL2, String _strSQL3) {
        return "CONCAT(CONCAT(" + _strSQL1 + "," + _strSQL2 + ")," + _strSQL3 + ")";
    }

    public String sqlGetSysDate() {
        return "(CURRENT TIMESTAMP)";
    }

    public String sqlDropField(String _sTableName, String _sFieldNames)
            throws Exception {
        throw new Exception("DB2 Universal Database 7.2\u7248\u672C\u4E0D\u652F\u6301\u5B57\u6BB5\u5220\u9664\uFF01");
    }

    public String encodeStrToWrite(String _strSrc) {
        return _strSrc;
    }

    public static void main(String args[]) {
        new DB2UDB();
    }

    public String initQuerySQL(String _strSql, int _nStartIndex, int _nSize) {
        return _strSql;
    }

    public static final DataType SMALLINT;
    public static final DataType INT;
    public static final DataType BIGINT;
    public static final DataType DECIMAL;
    public static final DataType REAL;
    public static final DataType FLOAT;
    public static final DataType CHAR;
    public static final DataType VARCHAR;
    public static final DataType CLOB;
    public static final DataType DBCLOB;
    public static final DataType BLOB;
    public static final DataType DATE;
    public static final DataType TIME;
    public static final DataType TIMESTAMP;
    public static final DataType UNIQUEIDENTIFIER = new DataType("UNIQUEIDENTIFIER", -5, 16);
    public static final DataType SQL_VARIRANT = new DataType("SQL_VARIRANT", -3);
    static final DataType m_allDataTypes[];
    static final DataType m_supportedDataTypes[];

    static {
        SMALLINT = new DataType("SMALLINT", 5, 2);
        INT = new DataType("INTEGER", 4, 4);
        BIGINT = new DataType("BIGINT", -5, 8);
        DECIMAL = new DataType("DECIMAL", 3, 9);
        REAL = new DataType("REAL", 7, 4);
        FLOAT = new DataType("FLOAT", 6, 8);
        CHAR = new DataType("CHAR", 1, -254);
        VARCHAR = new DataType("VARCHAR", 12, -32672);
        CLOB = new DataType("CLOB", -1, 16);
        DBCLOB = new DataType("DBCLOB", -1, 16);
        BLOB = new DataType("BLOB", -4, 16);
        DATE = new DataType("DATE", 93, 8);
        TIME = new DataType("TIME", 93, 8);
        TIMESTAMP = new DataType("TIMESTAMP", 93, 8);
        m_allDataTypes = (new DataType[]{
                SMALLINT, INT, BIGINT, DECIMAL, REAL, FLOAT, CHAR, VARCHAR, CLOB, DBCLOB,
                BLOB, DATE, TIME, TIMESTAMP
        });
        m_supportedDataTypes = (new DataType[]{
                SMALLINT, INT, DECIMAL, VARCHAR, CLOB, TIMESTAMP
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
