// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:59
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   TableInfos.java

package com.bixuebihui.util.db;

import com.bixuebihui.util.other.CMyException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.Enumeration;
import java.util.Hashtable;


/**
 * @author xwx
 */
public class TableInfos {

    private String _sDBOwner = "JIAOGUANJU";


    public TableInfos() {
        hTableInfos = null;
        hTableInfos = new Hashtable();
    }


    public void clear() {
        hTableInfos.clear();
    }

    public int getTableCount() {
        return hTableInfos.size();
    }

    public Enumeration getTableNames() {
        return hTableInfos.keys();
    }

    public TableInfo getTableInfo(String _sTableName) {
        if (_sTableName == null)
            return null;
        else
            return (TableInfo) hTableInfos.get(_sTableName.trim());
    }

    public FieldInfo getFieldInfo(String _sTableName, String _sFieldName) {
        TableInfo tableInfo = getTableInfo(_sTableName);
        if (tableInfo == null) {
            return null;
        } else {
            return tableInfo.getFieldInfo(_sFieldName);
        }
    }

    public boolean isField(String _sTableName, String _sFieldName) {
        return null != getFieldInfo(_sTableName, _sFieldName);
    }

    public void load(Connection _oConn, int _dbType)
            throws CMyException {
        load(_oConn, _dbType, _sDBOwner);
    }

    public void load(Connection _oConn, int _dbType, String _sDBOwner)
            throws CMyException {
        hTableInfos.clear();
        String strSQL;
        if (_dbType == DBTypes.ORACLE) //oracle
            strSQL = "SELECT * FROM ALL_TAB_COLUMNS WHERE OWNER=? ORDER BY TABLE_NAME,COLUMN_ID";
        else if (_dbType == DBTypes.SQLSERVER)
            strSQL = "SELECT *, NUMERIC_SCALE AS DATA_SCALE FROM WCM_ViewCOLUMNS where 1=? ORDER BY TABLE_NAME, COLUMN_ID";
        else if (_dbType == DBTypes.DB2UDB)
            strSQL = "SELECT c.tbname AS TABLE_NAME,c.colno AS COLUMN_ID,c.name AS COLUMN_NAME,c.typename AS DATA_TYPE," +
                    "c.longlength AS DATA_LENGTH, c.nulls AS NULLABLE,c.default AS DATA_DEFAULT,c.scale AS DATA_SCALE" +
                    " FROM sysibm.syscolumns c where c.tbcreator=? ORDER BY TABLE_NAME, COLUMN_ID";
        else if (_dbType == DBTypes.SybaseASE)
            strSQL = "SELECT * FROM WCM_ViewCOLUMNS where 1=? ORDER BY TABLE_NAME, COLUMN_ID";
        else
            throw new CMyException(1, "不支持该类型(" + _dbType + ")数据库！");


        try (PreparedStatement stmt = _oConn.prepareStatement(strSQL)) {

             stmt.setObject(1, ((_dbType==DBTypes.ORACLE || _dbType==DBTypes.DB2UDB))? _sDBOwner:1);

            try(ResultSet rs = stmt.executeQuery()) {
                TableInfo tableInfo = null;
                DBType dbType = DBTypes.getDBType(_dbType);
                String sFieldName;
                FieldInfo fieldInfo;
                for (; rs.next(); tableInfo.putFieldInfo(sFieldName, fieldInfo)) {
                    String sTableName = rs.getString("TABLE_NAME");
                    int nColumnId = rs.getInt("COLUMN_ID");
                    sFieldName = rs.getString("COLUMN_NAME");
                    String sDataType = rs.getString("DATA_TYPE");
                    int nDataLength = rs.getInt("DATA_LENGTH");
                    String sNullable = rs.getString("NULLABLE");
                    boolean isNullable = false;
                    if (sNullable == null || sNullable.compareToIgnoreCase("Y") == 0)
                        isNullable = true;
                    String sDataDefault = rs.getString("DATA_DEFAULT");
                    int nScale = rs.getInt("DATA_SCALE");
                    fieldInfo = new FieldInfo(dbType, sDataType, nDataLength, isNullable, nColumnId, sDataDefault, nScale);
                    if (tableInfo == null || tableInfo.getTableName().compareTo(sTableName) != 0) {
                        tableInfo = new TableInfo();
                        tableInfo.setTableName(sTableName);
                        hTableInfos.put(sTableName, tableInfo);
                    }
                }
            }

        } catch (SQLException ex) {
            throw new CMyException(40, "\u4ECE\u6570\u636E\u5E93\u4E2D\u83B7\u53D6\u5B57\u6BB5\u4FE1\u606F\u65F6\u5931\u8D25\uFF08TableInfos.load\uFF09", ex);
        } catch (Exception ex) {
            throw new CMyException(0, "\u4ECE\u6570\u636E\u5E93\u4E2D\u83B7\u53D6\u5B57\u6BB5\u4FE1\u606F\u65F6\u5931\u8D25\uFF08TableInfos.load\uFF09", ex);
        }
    }

