package com.bixuebihui.tablegen.diffhandler;

import com.bixuebihui.tablegen.entry.ColumnData;

import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-28
 * Time: 下午3:27
 * To change this template use File | Settings | File Templates.
 * 数据库表的比对
 * @author xwx
 */
public interface DiffHandler {


    /**
     * 表不一致的处理
     * @param table
     */
    void processTableDiff(String table) throws IOException;

    /**
     * 比对结束的处理
     * @param tableData
     */
    void processEnd(HashMap<String, List<ColumnData>> tableData);
}
