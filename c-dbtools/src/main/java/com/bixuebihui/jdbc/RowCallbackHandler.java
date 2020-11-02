package com.bixuebihui.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>RowCallbackHandler interface.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public interface RowCallbackHandler
{
    /**
     * <p>processRow.</p>
     *
     * @param resultset a {@link java.sql.ResultSet} object.
     * @throws java.sql.SQLException if any.
     */
    void processRow(ResultSet resultset) throws SQLException;
}
