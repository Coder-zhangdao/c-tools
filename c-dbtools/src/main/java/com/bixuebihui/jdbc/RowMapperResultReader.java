package com.bixuebihui.jdbc;

import javax.validation.constraints.NotNull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>RowMapperResultReader class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class RowMapperResultReader<T> {

	private RowMapper<T> handle;
	private List<T> res;

    /**
     * <p>Constructor for RowMapperResultReader.</p>
     *
     * @param handle a {@link RowMapper} object.
     */
    public RowMapperResultReader(RowMapper<T> handle){
        this.handle = handle;
    }

    /**
     * <p>Constructor for RowMapperResultReader.</p>
     *
     * @param handle a {@link RowMapper} object.
     * @param resultList a {@link java.util.List} object.
     */
    public RowMapperResultReader(RowMapper<T> handle, List<T> resultList){
        this.handle = handle;
        this.res = resultList;
    }

    /**
     * <p>processResultSet.</p>
     *
     * @param rs a {@link java.sql.ResultSet} object.
     * @return a {@link java.util.List} object.
     * @throws java.sql.SQLException if any.
     */
    public @NotNull List<T> processResultSet(ResultSet rs) throws SQLException {
        if(res==null){
            res = new ArrayList<T>();
        }else{
            res.clear();
        }

        int index=1;
        while (rs.next()) {
            res.add( handle.mapRow(rs, index++));
        }
        return res;
    }
}
