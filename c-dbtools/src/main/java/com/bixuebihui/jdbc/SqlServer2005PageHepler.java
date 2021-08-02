package com.bixuebihui.jdbc;


import com.bixuebihui.DbException;

import java.sql.SQLException;

/**
 * SQL SERVER 2005 分页辅助类.
 *
 * @author 黄明杰
 * @version 0.2
 * <p>注意：</p>
 *
 * 不支持"UNION" 和“UNION ALL” 这样的结果合并语句，例如：
 * Sql代码
 *
 * 1. select * from dbo.[user]
 * 2. union
 * 3. select top 1 * from dbo.[user] order by age
 *
 * select * from dbo.[user]
 * union
 * select top 1 * from dbo.[user] order by age
 *
 * 主要原因是 UNION  这样的语句没有共同的order by 条件，所以不想支持，如果你需要得到正确的结果你需要这样写，如下：
 * Sql代码
 *
 * 1. select * from (
 * 2.     select * from dbo.[user]
 * 3.     union
 * 4.     select top 1 * from dbo.[user] order by age
 * 5. ) Q order by id
 *
 * select * from (
 * select * from dbo.[user]
 * union
 * select top 1 * from dbo.[user] order by age
 * ) Q order by id
 */
public class SqlServer2005PageHepler extends SqlServer2000PageHepler {

    private SqlServer2005PageHepler() throws IllegalAccessException {
        super();
    }


    /**
     * 得到分页的SQL
     *
     * @param limit       count of records to return
     * @param offset      offset from start
     * @param querySelect select clause
     * @return select clause with pagination
     * {@link SqlServer2000PageHepler#getLimitString }
     */
    public static String getLimitString(String querySelect, int offset, int limit) {

        try {
            querySelect = getLineText(querySelect);
            int orderIndex = getLastOrderInsertPoint(querySelect);

            return "select * from (select *,ROW_NUMBER() OVER (" +
                    querySelect.substring(orderIndex).replaceAll("[^\\s,]+\\.", "") +
                    ") _row_num from (" +
                    querySelect.substring(0, orderIndex) + ") _t" +
                    ") _t where _t._row_num > " +
                    (offset == -1 ? "?" : offset) +
                    " and _t._row_num <=  " +
                    (limit == -1 ? "?" : limit);
        } catch (SQLException ex) {
            throw new DbException(ex);
        }
    }


    /**
     * 得到最后一个order By的插入点位置
     * @param querySelect  sql语句
     * @return 返回最后一个order By插入点的位置
     * @throws SQLException 如果不存在正确的order by
     */
    private static int getLastOrderInsertPoint(String querySelect) throws SQLException {
        int orderIndex = querySelect.toLowerCase().lastIndexOf("order by");
        if (orderIndex == -1
                || !isBracketCanPartnership(querySelect.substring(orderIndex))) {
            throw new SQLException("SQL 2005 分页必须要有Order by 语句!");
        }
        return orderIndex;
    }


}
