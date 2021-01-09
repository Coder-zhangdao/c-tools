// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:58
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   SQLServerDB.java

package com.bixuebihui.util.db;

import com.bixuebihui.util.other.CMyException;
import com.bixuebihui.util.other.CMyString;

import java.sql.Connection;

// Referenced classes of package com.bixuebihui.util.database:
//            DBType, DataType

public class SQLServerDB extends DBType {

    public SQLServerDB() {
        super("SQLServer", "com.microsoft.jdbc.sqlserver.SQLServerDriver", true);
    }

    public SQLServerDB(String _sName, String _sDriverClass) {
        super(_sName, _sDriverClass, true);
    }

    @Override
    public String encodeStrToWrite(String _strSrc) {
        try {
            if (_strSrc == null) {
                return null;
            }
            return new String(_strSrc.getBytes(), WRITE_STR_ENCODING);
        } catch (Exception ex) {

        }
        return _strSrc;
    }

    @Override
    public boolean canWriteTextDirectly() {
        return true;
    }

    @Override
    public DataType[] getAllDataTypes() {
        return m_allDataTypes;
    }

    @Override
    public DataType[] getSupportedDataTypes() {
        return m_supportedDataTypes;
    }

    @Override
    public String sqlConcatStr(String _strSQL1, String _strSQL2) {
        return _strSQL1 + "+" + _strSQL2;
    }

    @Override
    public String sqlConcatStr(String _strSQL1, String _strSQL2, String _strSQL3) {
        return _strSQL1 + "+" + _strSQL2 + "+" + _strSQL3;
    }

    @Override
    public String sqlConcatStr(String _strSQLs[]) {
        String sRet = _strSQLs[0];
        for (int i = 1; i < _strSQLs.length; i++) {
            sRet = sRet + "+" + _strSQLs[i];
        }

        return sRet;
    }

    @Override
    public String sqlFilterForClob(String _sFieldName, String _sValue) {
        return " patindex('%" + CMyString.filterForSQL(_sValue) + "%'," + _sFieldName + ")>0 ";
    }

    @Override
    public String sqlAddField(String _sTableName, String _sFieldName, String _sFieldType, int _nMaxLength, boolean _bNullable, String _sDefaultValue, int _nScale) {
        String strSQL = "ALTER TABLE " + _sTableName + " ADD " + _sFieldName + " " + _sFieldType;
        DataType dataType = getDataType(_sFieldType);
        if (dataType == null) {
            return null;
        }
        if (dataType.isLengthDefinedByUser()) {
            if (_nScale > 0) {
                strSQL = strSQL + "(" + _nMaxLength + ", " + _nScale + ")";
            } else {
                strSQL = strSQL + "(" + _nMaxLength + ")";
            }
        }
        if (_bNullable) {
            strSQL = strSQL + " NULL";
        } else {
            if (_sDefaultValue != null) {
                strSQL = strSQL + " DEFAULT ";
                if (dataType.isCharData()) {
                    strSQL = strSQL + "'" + CMyString.filterForSQL(_sDefaultValue) + "'";
                } else {
                    strSQL = strSQL + _sDefaultValue;
                }
            }
            strSQL = strSQL + " NOT NULL ";
        }
        return strSQL;
    }

    @Override
    public String sqlDropField(String _sTableName, String _sFieldNames)
            throws Exception {
        return "ALTER TABLE " + _sTableName + " DROP COLUMN " + _sFieldNames;
    }

    @Override
    public String sqlGetSysDate() {
        return "GETDATE()";
    }

    @Override
    public String sqlFilterOneDay(String _sFieldName, String _sDateTime, String _sFormat) {
        return "DateDiff(day," + _sFieldName + ",'" + _sDateTime + "')=0";
    }

    @Override
    public String sqlDateTime(String _sDateTime, String _sFormat) {
        return "'" + _sDateTime + "'";
    }

    @Override
    public String sqlDate(String _sDateTime) {
        return "'" + _sDateTime + "'";
    }

