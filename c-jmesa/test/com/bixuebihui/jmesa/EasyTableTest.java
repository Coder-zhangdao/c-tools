package com.bixuebihui.jmesa;

import com.bixuebihui.BeanFactory;
import com.bixuebihui.jdbc.IDbHelper;
import org.hamcrest.collection.ArrayAsIterableMatcher;
import org.hamcrest.collection.ArrayMatching;
import org.hamcrest.core.StringContains;
import org.hamcrest.core.StringStartsWith;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class EasyTableTest {

    IDbHelper dbHelper = (IDbHelper) BeanFactory.createObjectById("dbHelper");

    @Test
    public void testGetColNames() {
        EasyTable et = new EasyTable(dbHelper, null, "select count(*) cnt, myc as c from b limit 3");
        String[] cols = et.getColNames();

        assertEquals("cnt", cols[0]);
        assertEquals("c", cols[1]);

        String sql = "select count(id) CNT, state,  case state when 2 then '审核未通过' when 1 then '已审核' when 0 then '未审核'  else 'unknow' end as 状态 from Zoning_Plot group by state";
        et = new EasyTable(dbHelper, null, sql);

        cols = et.getColNames();

        //TODO find where toLowerCase is used in SQLParser
        assertEquals("CNT".toLowerCase(), cols[0]);
        assertEquals("STATE".toLowerCase(), cols[1]);
        assertEquals("状态", cols[2]);


    }

    @Test
    public void testEasyTableIDbHelperStringString() {
        EasyTable et = new EasyTable(dbHelper, null, "select count(*) cnt, c from b limit 3");

        assertEquals("b", et.getId());
        System.out.println(et.getColsList());
        for (String colName : et.getColNames())
            System.out.println("    " + colName);
        System.out.println("labels:");
        for (String colName : et.msp.colLabels)
            System.out.println("=== " + colName);


        String sql = "select count(*) 用户数, min(LastLoginDate) 活跃日期  from `User` where LastLoginDate > '2014-03-01'";
        et = new EasyTable(dbHelper, "用户数", sql);

        assertEquals("User", et.getId());
        System.out.println(et.getColsList());
        assertThat("must be 用户数,活跃日期", et.getColNames(),
                ArrayMatching.arrayContainingInAnyOrder("用户数","活跃日期") );
        for (String colName : et.getColNames())
            System.out.println("    " + colName);
        System.out.println("labels:");
        for (String colName : et.msp.colLabels)
            System.out.println("=== " + colName);


    }

    @Test
    public void testEasyTableForStat() throws SQLException {
        EasyTable et = new EasyTable(dbHelper, "站内系统私信", "select count(c_key)  数量 from t_config where c_key =0");

        assertEquals("t_config", et.getId());
        System.out.println(et.getColsList());
      //  assertEquals("数量", et.getUniquePropertyName());
        for (String colName : et.getColNames())
            System.out.println("    " + colName);
        System.out.println("labels:");
        for (String colName : et.msp.colLabels)
            System.out.println("=== " + colName);

        et.setUseSimpleView(true);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String res = et.handleRequestInternal(request, response);
        assertEquals(null, res);
        res = (String) request.getAttribute(et.getId());
        assertThat("must contain id: t_config_column_数量_1", res,StringContains.containsString("t_config_column_数量_1"));
    }


    @Test
    public void testEasyTableIDbHelperStringStringString() throws SQLException {
        String tableName = "t_config";
        String pkName = "c_key";
        String baseSql = " select c_key, c_name , c_value from t_config";
        EasyTable et = new EasyTable(dbHelper, tableName, baseSql, pkName, null);


        String[] cols = et.getColNames();

        assertEquals("C_KEY".toLowerCase(), cols[0]);
        assertEquals("C_NAME".toLowerCase(), cols[1]);
        assertEquals("C_VALUE".toLowerCase(), cols[2]);

        assertEquals("t_config", et.getId());

        assertEquals("c_key", et.getUniquePropertyName());


    }

    @Test
    public void testRender() throws Exception {
        String tableName = "t_config";
        String pkName = "c_key";
        String baseSql = " select c_key, c_name , c_value from t_config";
        EasyTable et = new EasyTable(dbHelper, tableName, baseSql, pkName, tableName);

        HttpServletRequest request = new MockHttpServletRequest();

        HttpServletResponse response = new MockHttpServletResponse();

        String res = et.render(request, response);

        assertThat("must contains", res, StringContains.containsString("t_config_row1"));
        //System.out.println(res);
    }

    @Test
    public void testRenderPager() throws Exception {
        String tableName = "t_config";
        String pkName = "c_key";
        String baseSql = " select c_key, c_name , c_value from t_config";
        EasyTable et = new EasyTable(dbHelper, tableName, baseSql, pkName, null);
        //int maxRows=50;
        MockHttpServletRequest request = new MockHttpServletRequest();
        //request.setParameter("maxRows",maxRows+"");
        //request.setParameter("ac","max_rows");
        //request.setParameter(tableName+"_mr_", maxRows+"");

        HttpServletResponse response = new MockHttpServletResponse();

        String res = et.render(request, response);

        assertTrue(res.contains("t_config_row1"));

        System.out.println(res);
    }

    @Test
    public void testRenderAuto() throws Exception {


        String baseSql = " select c_key, c_name , c_value from t_config";
        EasyTable et = new EasyTable(dbHelper, null, baseSql);

        HttpServletRequest request = new MockHttpServletRequest();

        HttpServletResponse response = new MockHttpServletResponse();

        String res = et.render(request, response);

        System.out.println(res);
    }

}
