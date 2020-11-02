package com.bixuebihui.jdbc;
/*
 * @(#)SqlServer2005PageHepler.java 0.2 08/09/20
 * Copyright 2009 Zonrong, Inc. All rights reserved.
 */

import java.sql.SQLException;

/**
 * SQL SERVER 2005 分页辅助类.
 *
 * @author 黄明杰
 * @version 0.2
 * <p>
 * <p>
 * <p>
 * 注意：
 * <p>
 * 不支持"UNION" 和“UNION ALL” 这样的结果合并语句，例如：
 * Sql代码
 * <p>
 * 1. select * from dbo.[user]
 * 2. union
 * 3. select top 1 * from dbo.[user] order by age
 * <p>
 * select * from dbo.[user]
 * union
 * select top 1 * from dbo.[user] order by age
 * <p>
 * 主要原因是 UNION  这样的语句没有共同的order by 条件，所以不想支持，如果你需要得到正确的结果你需要这样写，如下：
 * Sql代码
 * <p>
 * 1. select * from (
 * 2.     select * from dbo.[user]
 * 3.     union
 * 4.     select top 1 * from dbo.[user] order by age
 * 5. ) Q order by id
 * <p>
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
     *  {@link SqlServer2000PageHepler#getLimitString }
     */
    public static String getLimitString(String querySelect,int offset, int limit) throws SQLException {

        querySelect     = getLineText(querySelect);
        int orderIndex  = getLastOrderInsertPoint(querySelect);

        StringBuilder sb = new StringBuilder(querySelect.length())
            .append("select * from (select *,ROW_NUMBER() OVER (")
            .append(querySelect.substring(orderIndex).replaceAll("[^\\s,]+\\.", ""))
            .append(") _row_num from (")
            .append(querySelect.substring(0, orderIndex)).append(") _t")
            .append(") _t where _t._row_num > ")
            .append(offset == -1 ? "?" : offset)
            .append(" and _t._row_num <=  ")
            .append(limit  == -1 ? "?" : limit);

        return sb.toString();
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
                || !isBracketCanPartnership(querySelect.substring(orderIndex,querySelect.length()))) {
            throw new SQLException("SQL 2005 分页必须要有Order by 语句!");
        }
        return orderIndex;
    }


}
