package com.bixuebihui.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>LongReader class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class LongReader extends RowMapperResultReader<Long> {

    /**
     * <p>Constructor for LongReader.</p>
     *
     * @param resultList a {@link java.util.List} object.
     */
    public LongReader(List<Long> resultList) {

        super(new RowMapper<Long>() {
            @Override
            public Long mapRow(ResultSet rs, int index) throws SQLException {
                return rs.getLong(1);
            }
        },resultList);
    }


}
