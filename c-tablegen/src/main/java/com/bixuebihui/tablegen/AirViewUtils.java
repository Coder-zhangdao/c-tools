package com.bixuebihui.tablegen;

import com.bixuebihui.jdbc.BaseDao;
import com.bixuebihui.tablegen.entry.ColumnData;
import com.bixuebihui.tablegen.entry.TableInfo;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.ResultSetMetaData.columnNullable;

/**
 * Suitable to use when view is not available, for example the DBA not allowed to create view for some reasons.
 * So we use plain sql for ORM
 */
public class AirViewUtils {

    public static TableInfo getColumnData(ResultSetMetaData rs, String tableName, int dbtype) throws SQLException {
        List<ColumnData> colData = new ArrayList<>();

        ColumnData cd;
        int colType;
        int colCols;
        int decimalDigits;
        boolean isNullable;
        boolean isAuto_increment;
        String remarks;

        try {
            for (int i=1; i<=rs.getColumnCount(); i++) {

                String colName = rs.getColumnName(i);

                //oracle system columns
                if (colName.contains("#"))
                {
                    continue;
                }

                boolean existsCol = false;
                for (ColumnData dt : colData) {
                    if (dt.getName().equalsIgnoreCase(colName)) {
                        existsCol = true;
                        break;
                    }
                }
                if (existsCol) {
                    continue;
                }

                // 17
                isNullable = rs.isNullable(i) == columnNullable;

                //Access driver have not a column named IS_AUTOINCREMENT
                // 23
                isAuto_increment = (dbtype != BaseDao.ACCESS) && rs.isAutoIncrement(i);

                decimalDigits = rs.getPrecision(i);//.getString("DECIMAL_DIGITS");

                // REMARKS String => 描述列的注释（可为 null）
                remarks = rs.getColumnLabel(i) ; //co.getString("REMARKS");

                //COLUMN_DEF String => 该列的默认值，当值在单引号内时应被解释为一个字符串（可为 null）

                // column type (XOPEN values)
                colType = rs.getColumnType(i);// .getInt(5);
                // size e.g. varchar(20)
                colCols = rs.getColumnDisplaySize(i);//rs.getInt(7);
                cd = new ColumnData(colName, colType, colCols, isNullable,
                        isAuto_increment, decimalDigits, null, remarks);
                colData.add(cd);
            }
        } catch (SQLException e) {
            System.err.println("Table name:" );

            for (int i = 1; i <= rs.getColumnCount(); i++) {
                System.err.println(i + " : " +rs.getSchemaName(i) + rs.getTableName(i) + rs.getColumnName(i) + " -> " + rs.getColumnLabel(i));
            }
            throw e;
        }
        TableInfo tableInfo = new TableInfo(tableName);
        tableInfo.setFields(colData);

        return tableInfo;

    }

}
