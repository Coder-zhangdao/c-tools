package com.bixuebihui.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;


/**
 * <p>PooledPreparedStatement class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class PooledPreparedStatement extends PooledStatement
        implements PreparedStatement
{
    /**
     * <p>Constructor for PooledPreparedStatement.</p>
     *
     * @param preparedstatement a {@link java.sql.PreparedStatement} object.
     * @throws java.sql.SQLException if any.
     */
    public PooledPreparedStatement(PreparedStatement preparedstatement) throws SQLException {
        super.stmt = preparedstatement;
        pstmt = preparedstatement;
        if(pstmt==null) {
            throw new SQLException("Null preparedstatement!");
        }
    }

    /**
     * <p>close.</p>
     *
     * @throws java.sql.SQLException if any.
     */
    @Override
    public void close()
            throws SQLException {
        pstmt.clearParameters();
    }

    /**
     * <p>executeQuery.</p>
     *
     * @return a {@link java.sql.ResultSet} object.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public ResultSet executeQuery()
            throws SQLException {
        return pstmt.executeQuery();
    }

    /**
     * <p>executeUpdate.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public int executeUpdate()
            throws SQLException {
        return pstmt.executeUpdate();
    }

    /** {@inheritDoc} */
    @Override
    public void setNull(int i, int j)
            throws SQLException {
        pstmt.setNull(i, j);
    }

    /** {@inheritDoc} */
    @Override
    public void setBoolean(int i, boolean flag)
            throws SQLException {
        pstmt.setBoolean(i, flag);
    }

    /** {@inheritDoc} */
    @Override
    public void setByte(int i, byte byte0)
            throws SQLException {
        pstmt.setByte(i, byte0);
    }

    /** {@inheritDoc} */
    @Override
    public void setShort(int i, short word0)
            throws SQLException {
        pstmt.setShort(i, word0);
    }

    /** {@inheritDoc} */
    @Override
    public void setInt(int i, int j)
            throws SQLException {
        pstmt.setInt(i, j);
    }

    /** {@inheritDoc} */
    @Override
    public void setLong(int i, long l)
            throws SQLException {
        pstmt.setLong(i, l);
    }

    /** {@inheritDoc} */
    @Override
    public void setFloat(int i, float f)
            throws SQLException {
        pstmt.setFloat(i, f);
    }

    /** {@inheritDoc} */
    @Override
    public void setDouble(int i, double d)
            throws SQLException {
        pstmt.setDouble(i, d);
    }

    /** {@inheritDoc} */
    @Override
    public void setBigDecimal(int i, BigDecimal bigdecimal)
            throws SQLException {
        pstmt.setBigDecimal(i, bigdecimal);
    }

    /** {@inheritDoc} */
    @Override
    public void setString(int i, String s)
            throws SQLException {
        pstmt.setString(i, s);
    }

    /**
     * <p>setBytes.</p>
     *
     * @param i a int.
     * @param abyte0 an array of byte.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public void setBytes(int i, byte abyte0[])
            throws SQLException {
        pstmt.setBytes(i, abyte0);
    }

    /** {@inheritDoc} */
    @Override
    public void setDate(int i, Date date)
            throws SQLException {
        pstmt.setDate(i, date);
    }

    /** {@inheritDoc} */
    @Override
    public void setTime(int i, Time time)
            throws SQLException {
        pstmt.setTime(i, time);
    }

    /** {@inheritDoc} */
    @Override
    public void setTimestamp(int i, Timestamp timestamp)
            throws SQLException {
        pstmt.setTimestamp(i, timestamp);
    }

    /** {@inheritDoc} */
    @Override
    public void setAsciiStream(int i, InputStream inputstream, int j)
            throws SQLException {
        pstmt.setAsciiStream(i, inputstream, j);
    }

    /** {@inheritDoc} */
    @Override
    public void setUnicodeStream(int i, InputStream inputstream, int j)
            throws SQLException {
        pstmt.setUnicodeStream(i, inputstream, j);
    }

    /**
     * <p>setBinaryStream.</p>
     *
     * @param i a int.
     * @param inputstream a {@link java.io.InputStream} object.
     * @param j a int.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public void setBinaryStream(int i, InputStream inputstream, int j)
            throws SQLException {
        pstmt.setBinaryStream(i, inputstream, j);
    }

    /**
     * <p>clearParameters.</p>
     *
     * @throws java.sql.SQLException if any.
     */
    @Override
    public void clearParameters()
            throws SQLException {
        pstmt.clearParameters();
    }

    /** {@inheritDoc} */
    @Override
    public void setObject(int i, Object obj, int j, int k)
            throws SQLException {
        pstmt.setObject(i, obj, j, k);
    }

    /** {@inheritDoc} */
    @Override
    public void setObject(int i, Object obj, int j)
            throws SQLException {
        pstmt.setObject(i, obj, j);
    }

    /** {@inheritDoc} */
    @Override
    public void setObject(int i, Object obj)
            throws SQLException {
        pstmt.setObject(i, obj);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("deprecated")
    public void setCharacterStream(int i, Reader reader, int j)
            throws SQLException {
        pstmt.setCharacterStream(i, reader, j);
    }

    /**
     * <p>execute.</p>
     *
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public boolean execute()
            throws SQLException {
        return pstmt.execute();
    }

    /**
     * <p>getFetchDirection.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public int getFetchDirection()
            throws SQLException {
        return pstmt.getFetchDirection();
    }

    /** {@inheritDoc} */
    @Override
    public void setFetchDirection(int i)
            throws SQLException {
        pstmt.setFetchDirection(i);
    }

    /**
     * <p>executeBatch.</p>
     *
     * @return an array of int.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public int[] executeBatch()
            throws SQLException {
        return pstmt.executeBatch();
    }

    /**
     * <p>clearBatch.</p>
     *
     * @throws java.sql.SQLException if any.
     */
    @Override
    public void clearBatch()
            throws SQLException {
        pstmt.clearBatch();
    }

    /**
     * <p>addBatch.</p>
     *
     * @throws java.sql.SQLException if any.
     */
    @Override
    public void addBatch()
            throws SQLException {
        pstmt.addBatch();
    }

    /** {@inheritDoc} */
    @Override
    public void addBatch(String s)
            throws SQLException {
        pstmt.addBatch(s);
    }

    /** {@inheritDoc} */
    @Override
    public void setRef(int i, Ref ref)
            throws SQLException {
        pstmt.setRef(i, ref);
    }

    /**
     * <p>getResultSetConcurrency.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public int getResultSetConcurrency()
            throws SQLException {
        return pstmt.getResultSetConcurrency();
    }

    /** {@inheritDoc} */
    @Override
    public void setClob(int i, Clob clob)
            throws SQLException {
        pstmt.setClob(i, clob);
    }

    /** {@inheritDoc} */
    @Override
    public void setDate(int i, Date date, Calendar calendar)
            throws SQLException {
        pstmt.setDate(i, date, calendar);
    }

    /** {@inheritDoc} */
    @Override
    public void setArray(int i, Array array)
            throws SQLException {
        pstmt.setArray(i, array);
    }

    /**
     * <p>setBlob.</p>
     *
     * @param i a int.
     * @param blob a {@link java.sql.Blob} object.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public void setBlob(int i, Blob blob)
            throws SQLException {
        pstmt.setBlob(i, blob);
    }

    /**
     * <p>getFetchSize.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public int getFetchSize()
            throws SQLException {
        return pstmt.getFetchSize();
    }

    /** {@inheritDoc} */
    @Override
    public void setFetchSize(int i)
            throws SQLException {
        pstmt.setFetchSize(i);
    }

    /** {@inheritDoc} */
    @Override
    public void setTime(int i, Time time, Calendar calendar)
            throws SQLException {
        pstmt.setTime(i, time, calendar);
    }

    /**
     * <p>getConnection.</p>
     *
     * @return a {@link java.sql.Connection} object.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public Connection getConnection()
            throws SQLException {
        return pstmt.getConnection();
    }

    /**
     * <p>getMetaData.</p>
     *
     * @return a {@link java.sql.ResultSetMetaData} object.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public ResultSetMetaData getMetaData()
            throws SQLException {
        return pstmt.getMetaData();
    }

    /**
     * <p>getResultSetType.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public int getResultSetType()
            throws SQLException {
        return pstmt.getResultSetType();
    }

    /** {@inheritDoc} */
    @Override
    public void setTimestamp(int i, Timestamp timestamp, Calendar calendar)
            throws SQLException {
        pstmt.setTimestamp(i, timestamp, calendar);
    }

    /**
     * <p>Constructor for PooledPreparedStatement.</p>
     */
    protected PooledPreparedStatement() {
    }

    /** {@inheritDoc} */
    @Override
    public void setNull(int i, int j, String s)
            throws SQLException {
        pstmt.setNull(i, j, s);
    }

    protected PreparedStatement pstmt;


    //add by [xing]

    /**
     * <p>getParameterMetaData.</p>
     *
     * @return a {@link java.sql.ParameterMetaData} object.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public ParameterMetaData  getParameterMetaData() throws SQLException {
        return pstmt.getParameterMetaData();
    }

    /** {@inheritDoc} */
    @Override
    public void setURL(int parameterIndex, java.net.URL x) throws SQLException{
        pstmt.setURL(parameterIndex, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x)
            throws SQLException {
        pstmt.setAsciiStream(parameterIndex, x);

    }

    /**
     * <p>setAsciiStream.</p>
     *
     * @param parameterIndex a int.
     * @param x a {@link java.io.InputStream} object.
     * @param length a long.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length)
            throws SQLException {
        pstmt.setAsciiStream(parameterIndex, x, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setBinaryStream(int parameterIndex, InputStream x)
            throws SQLException {
        pstmt.setBinaryStream(parameterIndex, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length)
            throws SQLException {
        pstmt.setBinaryStream(parameterIndex, x, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setBlob(int parameterIndex, InputStream inputStream)
            throws SQLException {
        pstmt.setBlob(parameterIndex, inputStream);

    }

    /** {@inheritDoc} */
    @Override
    public void setCharacterStream(int parameterIndex, Reader reader)
            throws SQLException {
        pstmt.setCharacterStream(parameterIndex, reader);
    }

    /** {@inheritDoc} */
    @Override
    public void setCharacterStream(int parameterIndex, Reader reader,
                                   long length) throws SQLException {
        pstmt.setCharacterStream(parameterIndex, reader, length);

    }

    /**
     * <p>setClob.</p>
     *
     * @param parameterIndex a int.
     * @param reader a {@link java.io.Reader} object.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        pstmt.setClob(parameterIndex, reader);
    }

    /** {@inheritDoc} */
    @Override
    public void setNCharacterStream(int parameterIndex, Reader value)
            throws SQLException {
        pstmt.setNCharacterStream(parameterIndex, value);
    }


    /** {@inheritDoc} */
    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        pstmt.setNClob(parameterIndex, reader);

    }

    /**
     * <p>isClosed.</p>
     *
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public boolean isClosed() throws SQLException {
        return pstmt.isClosed();
    }


    /** {@inheritDoc} */
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return pstmt.unwrap(iface);
    }

    /** {@inheritDoc} */
    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length)
            throws SQLException {
        pstmt.setBlob(parameterIndex, inputStream, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setClob(int parameterIndex, Reader reader, long length)
            throws SQLException {
        pstmt.setClob(parameterIndex, reader, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setNCharacterStream(int parameterIndex, Reader value,
                                    long length) throws SQLException {
        pstmt.setNCharacterStream(parameterIndex, value, length);
    }

    /**
     * <p>setNClob.</p>
     *
     * @param parameterIndex a int.
     * @param value a {@link java.sql.NClob} object.
     * @throws java.sql.SQLException if any.
     */
    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        pstmt.setNClob(parameterIndex, value);
    }

    /** {@inheritDoc} */
    @Override
    public void setNClob(int parameterIndex, Reader reader, long length)
            throws SQLException {
        pstmt.setNClob(parameterIndex, reader, length);
    }

    /** {@inheritDoc} */
    @Override
    public void setNString(int parameterIndex, String value)
            throws SQLException {
        pstmt.setNString(parameterIndex, value);
    }

    /** {@inheritDoc} */
    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        pstmt.setRowId(parameterIndex, x);
    }

    /** {@inheritDoc} */
    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject)
            throws SQLException {
        pstmt.setSQLXML(parameterIndex, xmlObject);
    }


}
