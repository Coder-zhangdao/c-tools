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

        super((rs, index) -> rs.getLong(1),resultList);
    }


}