    @Override
    public String sqlDateField(String _sDateField) {
        return _sDateField;
    }

    @Override
    public String initQuerySQL(String _strSql, int _nStartIndex, int _nSize) {
        return _strSql;
    }

    public static String WRITE_STR_ENCODING = "ISO-8859-1";
    public static final DataType BIT;
    public static final DataType TINYINT;
    public static final DataType SMALLINT;
    public static final DataType INT;
    public static final DataType BIGINT;
    public static final DataType NUMERIC;
    public static final DataType REAL;
    public static final DataType FLOAT;
    public static final DataType DECIMAL;
    public static final DataType MONEY;
    public static final DataType SMALLMONEY;
    public static final DataType BINARY;
    public static final DataType VARBINARY;
    public static final DataType IMAGE;
    public static final DataType CHAR;
    public static final DataType NCHAR;
    public static final DataType VARCHAR;
    public static final DataType NVARCHAR;
    public static final DataType TEXT;
    public static final DataType NTEXT;
    public static final DataType TIMESTAMP;
    public static final DataType DATETIME;
    public static final DataType SMALLDATETIME;
    public static final DataType UNIQUEIDENTIFIER;
    public static final DataType SQL_VARIRANT;
    static final DataType m_allDataTypes[];
    static final DataType m_supportedDataTypes[];

    static {
        BIT = new DataType("BIT", -7, 1);
        TINYINT = new DataType("TINYINT", -6, 1);
        SMALLINT = new DataType("SMALLINT", 5, 2);
        INT = new DataType("INT", 4, 4);
        BIGINT = new DataType("BIGINT", -5, 8);
        NUMERIC = new DataType("NUMERIC", 2, 9);
        REAL = new DataType("REAL", 7, 4);
        FLOAT = new DataType("FLOAT", 6, 8);
        DECIMAL = new DataType("DECIMAL", 3, 9);
        MONEY = new DataType("MONEY", 3, 8);
        SMALLMONEY = new DataType("SMALLMONEY", 3, 4);
        BINARY = new DataType("BINARY", -2);
        VARBINARY = new DataType("VARBINARY", -3);
        IMAGE = new DataType("IMAGE", -4, 16);
        CHAR = new DataType("CHAR", 1, -8000);
        NCHAR = new DataType("NCHAR", 1, -4000);
        VARCHAR = new DataType("VARCHAR", 12, -8000);
        NVARCHAR = new DataType("NVARCHAR", 12, -4000);
        TEXT = new DataType("TEXT", -1, 16);
        NTEXT = new DataType("NTEXT", -1, 16);
        TIMESTAMP = new DataType("TIMESTAMP", 93, 8);
        DATETIME = new DataType("DATETIME", 93, 8);
        SMALLDATETIME = new DataType("SMALLDATETIME", 93, 4);
        UNIQUEIDENTIFIER = new DataType("UNIQUEIDENTIFIER", -5, 16);
        SQL_VARIRANT = new DataType("SQL_VARIRANT", -3);
        m_allDataTypes = (new DataType[]{
                BIGINT, BINARY, BIT, CHAR, DATETIME, DECIMAL, FLOAT, IMAGE, INT, MONEY,
                NCHAR, NTEXT, NUMERIC, NVARCHAR, REAL, SMALLDATETIME, SMALLINT, SMALLMONEY, SQL_VARIRANT, TEXT,
                TIMESTAMP, TINYINT, UNIQUEIDENTIFIER, VARBINARY, VARCHAR
        });
        m_supportedDataTypes = (new DataType[]{
                SMALLINT, TINYINT, INT, NUMERIC, NVARCHAR, NTEXT, DATETIME, FLOAT, NUMERIC
        });
    }

    @Override
    public boolean setClob(Connection connection, String s, String s1, String s2, String s3, String s4) throws CMyException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean setClob(Connection connection, String s, String s1, String s2, String[] as) throws CMyException {
        // TODO Auto-generated method stub
        return false;
    }
}
