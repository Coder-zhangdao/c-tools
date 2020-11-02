package com.bixuebihui.jdbc;

import java.sql.SQLException;
import java.util.List;

/**
 * 用于多组结果合并操作
 *
 * @author xingwx
 * @param <T> 传入类型
 * @version $Id: $Id
 */
public interface ProcessHandler<T>
{
    /**
     * <p>process.</p>
     *
     * @param items a {@link java.util.List} object.
     * @throws java.sql.SQLException if any.
     */
    void process(List<T> items) throws SQLException;
}
