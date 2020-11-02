package com.bixuebihui.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>AbstractBaseDao class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class AbstractBaseDao extends BaseDao<Object, Long> {

    /**
     * <p>Constructor for AbstractBaseDao.</p>
     *
     * @param db a {@link IDbHelper} object.
     */
    public AbstractBaseDao(IDbHelper db) {
        this.dbHelper = db;
    }

    /** {@inheritDoc} */
    public Object mapRow(ResultSet rs, int index) throws SQLException {
        throw new SQLException("not implement!");
    }


    /**
     * <p>insertDummy.</p>
     *
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    public boolean insertDummy() throws SQLException {
        throw new SQLException("not implement!");
    }

    /** {@inheritDoc} */
    public Long getId(Object info) {
        return 0L;
    }

    /**
     * <p>setId.</p>
     *
     * @param info a {@link java.lang.Object} object.
     * @param id a {@link java.lang.Long} object.
     */
    public void setId(Object info, Long id) {
        //must override if the table has a primary key!
    }


    /** {@inheritDoc} */
    @Override
    public String getTableName() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getKeyName() {
        return null;
    }

    /**
     * <p>getDummySql.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws java.sql.SQLException if any.
     */
    public String getDummySql() throws SQLException {
        if(this.getDBTYPE() == ORACLE)
            return " from dual";
        return "";
    }

}
