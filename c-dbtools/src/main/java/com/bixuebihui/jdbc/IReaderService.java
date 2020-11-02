package com.bixuebihui.jdbc;

import java.sql.SQLException;
import java.util.Collection;

/**
 * 配合使用完成分页读取数据库表
 *
 * @author xingwx
 * @param <T>  返回类型
 * @version $Id: $Id
 */
public interface IReaderService<T> {

    /**
     * <p>select.</p>
     *
     * @param whereClause a {@link java.lang.String} object.
     * @param orderBy     a {@link java.lang.String} object.
     * @param rowStart    a int.
     * @param rowEnd      a int.
     * @return a {@link java.util.Collection} object.
     * @throws java.sql.SQLException if any.
     */
    Collection<T> select(String whereClause, String orderBy, int rowStart, int rowEnd) throws SQLException;

    /**
     * <p>count.</p>
     *
     * @param whereClause a {@link java.lang.String} object.
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    int count(String whereClause) throws SQLException;

}
