// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:57
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   CSQLText.java

package com.bixuebihui.util.db;

import com.bixuebihui.util.other.CMyException;

import java.io.Reader;
import java.sql.ResultSet;

public class CSQLText {

    public CSQLText() {
    }

    private static String getText(Reader p_reader)
            throws Exception {
        if (p_reader == null)
            return null;
        StringBuffer txtBuff = null;
        char buff[] = new char[50000];
        for (int nLen = -1; (nLen = p_reader.read(buff)) != -1; ) {
            if (txtBuff == null)
                txtBuff = new StringBuffer(nLen);
            txtBuff.append(buff, 0, nLen);
        }

        buff = null;
        p_reader.close();
        if (txtBuff == null)
            return "";
        else
            return txtBuff.toString();
    }

    public static String getText(ResultSet p_rsData, String p_sFieldName)
            throws CMyException {
        try {
            Reader reader = p_rsData.getCharacterStream(p_sFieldName);
            return getText(reader);
        } catch (Exception ex) {
            throw new CMyException(40, "从当前记录集中读取Text字段时出错(CSQLText.getText)", ex);
        }
    }

    public static String getText(ResultSet p_rsData, int p_nFieldIndex)
            throws CMyException {
        try {
            Reader reader = p_rsData.getCharacterStream(p_nFieldIndex);
            return getText(reader);
        } catch (Exception ex) {
            throw new CMyException(40, "从当前记录集中读取Text字段时出错(CSQLText.getText)", ex);
        }
    }
}
