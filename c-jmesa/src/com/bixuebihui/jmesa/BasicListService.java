package com.bixuebihui.jmesa;

import com.bixuebihui.jdbc.AbstractBaseDao;
import com.bixuebihui.jdbc.BaseDao;
import com.bixuebihui.jdbc.IDbHelper;
import com.bixuebihui.jdbc.SqlObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xwx
 */
public class BasicListService extends AbstractBaseDao {

    private SqlObject sqlObj = new SqlObject();


    public BasicListService(IDbHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public String getTableName() {
        return "(" + sqlObj.getSqlString() + ") basiclistservice_table_alias";
    }

    @Override
    public int count(String where) throws NumberFormatException, SQLException {
        if (this.getSqlParams() == null || this.getSqlParams().length == 0) {
            return super.count(where);
        }
        String sql = "select count(*) from " + this.getTableName() + "   " + where;
        return Integer.parseInt(this.getDbHelper().executeScalar(sql, sqlObj.getParameters()).toString());
    }


    @Override
    public List<Object> select(String whereClause, String orderBy, int rowStart, int rowEnd)
            throws SQLException {

        boolean needOrderBy = true;
        String tableName = getTableName();
        if (this.getDbType() == BaseDao.DERBY || tableName.toUpperCase().indexOf("ORDER BY") > 0) {
            needOrderBy = false;
        }

        String selectSql = "select * from " + tableName + " " + whereClause + " "
                + (needOrderBy ? orderBy : "");

        List<Map<String, Object>> v = getDbHelper().executeQuery(this.getPagingSql(selectSql, rowStart, rowEnd), sqlObj.getParameters());
        ArrayList<Object> list = new ArrayList<>();
        list.addAll(v);
        return list;

    }


    @Override
    public Map<String, Object> selectByIds(String arg0, List<String> arg1)
            throws SQLException {
        throw new SQLException("not implement!");
    }

    @Override
    public boolean updateByKey(Object arg0) throws SQLException {
        throw new SQLException("not implement!");
    }

    public void setCoreSql(String coreSql) {
        sqlObj.setSqlString(coreSql);
    }

    public Object[] getSqlParams() {
        return sqlObj.getParameters();
    }

    public void setSqlParams(Object[] sqlParameters) {
        sqlObj.setParameters(sqlParameters);
    }


}