    public void toHtml(Connection _oConn, int _dbType, String _sFileName)
            throws CMyException {
        String strSQL = null;
        if (_dbType == 1) //oracle
            strSQL = "SELECT TABLE_NAME,COLUMN_ID,COLUMN_NAME,DATA_TYPE,DATA_LENGTH,NULLABLE,DATA_DEFAULT, DATA_SCALE FROM ALL_TAB_COLUMNS WHERE OWNER='" + _sDBOwner + "' ORDER BY TABLE_NAME,COLUMN_ID";
        else if (_dbType == 2)
            strSQL = "SELECT * FROM WCM_ViewCOLUMNS ORDER BY TABLE_NAME, COLUMN_ID";
        else if (_dbType == 3)
            strSQL = "SELECT c.tbname AS TABLE_NAME,c.colno AS COLUMN_ID,c.name AS COLUMN_NAME,c.typename AS DATA_TYPE,c.longlength AS DATA_LENGTH, c.nulls AS NULLABLE,c.default AS DATA_DEFAULT,c.scale AS DATA_SCALE FROM sysibm.syscolumns c where c.tbcreator='TRSWCM' ORDER BY TABLE_NAME, COLUMN_ID";
        else if (_dbType == 4)
            strSQL = "SELECT * FROM WCM_ViewCOLUMNS ORDER BY TABLE_NAME, COLUMN_ID";
        else
            throw new CMyException(1, "不支持该类型数据库！");
        DBType dbType = DBTypes.getDBType(_dbType);

        try (Statement stmt = _oConn.createStatement(); ResultSet rs = stmt.executeQuery(strSQL)) {


            String sLastTblName = null;
            StringBuilder sHtml = new StringBuilder("<html>");
            int i = 1;
            for (sHtml.append("\n<head>\n  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> \n" +
                    "  <link rel=\"stylesheet\" href=\"../style/zc2006.css\">\n</head><body>"); rs.next(); sHtml.append("\n</tr>")) {
                i++;
                String sTableName = rs.getString("TABLE_NAME");
                int nColumnId = rs.getInt("COLUMN_ID");
                String sFieldName = rs.getString("COLUMN_NAME");
                String sDataType = rs.getString("DATA_TYPE");
                int nDataLength = rs.getInt("DATA_LENGTH");
                boolean isNullable = rs.getString("NULLABLE").compareToIgnoreCase("Y") == 0;
                String sDataDefault = rs.getString("DATA_DEFAULT");
                int nDataScale = rs.getInt("DATA_SCALE");
                if (sLastTblName == null || sLastTblName.compareTo(sTableName) != 0) {
                    sLastTblName = sTableName;
                    if (sLastTblName != null)
                        sHtml.append("\n</table>\n</p><br>");
                    sHtml.append("\n<p><img border=\"0\" src=\"../images/blue-folder-open.gif\" align=\"absmiddle\"><b>" + i + ". " + sTableName + "</b>");
                    sHtml.append("\n<table border='0' cellspacing='2' cellpadding='2'>");
                    sHtml.append("\n<tr bgcolor=\"#dddddd\">");
                    sHtml.append("\n    <td align=\"center\" height=\"14\" nowrap>属性名称</td>");
                    sHtml.append("\n    <td align=\"center\" height=\"14\" nowrap>字段名称</td>");
                    sHtml.append("\n    <td align=\"center\" height=\"14\" nowrap>列标号</td>");
                    sHtml.append("\n    <td align=\"center\" height=\"14\" nowrap>数据类型</td>");
                    sHtml.append("\n    <td align=\"center\" height=\"14\" nowrap>长度</td>");
                    sHtml.append("\n    <td align=\"center\" height=\"14\" nowrap>允许为空</td>");
                    sHtml.append("\n    <td align=\"center\" height=\"14\" nowrap>默认值</td>");
                    sHtml.append("\n</tr>");
                }
                FieldInfo fi = new FieldInfo(dbType, sDataType, nDataLength, isNullable, nColumnId, sDataDefault, nDataScale);
                sHtml.append("\n<tr bgcolor=\"#eeeeee\">");
                sHtml.append("\n    <td nowrap>" + sFieldName + "</td>");
                sHtml.append("\n    <td nowrap>" + sFieldName + "</td>");
                sHtml.append("\n    <td nowrap>" + fi.getColumnID() + "</td>");
                sHtml.append("\n    <td nowrap>" + fi.getDataTypeName() + "</td>");
                sHtml.append("\n    <td nowrap>" + fi.getDataLength() + "</td>");
                if (fi.isNullable())
                    sHtml.append("\n    <td nowrap><font color=#0000ff>Yes</font></td>");
                else
                    sHtml.append("\n    <td nowrap><font color=#ff0000>No</font></td>");
                sHtml.append("\n    <td nowrap>" + fi.getDataDefault() + "</td>");
            }

            sHtml.append("\n<table/>\n</p><br>");
            sHtml.append("\n</body>\n</html>");
            FileUtils.write(new File(_sFileName), sHtml.toString(), Charset.defaultCharset());
        } catch (SQLException ex) {
            throw new CMyException(40, "从数据库中获取字段信息时失败（TableInfos.toHtml）", ex);
        } catch (Exception ex) {
            throw new CMyException(0, "从数据库中获取字段信息时失败（TableInfos.toHtml）", ex);
        }
    }

    public static void main(String args[]) throws ClassNotFoundException {
        Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
        try (Connection oConn = DriverManager.getConnection("jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=mydb", "mydb", "mydb")
        ) {
            TableInfos tableInfos = new TableInfos();
            tableInfos.toHtml(oConn, 2, "d:\\test.htm");
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    private Hashtable hTableInfos;

    public String get_sDBOwner() {
        return _sDBOwner;
    }

    public void set_sDBOwner(String owner) {
        _sDBOwner = owner;
    }

}
