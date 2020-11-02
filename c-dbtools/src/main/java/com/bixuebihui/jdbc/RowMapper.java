package com.bixuebihui.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>RowMapper interface.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public interface RowMapper<T> {
   /**
    * <p>mapRow.</p>
    *
    * @param rs    a {@link java.sql.ResultSet} object.
    * @param index a int.
    * @return a T object.
    * @throws java.sql.SQLException if any.
    */
   T mapRow(ResultSet rs, int index) throws SQLException;
}
