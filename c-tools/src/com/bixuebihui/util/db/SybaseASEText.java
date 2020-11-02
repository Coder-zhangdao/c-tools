// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:59
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   SybaseASEText.java

package com.bixuebihui.util.db;

//import com.sybase.jdbc2.jdbc.SybResultSet;

public class SybaseASEText {

    public SybaseASEText() {
    }
/*
    public static String getClob(ResultSet p_rsData, boolean p_bJdbcIs2, int p_nFieldIndex)
        throws CMyException
    {
        return ((SybResultSet)p_rsData).getString(p_nFieldIndex);
        SQLException ex;

        throw new CMyException(40, "\u4ECE\u5F53\u524D\u8BB0\u5F55\u96C6\u4E2D\u8BFB\u53D6CLOB\u5B57\u6BB5\u65F6\u51FA\u9519(SybaseASEText.getClob)", ex);
        Exception ex;

        throw new CMyException(0, "\u4ECE\u5F53\u524D\u8BB0\u5F55\u96C6\u4E2D\u8BFB\u53D6CLOB\u5B57\u6BB5\u65F6\u51FA\u9519(SybaseASEText.getClob)", ex);
    }

    public static String getClob(ResultSet p_rsData, boolean p_bJdbcIs2, String p_sFieldName)
        throws CMyException
    {
        return ((SybResultSet)p_rsData).getString(p_sFieldName);
        SQLException ex;
        ex;
        throw new CMyException(40, "\u4ECE\u5F53\u524D\u8BB0\u5F55\u96C6\u4E2D\u8BFB\u53D6CLOB\u5B57\u6BB5\u65F6\u51FA\u9519(SybaseASEText.getClob)", ex);
        Exception ex;
        ex;
        throw new CMyException(0, "\u4ECE\u5F53\u524D\u8BB0\u5F55\u96C6\u4E2D\u8BFB\u53D6CLOB\u5B57\u6BB5\u65F6\u51FA\u9519(SybaseASEText.getClob)", ex);
    }

    public static boolean setClob(Connection p_oConn, String p_sValue, String p_sUpdateSQL, String p_sFieldName)
        throws CMyException
    {
        Exception exception;
        if(p_sValue == null)
            return true;
        boolean flag;
        try
        {
            PreparedStatement pstmt = p_oConn.prepareStatement(p_sUpdateSQL);
            ByteArrayInputStream baiStream = new ByteArrayInputStream(p_sValue.getBytes("GBK"));
            pstmt.setAsciiStream(1, baiStream, baiStream.available());
            pstmt.executeUpdate();
            pstmt.close();
            flag = true;
        }
        catch(SQLException ex)
        {
            throw new CMyException(40, "\u5199\u5165CLOB\u5B57\u6BB5\u65F6\u51FA\u9519(SybaseASEText.setClob)", ex);
        }
        catch(Exception ex)
        {
            throw new CMyException(0, "\u5199\u5165CLOB\u5B57\u6BB5\u65F6\u51FA\u9519(SybaseASEText.setClob)", ex);
        }
        finally
        {
            Object _tmp = JVM INSTR jsr 108;
        }
        return flag;
        throw exception;
    }

    public static boolean setClob(Connection p_oConn, String p_sTableName, String p_sWhere, String p_sIdFieldName, String p_sClobFieldName, String p_sValue)
        throws CMyException
    {
        if(p_sClobFieldName.length() < 1 || p_sTableName.length() < 1 || p_sWhere.length() < 1 || p_sIdFieldName.length() < 1)
        {
            throw new CMyException(10, "\u53C2\u6570\u65E0\u6548(SybaseASEText.setClob)");
        } else
        {
            String strSQL = "UPDATE " + p_sTableName + " SET " + p_sClobFieldName + "=? WHERE " + p_sWhere;
            return setClob(p_oConn, p_sValue, strSQL, p_sClobFieldName);
        }
    }

    public static boolean setClob(Connection p_oConn, String p_sTableName, String p_sWhere, String p_sIdFieldName, String p_asFieldsAndValues[])
        throws CMyException
    {
        Exception exception;
        int nClobCount = p_asFieldsAndValues.length / 2;
        boolean blFound = false;
        if(nClobCount <= 0 || nClobCount * 2 != p_asFieldsAndValues.length)
            throw new CMyException(10, "CLOB\u5B57\u6BB5\u540D\u548C\u5B57\u6BB5\u503C\u6570\u7EC4\u53C2\u6570\u65E0\u6548(SybaseASEText.setClob)");
        String strSQL = "UPDATE " + p_sTableName + " SET " + p_asFieldsAndValues[0] + "=?";
        for(int i = 1; i < nClobCount; i++)
            strSQL = strSQL + "," + p_asFieldsAndValues[i * 2] + "=?";

        strSQL = strSQL + " WHERE " + p_sWhere;
        boolean flag;
        try
        {
            PreparedStatement pstmt = p_oConn.prepareStatement(strSQL);
            for(int i = 0; i < nClobCount; i++)
            {
                String sValue = p_asFieldsAndValues[i * 2 + 1];
                if(sValue == null)
                    sValue = "";
                ByteArrayInputStream baiStream = new ByteArrayInputStream(sValue.getBytes("GBK"));
                pstmt.setAsciiStream(i + 1, baiStream, baiStream.available());
            }

            pstmt.executeUpdate();
            pstmt.close();
            flag = true;
        }
        catch(SQLException ex)
        {
            throw new CMyException(40, "\u5199\u5165CLOB\u5B57\u6BB5\u65F6\u51FA\u9519(SybaseASEText.setClob)", ex);
        }
        catch(Exception ex)
        {
            throw new CMyException(0, "\u5199\u5165CLOB\u5B57\u6BB5\u65F6\u51FA\u95192(SybaseASEText.setClob)", ex);
        }
        finally
        {
            Object _tmp = JVM INSTR jsr 297;
        }
        return flag;
        throw exception;
    }
    */
}
