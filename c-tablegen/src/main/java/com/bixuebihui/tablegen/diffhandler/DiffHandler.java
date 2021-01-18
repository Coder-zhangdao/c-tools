package com.bixuebihui.tablegen.diffhandler;

import com.bixuebihui.tablegen.ColumnData;

import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-28
 * Time: 下午3:27
 * To change this template use File | Settings | File Templates.
 * 数据库表的比对
 */
public interface DiffHandler {


    /**
     * 表不一致的处理
     * @param tableName
     */
    void processTableDiff(String tableName);

    /**
     * 比对结束的处理
     */
    void processEnd(HashMap<String, List<ColumnData>> tableData);
}
