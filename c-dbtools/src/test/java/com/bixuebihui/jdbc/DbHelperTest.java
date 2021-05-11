package com.bixuebihui.jdbc;

import com.bixuebihui.datasource.BitmechanicDataSource;
import com.bixuebihui.datasource.DataSourceTest;
import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class DbHelperTest extends TestCase {


    DbHelper h = new DbHelper();

    public void testExecuteNoQueryBatch2() throws Exception {

        String sql = "insert into t_log(lid, uid, content)values(?,?,?)";
        h.executeNoQuery("delete from t_log where lid<100000");
        int res = h.executeNoQueryBatch(sql, 100, index -> new Object[]{index, index, "test" + index}, null);

        long a = (long) h.executeScalar("select count(*) from t_log where lid<1000");
        assertTrue(a == 100);
    }

    public void testInsertAndFetchId() throws Exception {

        String sql = "insert into t_log( uid, content)values(?,?)";
        h.executeNoQuery("delete from t_log where lid<100000");
        long res = h.insertAndFetchLastId(sql, new Object[]{10000, "test10000" },null, null);
        assertTrue(res>1);
        System.out.println(res);

        long a = (long) h.executeScalar("select count(*) from t_log where lid<1000");
        assertTrue(a == 1);
    }

    public void testExecuteQuery1() throws Exception {
    }

    public void testExecuteScalar() throws Exception {
    }

    protected void setUp() {
        BitmechanicDataSource ds = new BitmechanicDataSource();
        ds.setDatabaseConfig(DataSourceTest.getConfigMaster());

        h.setDataSource(ds);
    }


    public void testGetConnection() throws SQLException {

        h.getConnection();
    }

    public void testFreeConnection() throws SQLException {
        Connection cn = h.getConnection();
        DbHelper.freeConnection(cn);

    }

    public void testDbHelperConnection() throws SQLException {
        Connection cn = h.getConnection();
        IDbHelper h1 = new DbHelper(cn);
        assertEquals(cn, h1.getConnection());

    }

    public void testClose() throws SQLException {
        Connection cn = h.getConnection();
        cn.close();
        h.close();

    }


    public void testExecuteScalarString() throws SQLException {
        String strSql = "select 1+2 from dual";
        String v = h.executeScalar(strSql).toString();
        assertEquals(v, "3");
    }

    public void testExecuteUpdate() {
    }

    public void testExeQuery() throws SQLException {
        String strSql = "select 1+2 from dual";
        List<Map<String, Object>> v = h.exeQuery(strSql);
        assertEquals(1, v.size());
    }


}
