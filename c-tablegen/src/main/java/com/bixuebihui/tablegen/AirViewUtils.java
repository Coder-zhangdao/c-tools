package com.bixuebihui.tablegen;

import com.bixuebihui.jdbc.BaseDao;
import com.bixuebihui.tablegen.entry.ColumnData;
import com.bixuebihui.tablegen.entry.TableInfo;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        int decimalDigits = 0;
        boolean isNullable;
        boolean isAuto_increment;
        String defaultValue;
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
                defaultValue = null;//rs.getString("COLUMN_DEF");

                // column type (XOPEN values)
                colType = rs.getColumnType(i);// .getInt(5);
                // size e.g. varchar(20)
                colCols = rs.getColumnDisplaySize(i);//rs.getInt(7);
                cd = new ColumnData(colName, colType, colCols, isNullable,
                        isAuto_increment, decimalDigits, defaultValue, remarks);
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
//
//
//    /**
//     * <p>resultSet2Vector.</p>
//     *
//     * @param rs a {@link java.sql.ResultSet} object.
//     * @return a {@link java.util.List} object.
//     * @throws java.sql.SQLException if any.
//     */
//    public @NotNull
//    List<Map<String, Object>> resultSet2Vector(ResultSet rs) throws SQLException {
//        List<Map<String, Object>> result = new ArrayList<>();
//        //System.out.println("No data"); //http://stackoverflow.com/a/6813771/1484621
//        if (!rs.isBeforeFirst() ) {
//            return result;
//        }
//
//        ResultSetMetaData metaData = rs.getMetaData();
//        int iColumnNumber = metaData.getColumnCount();
//
//        while (rs.next()) {
//            Map<String, Object> mapColumn = new HashMap<>(32);
//            for (int i = 1; i <= iColumnNumber; i++) {
//
//                String colTypeName = metaData.getColumnTypeName(i).toUpperCase();
//
//                String colName = metaData.getColumnName(i);
//
//                if(!org.apache.commons.lang3.StringUtils.isBlank(metaData.getColumnLabel(i))) {
//                    colName = metaData.getColumnLabel(i);
//                }
//
//                if(mapColumn.containsKey(colName) && rs.getObject(i)==null){
//                    //同一列名的已存在且不为空，则保留当前值，用于多表联合查询，但后表无记录的情况
//                    continue;
//                }
//
//                switch (colTypeName) {
//                    case "NUMBER":
//                    case "INT":
//                    case "INTEGER":
//                    case "TINYINT":
//                    case "TINYINT UNSIGNED":
//                    case "INTEGER IDENTITY":
//                    case "COUNTER":
//                    case "NUMERIC":
//                    case "DECIMAL":
//                    case "MEDIUMINT":
//                    case "INT UNSIGNED":
//                    case "MEDIUMINT UNSIGNED":
//
//                        dealNumber(rs, metaData, mapColumn, i, colTypeName, colName);
//
//                        break;
//                    case "BIGINT":
//                    case "BIGINT UNSIGNED":
//                        mapColumn.put(colName,
//                                rs.getLong(i));
//                        break;
//                    case "SMALLINT":
//                        mapColumn.put(colName,
//                                rs.getInt(i));
//                        break;
//                    case "VARCHAR2":
//                    case "VARCHAR":
//                    case "NVARCHAR2":
//                    case "NVARCHAR":
//                    case "TEXT":
//                    case "MEDIUMTEXT":
//                    case "LONGTEXT":
//                    case "CHAR":
//                    case "NTEXT":
//                    case "NCHAR":
//                    case "LONGVARCHAR":
//                    case "LONGCHAR": {
//                        String temp = rs.getString(i);
//                        if (temp == null) {
//                            temp = "";
//                        }
//                        mapColumn.put(colName, temp);
//                        break;
//                    }
//                    case "DATETIME":
//                    case "TIMESTAMP":
//                        // 10.17
//                        if (rs.getTimestamp(i) != null) {
//                            mapColumn.put(colName, rs.getTimestamp(i));
//                        } else {
//                            // 1970 year
//                            Timestamp d1970 = new Timestamp(0);
//                            mapColumn.put(colName, d1970);
//                        }
//                        break;
//                    case "DATE":
//                        java.util.Date dt = rs.getDate(i);
//                        if (dt != null) {
//                            Timestamp tm = new Timestamp(
//                                    dt.getTime());
//                            mapColumn.put(colName, tm);
//                        } else {
//                            Timestamp d1970 = new Timestamp(0);
//                            mapColumn.put(colName, d1970);
//                        }
//
//                        break;
//                    case "LONG": {
//                        String temp = "";
//                        byte[] mybyte = rs.getBytes(i);
//                        if (mybyte != null) {
//                            temp = new String(mybyte, Charset.defaultCharset());
//                        }
//                        mapColumn.put(colName, temp);
//                        break;
//                    }
//                    case "RAW":
//                    case "VARCHAR () FOR BIT DATA":
//                        byte[] bb = rs.getBytes(i);
//                        mapColumn.put(colName, bb == null ? new byte[0]
//                                : bb);
//                        break;
//                    case "CLOB": {
//                        String temp = null;
//                        try {
//                            temp = clob2Str(rs.getClob(i));
//                        } catch (IOException e) {
//                            LOG.error(e);
//                        }
//                        mapColumn.put(colName, temp);
//                        break;
//                    }
//                    case "BOOL":
//                    case "BIT": {
//                        Boolean temp = rs.getBoolean(i);
//                        mapColumn.put(colName, temp);
//                        break;
//                    }
//                    case "FLOAT": {
//                        Float temp = rs.getFloat(i);
//                        mapColumn.put(colName, temp);
//                        break;
//                    }
//                    case "DOUBLE": {
//                        Double temp = rs.getDouble(i);
//                        mapColumn.put(colName, temp);
//                        break;
//                    }
//                    default:
//                        mapColumn.put(colName, rs.getObject(i));
//                        String string = " -colName:";
//                        LOG.warn(
//                                "Unknow data type, ask xwx@live.cn for adding this to DbHelper.resultSet2Vector colTypeName: "
//                                        + colTypeName + string + colName);
//                        break;
//                }
//            }
//            result.add(mapColumn);
//        }
//        return result;
//    }
//
//    private void dealNumber(ResultSet rset, ResultSetMetaData rsmdQuery, Map<String, Object> columnMap, int i, String colTypeName, String colName) throws SQLException {
//        if("TINYINT".equals(colTypeName) && rsmdQuery.getPrecision(i)==1) {
//            columnMap.put(colName,	rset.getBoolean(i));
//        } else if("TINYINT".equals(colTypeName) || "TINYINT UNSIGNED".equals(colTypeName)) {
//            columnMap.put(colName,	rset.getByte(i));
//        } else if (rsmdQuery.getScale(i) <= 0) {
//            // BigDecimal or Integer
//            if (rset.getObject(i) instanceof BigDecimal) {
//                BigDecimal a = (BigDecimal) rset.getObject(i);
//                if (a.scale() > 0
//                        || a.compareTo(new BigDecimal(
//                        Integer.MAX_VALUE)) > 0) {
//                    columnMap.put(colName,
//                            rset.getObject(i));
//                } else {
//                    columnMap.put(colName,
//                            rset.getInt(i));
//                }
//            }else if (rsmdQuery.getPrecision(i)>9){
//                columnMap.put(colName,
//                        rset.getLong(i));
//            }else {
//                columnMap.put(colName,
//                        rset.getInt(i));
//            }
//        } else {
//            columnMap.put(colName,
//                    rset.getDouble(i));
//
//        }
//    }
}
