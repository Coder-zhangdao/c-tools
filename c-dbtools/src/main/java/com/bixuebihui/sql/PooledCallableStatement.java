// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-1-4 11:28:25
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   PooledCallableStatement.java

package com.bixuebihui.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;


/**
 * <p>PooledCallableStatement class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class PooledCallableStatement extends PooledPreparedStatement implements CallableStatement {

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws SQLException {
        /**
         * This is empty because no need close native Statement, it will closed by PoolManager
         * need confirm test!
         */
    }

    /** {@inheritDoc} */
    @Override
    public void registerOutParameter(int i, int j) throws SQLException {
        cstmt.registerOutParameter(i, j);
    }

    /** {@inheritDoc} */
    @Override
    public void registerOutParameter(int i, int j, int k) throws SQLException {
        cstmt.registerOutParameter(i, j, k);
    }

    /** {@inheritDoc} */
    @Override
    public boolean wasNull() throws SQLException {
        return cstmt.wasNull();
    }

    /** {@inheritDoc} */
    @Deprecated
    @Override
    public BigDecimal getBigDecimal(int i, int j) throws SQLException {
        return cstmt.getBigDecimal(i, j);
    }

    /** {@inheritDoc} */
    @Override
    public boolean getBoolean(int i) throws SQLException {
        return cstmt.getBoolean(i);
    }


    /** {@inheritDoc} */
    @Override
    public byte getByte(int i) throws SQLException {
        return cstmt.getByte(i);
    }

    /** {@inheritDoc} */
    @Override
    public byte[] getBytes(int i) throws SQLException {
        return cstmt.getBytes(i);
    }

    /** {@inheritDoc} */
    @Override
    public Date getDate(int i) throws SQLException {
        return cstmt.getDate(i);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(int i) throws SQLException {
        return cstmt.getDouble(i);
    }

    /** {@inheritDoc} */
    @Override
    public float getFloat(int i) throws SQLException {
        return cstmt.getFloat(i);
    }

    /** {@inheritDoc} */
    @Override
    public int getInt(int i) throws SQLException {
        return cstmt.getInt(i);
    }

    /** {@inheritDoc} */
    @Override
    public long getLong(int i) throws SQLException {
        return cstmt.getLong(i);
    }

    /** {@inheritDoc} */
    @Override
    public Object getObject(int i) throws SQLException {
        return cstmt.getObject(i);
    }

    /** {@inheritDoc} */
    @Override
    public short getShort(int i) throws SQLException {
        return cstmt.getShort(i);
    }

    /** {@inheritDoc} */
    @Override
    public String getString(int i) throws SQLException {
        return cstmt.getString(i);
    }

    /** {@inheritDoc} */
    @Override
    public Time getTime(int i) throws SQLException {
        return cstmt.getTime(i);
    }

    /** {@inheritDoc} */
    @Override
    public Timestamp getTimestamp(int i) throws SQLException {
        return cstmt.getTimestamp(i);
    }

    /** {@inheritDoc} */
    @Override
    public Time getTime(int i, Calendar calendar) throws SQLException {
        return cstmt.getTime(i, calendar);
    }

    /** {@inheritDoc} */
    @Override
    public Clob getClob(int i) throws SQLException {
        return cstmt.getClob(i);
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal getBigDecimal(int i) throws SQLException {
        return cstmt.getBigDecimal(i);
    }

    /** {@inheritDoc} */
    @Override
    public void registerOutParameter(int i, int j, String s) throws SQLException {
        cstmt.registerOutParameter(i, j, s);
    }

    /** {@inheritDoc} */
    @Override
    public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
        return cstmt.getTimestamp(i, calendar);
    }

    /** {@inheritDoc} */
    @Override
    public Blob getBlob(int i) throws SQLException {
        return cstmt.getBlob(i);
    }

    /** {@inheritDoc} */
    @Override
    public Array getArray(int i) throws SQLException {
        return cstmt.getArray(i);
    }

    /** {@inheritDoc} */
    @Override
    public Ref getRef(int i) throws SQLException {
        return cstmt.getRef(i);
    }

    /** {@inheritDoc} */
    @Override
    public Date getDate(int i, Calendar calendar) throws SQLException {
        return cstmt.getDate(i, calendar);
    }

    /**
     * <p>Constructor for PooledCallableStatement.</p>
     *
     * @param callablestatement a {@link java.sql.CallableStatement} object.
     */
    public PooledCallableStatement(CallableStatement callablestatement) {
        cstmt = callablestatement;
    }

    protected CallableStatement cstmt;

    // Class must implement the inherited abstract method
    // [xing]

    /** {@inheritDoc} */
    @Override
    public byte[] getBytes(String parameterName) throws SQLException {
        return cstmt.getBytes(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public void setDouble(String parameterName, double x) throws SQLException {
        cstmt.setDouble(parameterName, x);
    }

    /** {@inheritDoc} */
    @Override
    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return cstmt.getBigDecimal(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(String parameterName) throws SQLException {
        return cstmt.getDouble(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public Ref getRef(String parameterName) throws SQLException {
        return cstmt.getRef(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public void setBoolean(String parameterName, boolean x) throws SQLException {
        cstmt.setBoolean(parameterName, x);
    }

    /** {@inheritDoc} */
    @Override
    public Array getArray(String parameterName) throws SQLException {
        return cstmt.getArray(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public void setLong(String parameterName, long x) throws SQLException {
        cstmt.setLong(parameterName, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setDate(String parameterName, Date x) throws SQLException {
        cstmt.setDate(parameterName, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        cstmt.setTimestamp(parameterName, x);
    }

    /** {@inheritDoc} */
    @Override
    public String getString(String parameterName) throws SQLException {
        return cstmt.getString(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public void setTime(String parameterName, Time x) throws SQLException {
        cstmt.setTime(parameterName, x);
    }

    /** {@inheritDoc} */
    @Override
    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        cstmt.registerOutParameter(parameterName, sqlType);
    }

    /** {@inheritDoc} */
    @Override
    public void setString(String parameterName, String x) throws SQLException {
        cstmt.setString(parameterName, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        cstmt.setAsciiStream(parameterName, x, length);
    }

    /** {@inheritDoc} */
    @Override
    public Blob getBlob(String parameterName) throws SQLException {
        return cstmt.getBlob(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public Clob getClob(String parameterName) throws SQLException {
        return cstmt.getClob(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public void setByte(String parameterName, byte x) throws SQLException {
        cstmt.setByte(parameterName, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setURL(String parameterName, java.net.URL val) throws SQLException {
        cstmt.setURL(parameterName, val);
    }

    /** {@inheritDoc} */
    @Override
    public Time getTime(String parameterName) throws SQLException {
        return cstmt.getTime(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public void setShort(String parameterName, short x) throws SQLException {
        cstmt.setShort(parameterName, x);
    }

    /** {@inheritDoc} */
    @Override
    public java.net.URL getURL(int parameterIndex) throws SQLException {
        return cstmt.getURL(parameterIndex);
    }

    /** {@inheritDoc} */
    @Override
    public boolean getBoolean(String parameterName) throws SQLException {
        return cstmt.getBoolean(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        cstmt.setObject(parameterName, x, targetSqlType, scale);
    }

    /** {@inheritDoc} */
    @Override
    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        cstmt.setObject(parameterName, x, targetSqlType);
    }

    /** {@inheritDoc} */
    @Override
    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        cstmt.setTime(parameterName, x, cal);
    }

    /** {@inheritDoc} */
    @Override
    public byte getByte(String p) throws SQLException {
        return cstmt.getByte(p);
    }

    /** {@inheritDoc} */
    @Override
    public java.sql.Date getDate(String p, Calendar cal) throws SQLException {
        return cstmt.getDate(p, cal);
    }

    /** {@inheritDoc} */
    @Override
    public java.sql.Date getDate(String p) throws SQLException {
        return cstmt.getDate(p);
    }

    /** {@inheritDoc} */
    @Override
    public float getFloat(String p) throws SQLException {
        return cstmt.getFloat(p);
    }

    /** {@inheritDoc} */
    @Override
    public int getInt(String p) throws SQLException {
        return cstmt.getInt(p);
    }

    /** {@inheritDoc} */
    @Override
    public long getLong(String p) throws SQLException {
        return cstmt.getLong(p);
    }

    /** {@inheritDoc} */
    @Override
    public Object getObject(String p) throws SQLException {
        return cstmt.getObject(p);
    }

    /** {@inheritDoc} */
    @Override
    public short getShort(String p) throws SQLException {
        return cstmt.getShort(p);
    }

    /** {@inheritDoc} */
    @Override
    public Time getTime(String p, Calendar cal) throws SQLException {
        return cstmt.getTime(p, cal);
    }

    /** {@inheritDoc} */
    @Override
    public Timestamp getTimestamp(String p, Calendar cal) throws SQLException {
        return cstmt.getTimestamp(p, cal);
    }

    /** {@inheritDoc} */
    @Override
    public Timestamp getTimestamp(String p) throws SQLException {
        return cstmt.getTimestamp(p);
    }

    /** {@inheritDoc} */
    @Override
    public java.net.URL getURL(String p) throws SQLException {
        return cstmt.getURL(p);
    }

    /** {@inheritDoc} */
    @Override
    public void registerOutParameter(String p, int x, int l) throws SQLException {
        cstmt.registerOutParameter(p, x, l);
    }

    /** {@inheritDoc} */
    @Override
    public void registerOutParameter(String p, int x, String l) throws SQLException {
        cstmt.registerOutParameter(p, x, l);
    }

    /** {@inheritDoc} */
    @Override
    public void setBigDecimal(String p, BigDecimal x) throws SQLException {
        cstmt.setBigDecimal(p, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setBinaryStream(String p, java.io.InputStream x, int l) throws SQLException {
        cstmt.setBinaryStream(p, x, l);
    }

    /** {@inheritDoc} */
    @Override
    public void setBytes(String p, byte[] x) throws SQLException {
        cstmt.setBytes(p, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setCharacterStream(String p, java.io.Reader r, int l) throws SQLException {
        cstmt.setCharacterStream(p, r, l);
    }

    /** {@inheritDoc} */
    @Override
    public void setDate(String p, Date x, Calendar cal) throws SQLException {
        cstmt.setDate(p, x, cal);
    }

    /** {@inheritDoc} */
    @Override
    public void setFloat(String p, float x) throws SQLException {
        cstmt.setFloat(p, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setInt(String p, int x) throws SQLException {
        cstmt.setInt(p, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setNull(String p, int x, String l) throws SQLException {
        cstmt.setNull(p, x, l);
    }

    /** {@inheritDoc} */
    @Override
    public void setNull(String p, int x) throws SQLException {
        cstmt.setNull(p, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setObject(String p, Object x) throws SQLException {
        cstmt.setObject(p, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setTimestamp(String p, Timestamp x, Calendar cal) throws SQLException {
        cstmt.setTimestamp(p, x, cal);
    }

    /** {@inheritDoc} */
    @Override
    public Reader getCharacterStream(int parameterIndex) throws SQLException {

        return cstmt.getCharacterStream(parameterIndex);
    }

    /** {@inheritDoc} */
    @Override
    public Reader getCharacterStream(String parameterName) throws SQLException {
        return cstmt.getCharacterStream(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public Reader getNCharacterStream(int parameterIndex) throws SQLException {
        return cstmt.getNCharacterStream(parameterIndex);
    }

    /** {@inheritDoc} */
    @Override
    public Reader getNCharacterStream(String parameterName) throws SQLException {
        return cstmt.getNCharacterStream(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public String getNString(int parameterIndex) throws SQLException {
        return cstmt.getNString(parameterIndex);
    }

    /** {@inheritDoc} */
    @Override
    public String getNString(String parameterName) throws SQLException {
        return cstmt.getNString(parameterName);
    }

    /** {@inheritDoc} */
    @Override
    public void setAsciiStream(String parameterName, InputStream x) throws SQLException {
        cstmt.setAsciiStream(parameterName, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setAsciiStream(String parameterName, InputStream x, long length) throws SQLException {
        cstmt.setAsciiStream(parameterName, x, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setBinaryStream(String parameterName, InputStream x) throws SQLException {
        cstmt.setBinaryStream(parameterName, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setBinaryStream(String parameterName, InputStream x, long length) throws SQLException {
        cstmt.setBinaryStream(parameterName, x, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setBlob(String parameterName, Blob x) throws SQLException {
        cstmt.setBlob(parameterName, x);
    }


    /** {@inheritDoc} */
    @Override
    public void setBlob(String parameterName, InputStream inputStream) throws SQLException {
        cstmt.setBlob(parameterName, inputStream);
    }

    /** {@inheritDoc} */
    @Override
    public void setBlob(String parameterName, InputStream inputStream, long length) throws SQLException {
        cstmt.setBlob(parameterName, inputStream, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setCharacterStream(String parameterName, Reader reader) throws SQLException {
        cstmt.setCharacterStream(parameterName, reader);

    }

    /** {@inheritDoc} */
    @Override
    public void setCharacterStream(String parameterName, Reader reader, long length) throws SQLException {
        cstmt.setCharacterStream(parameterName, reader, length);

    }

    /** {@inheritDoc} */
    @Override
    public void setClob(String parameterName, Clob x) throws SQLException {
        cstmt.setClob(parameterName, x);

    }

    /** {@inheritDoc} */
    @Override
    public void setClob(String parameterName, Reader reader) throws SQLException {
        cstmt.setClob(parameterName, reader);
    }

    /** {@inheritDoc} */
    @Override
    public void setClob(String parameterName, Reader reader, long length) throws SQLException {
        cstmt.setClob(parameterName, reader, length);

    }

    /** {@inheritDoc} */
    @Override
    public void setNCharacterStream(String parameterName, Reader value) throws SQLException {
        cstmt.setNCharacterStream(parameterName, value);

    }

    /** {@inheritDoc} */
    @Override
    public void setNCharacterStream(String parameterName, Reader value, long length) throws SQLException {
        cstmt.setNCharacterStream(parameterName, value, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setNClob(String parameterName, Reader reader) throws SQLException {
        cstmt.setNClob(parameterName, reader);
    }

    /** {@inheritDoc} */
    @Override
    public void setNClob(String parameterName, Reader reader, long length) throws SQLException {
        cstmt.setNClob(parameterName, reader, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setNString(String parameterName, String value) throws SQLException {
        cstmt.setNString(parameterName, value);

    }

    /** {@inheritDoc} */
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        cstmt.setAsciiStream(parameterIndex, x);

    }

    /** {@inheritDoc} */
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        cstmt.setAsciiStream(parameterIndex, x, length);

    }

    /** {@inheritDoc} */
    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        cstmt.setBinaryStream(parameterIndex, x);

    }

    /** {@inheritDoc} */
    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        cstmt.setBinaryStream(parameterIndex, x, length);

    }

    /** {@inheritDoc} */
    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        cstmt.setBlob(parameterIndex, inputStream);

    }

    /** {@inheritDoc} */
    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        cstmt.setBlob(parameterIndex, inputStream, length);

    }

    /** {@inheritDoc} */
    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        cstmt.setCharacterStream(parameterIndex, reader);

    }

    /** {@inheritDoc} */
    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        cstmt.setCharacterStream(parameterIndex, reader, length);

    }

    /** {@inheritDoc} */
    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        cstmt.setClob(parameterIndex, reader);
    }

    /** {@inheritDoc} */
    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        cstmt.setClob(parameterIndex, reader, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        cstmt.setNCharacterStream(parameterIndex, value);

    }

    /** {@inheritDoc} */
    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        cstmt.setNCharacterStream(parameterIndex, value, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        cstmt.setNClob(parameterIndex, reader);
    }

    /** {@inheritDoc} */
    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        cstmt.setNClob(parameterIndex, reader, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        cstmt.setNString(parameterIndex, value);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isClosed() throws SQLException {
        return cstmt.isClosed();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isPoolable() throws SQLException {
        return cstmt.isPoolable();
    }

    /** {@inheritDoc} */
    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        cstmt.setPoolable(poolable);
    }

    /** {@inheritDoc} */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return cstmt.unwrap(iface);
    }

    /** {@inheritDoc} */
    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        cstmt.setNClob(parameterIndex, value);
    }

    /** {@inheritDoc} */
    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        cstmt.setRowId(parameterIndex, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        cstmt.setSQLXML(parameterIndex, xmlObject);
    }

    /** {@inheritDoc} */
    @Override
    public NClob getNClob(int parameterIndex) throws SQLException {
        return cstmt.getNClob(parameterIndex);
    }

    /** {@inheritDoc} */
    @Override
    public NClob getNClob(String parameterIndex) throws SQLException {
        return cstmt.getNClob(parameterIndex);
    }

    /** {@inheritDoc} */
    @Override
    public RowId getRowId(int parameterIndex) throws SQLException {
        return cstmt.getRowId(parameterIndex);
    }

    /** {@inheritDoc} */
    @Override
    public RowId getRowId(String parameterIndex) throws SQLException {
        return cstmt.getRowId(parameterIndex);
    }

    /** {@inheritDoc} */
    @Override
    public SQLXML getSQLXML(int arg0) throws SQLException {
        return cstmt.getSQLXML(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public SQLXML getSQLXML(String arg0) throws SQLException {
        return cstmt.getSQLXML(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public void setNClob(String arg0, NClob arg1) throws SQLException {
        cstmt.setNClob(arg0, arg1);
    }

    /** {@inheritDoc} */
    @Override
    public void setRowId(String arg0, RowId arg1) throws SQLException {
        cstmt.setRowId(arg0, arg1);
    }

    /** {@inheritDoc} */
    @Override
    public void setSQLXML(String parameterIndex, SQLXML xmlObject) throws SQLException {
        cstmt.setSQLXML(parameterIndex, xmlObject);
    }

    /** {@inheritDoc} */
    @Override
    public void closeOnCompletion() throws SQLException {
        cstmt.closeOnCompletion();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return cstmt.isCloseOnCompletion();
    }

    /** {@inheritDoc} */
    @Override
    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        return cstmt.getObject(parameterIndex, map);
    }

    /** {@inheritDoc} */
    @Override
    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        return cstmt.getObject(parameterName, map);
    }

    /** {@inheritDoc} */
    @Override
    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        return cstmt.getObject(parameterIndex, type);

    }

    /** {@inheritDoc} */
    @Override
    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        return cstmt.getObject(parameterName, type);

    }

}
