// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-1-4 11:28:43
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   PooledStatement.java

package com.bixuebihui.sql;

import java.io.Reader;
import java.sql.*;

/**
 * <p>PooledStatement class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class PooledStatement
        implements Statement
{

    /**
     * <p>close.</p>
     *
     * @throws java.sql.SQLException if any.
     */
    public void close()
            throws SQLException {
        stmt.setMaxRows(0);
    }

    /**
     * <p>getStatement.</p>
     *
     * @return a {@link java.sql.Statement} object.
     */
    public Statement getStatement() {
        return stmt;
    }

    /**
     * <p>dumpInfo.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String dumpInfo() {
        String s = System.getProperty("line.separator");
        String s1 = "\t\tPooledStatement: " + toString() + s;
        s1 += "\t\t\t Last Query SQL: " + lastQuerySQL + s;
        s1 += "\t\t\t Last Update SQL: " + lastUpdateSQL + s;
        return s1;
    }

    /**
     * {@inheritDoc}
     */
    public ResultSet executeQuery(String s)
            throws SQLException {
        lastQuerySQL = s;
        return stmt.executeQuery(s);
    }

    /** {@inheritDoc} */
    public int executeUpdate(String s)
            throws SQLException {
        lastUpdateSQL = s;
        return stmt.executeUpdate(s);
    }

    /**
     * <p>getMaxFieldSize.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    public int getMaxFieldSize()
            throws SQLException {
        return stmt.getMaxFieldSize();
    }

    /** {@inheritDoc} */
    public void setMaxFieldSize(int i)
            throws SQLException {
        stmt.setMaxFieldSize(i);
    }

    /**
     * <p>getMaxRows.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    public int getMaxRows()
            throws SQLException {
        return stmt.getMaxRows();
    }

    /** {@inheritDoc} */
    public void setMaxRows(int i)
            throws SQLException {
        stmt.setMaxRows(i);
    }

    /** {@inheritDoc} */
    public void setEscapeProcessing(boolean flag)
            throws SQLException {
        stmt.setEscapeProcessing(flag);
    }

    /**
     * <p>getQueryTimeout.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    public int getQueryTimeout()
            throws SQLException {
        return stmt.getQueryTimeout();
    }

    /** {@inheritDoc} */
    public void setQueryTimeout(int i)
            throws SQLException {
        stmt.setQueryTimeout(i);
    }

    /**
     * <p>cancel.</p>
     *
     * @throws java.sql.SQLException if any.
     */
    public void cancel()
            throws SQLException {
        stmt.cancel();
    }

    /**
     * <p>getWarnings.</p>
     *
     * @return a {@link java.sql.SQLWarning} object.
     * @throws java.sql.SQLException if any.
     */
    public SQLWarning getWarnings()
            throws SQLException {
        return stmt.getWarnings();
    }

    /**
     * <p>clearWarnings.</p>
     *
     * @throws java.sql.SQLException if any.
     */
    public void clearWarnings()
            throws SQLException {
        stmt.clearWarnings();
    }

    /** {@inheritDoc} */
    public void setCursorName(String s)
            throws SQLException {
        stmt.setCursorName(s);
    }

    /** {@inheritDoc} */
    public boolean execute(String s)
            throws SQLException {
        return stmt.execute(s);
    }

    /**
     * <p>getResultSet.</p>
     *
     * @return a {@link java.sql.ResultSet} object.
     * @throws java.sql.SQLException if any.
     */
    public ResultSet getResultSet()
            throws SQLException {
        return stmt.getResultSet();
    }

    /**
     * <p>getUpdateCount.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    public int getUpdateCount()
            throws SQLException {
        return stmt.getUpdateCount();
    }

    /**
     * <p>getMoreResults.</p>
     *
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    public boolean getMoreResults()
            throws SQLException {
        return stmt.getMoreResults();
    }


    /**
     * <p>setCharacterStream.</p>
     *
     * @deprecated dont use! not implement!
     * @param i  not implement
     * @param reader not implement
     * @param j not implement
     * @throws java.sql.SQLException not implement
     */
    @Deprecated
    public void setCharacterStream(int i, Reader reader, int j)
            throws SQLException {
        throw new SQLException("not implement!");
    }

    /**
     * <p>getFetchDirection.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    public int getFetchDirection()
            throws SQLException {
        return stmt.getFetchDirection();
    }

    /**
     * <p>executeBatch.</p>
     *
     * @return an array of int.
     * @throws java.sql.SQLException if any.
     */
    public int[] executeBatch()
            throws SQLException {
        return stmt.executeBatch();
    }

    /** {@inheritDoc} */
    public void setFetchSize(int i)
            throws SQLException {
        stmt.setFetchSize(i);
    }

    /**
     * <p>clearBatch.</p>
     *
     * @throws java.sql.SQLException if any.
     */
    public void clearBatch()
            throws SQLException {
        stmt.clearBatch();
    }

    /** {@inheritDoc} */
    public void addBatch(String s)
            throws SQLException {
        stmt.addBatch(s);
    }

    /**
     * <p>getResultSetConcurrency.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    public int getResultSetConcurrency()
            throws SQLException {
        return stmt.getResultSetConcurrency();
    }

    /** {@inheritDoc} */
    public void setFetchDirection(int i)
            throws SQLException {
        stmt.setFetchDirection(i);
    }

    /**
     * <p>getFetchSize.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    public int getFetchSize()
            throws SQLException {
        return stmt.getFetchSize();
    }

    /**
     * <p>getConnection.</p>
     *
     * @return a {@link java.sql.Connection} object.
     * @throws java.sql.SQLException if any.
     */
    public Connection getConnection()
            throws SQLException {
        return stmt.getConnection();
    }

    /**
     * <p>getResultSetType.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    public int getResultSetType()
            throws SQLException {
        return stmt.getResultSetType();
    }

    /**
     * <p>Constructor for PooledStatement.</p>
     */
    protected PooledStatement() {
    }

    /**
     * <p>Constructor for PooledStatement.</p>
     *
     * @param statement a {@link java.sql.Statement} object.
     */
    public PooledStatement(Statement statement) {
        stmt = statement;
    }

    protected Statement stmt;
    String lastQuerySQL;
    String lastUpdateSQL;


    // added by [xing]

    /** {@inheritDoc} */
    public boolean execute(String p, int x)throws SQLException{
        return stmt.execute(p, x);
    }

    /**
     * <p>execute.</p>
     *
     * @param p a {@link java.lang.String} object.
     * @param x an array of  int.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    public boolean execute(String p, int[] x)throws SQLException	{
        return stmt.execute(p, x);
    }

    /**
     * <p>execute.</p>
     *
     * @param p a {@link java.lang.String} object.
     * @param x an array of {@link java.lang.String} objects.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    public boolean execute(String p, String[] x)throws SQLException{
        return stmt.execute(p, x);
    }

    /**
     * <p>executeUpdate.</p>
     *
     * @param p a {@link java.lang.String} object.
     * @param x a int.
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    public int executeUpdate(String p, int x)throws SQLException{
        return stmt.executeUpdate(p, x);
    }

    /**
     * <p>executeUpdate.</p>
     *
     * @param p a {@link java.lang.String} object.
     * @param x an array of int.
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    public int executeUpdate(String p, int[] x)	throws SQLException{
        return stmt.executeUpdate(p, x);
    }

    /**
     * <p>executeUpdate.</p>
     *
     * @param p a {@link java.lang.String} object.
     * @param x an array of {@link java.lang.String} objects.
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    public int executeUpdate(String p, String[] x)throws SQLException{
        return stmt.executeUpdate(p, x);
    }

    /**
     * <p>getGeneratedKeys.</p>
     *
     * @return a {@link java.sql.ResultSet} object.
     * @throws java.sql.SQLException if any.
     */
    public ResultSet getGeneratedKeys()throws SQLException{
        return stmt.getGeneratedKeys();
    }

    /** {@inheritDoc} */
    public boolean getMoreResults(int i)	throws SQLException{
        return stmt.getMoreResults(i);
    }

    /**
     * <p>getResultSetHoldability.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    public int getResultSetHoldability()	throws SQLException	{
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    /**
     * <p>isClosed.</p>
     *
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    public boolean isClosed() throws SQLException {
        return stmt.isClosed();
    }


    /**
     * <p>isPoolable.</p>
     *
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    public boolean isPoolable() throws SQLException {
        return stmt.isPoolable();
    }

    /** {@inheritDoc} */
    public void setPoolable(boolean poolable) throws SQLException {
        stmt.setPoolable(poolable);
    }


    /** {@inheritDoc} */
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return stmt.unwrap(iface);
    }

    /**
     * <p>closeOnCompletion.</p>
     *
     * @throws java.sql.SQLException if any.
     */
    public void closeOnCompletion() throws SQLException {
        stmt.closeOnCompletion();

    }

    /**
     * <p>isCloseOnCompletion.</p>
     *
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    public boolean isCloseOnCompletion() throws SQLException {
        return stmt.isCloseOnCompletion();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return stmt.isWrapperFor(iface);
    }
}
