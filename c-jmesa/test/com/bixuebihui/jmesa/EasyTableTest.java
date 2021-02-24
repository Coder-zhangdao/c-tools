package com.bixuebihui.jmesa;

import com.bixuebihui.BeanFactory;
import com.bixuebihui.cache.DictionaryCache;
import com.bixuebihui.cache.DictionaryItem;
import com.bixuebihui.jdbc.IDbHelper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foundationdb.sql.StandardException;
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
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
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
    public void testEasyTableIDbHelperStringStringString() {
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
    public void testRender() {
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
    public void testRenderPager() {
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
    public void testRenderAuto() {


        String baseSql = " select c_key, c_name , c_value from t_config";
        EasyTable et = new EasyTable(dbHelper, null, baseSql);

        HttpServletRequest request = new MockHttpServletRequest();

        HttpServletResponse response = new MockHttpServletResponse();

        String res = et.render(request, response);

        System.out.println(res);
    }

    @Test
    public void testDictCache() {
        // https://docs.oracle.com/cd/E19798-01/821-1841/bnahu/index.html

        List<DictionaryItem> dict = new ArrayList<>();
        dict.add(new DictionaryItem("key","好", "1"));
        dict.add(new DictionaryItem("key1","很好", "2"));

        String myDict = "myDict";
        DictionaryCache.putInCache(myDict, dict);


        String baseSql = " select c_key, c_name , c_value from t_config";
        EasyTable et = new EasyTable(dbHelper, null, baseSql);

        Map<String, String> map = new HashMap<>();
        map.put("c_key", "dict use: ${fn:byId('"+myDict+".'.concat(row.c_key)).value}");
        et.setColsTemplate(map);
        HttpServletRequest request = new MockHttpServletRequest();

        HttpServletResponse response = new MockHttpServletResponse();

        String res = et.render(request, response);

        System.out.println(res);
    }


    @Test
    public void testParser() throws StandardException {
        EasyTable.MiniSqlParser mp = EasyTable.MiniSqlParser.parse("select count(*) from article");
        assertEquals(null, mp.uniquePropertyName);

        mp = EasyTable.MiniSqlParser.parse("select count(*) CNT from article");
        assertEquals("cnt", mp.uniquePropertyName);

    }

    @Test void testSetColNames(){
        EasyTable et = new EasyTable(dbHelper, "站内系统私信",
                "select * from t_config where c_key =0","a","表名",  "a,b,c" );
       assertEquals( "a,b,c", et.getColsList());

    }

    @Test
    public void testJson() throws SQLException {
        String tableName = "t_config";
        String baseSql = " select c_key, c_name , c_value from t_config";
        EasyTable et = new EasyTable(dbHelper, tableName, baseSql);

        String res = et.json("{\"exportType\":\"json\"}");

        assertThat("must contains", res, StringContains.containsString("t_config"));
        assertFalse("must not contains", res.contains("<td>"));
        //System.out.println(res);
    }


    @Test
    public void testJsonPaging() throws SQLException, JsonProcessingException {
        String tableName = "t_config";
        String baseSql = " select c_key, c_name , c_value from t_config";
        EasyTable et = new EasyTable(dbHelper, tableName, baseSql);

        int size=2;
        String res = et.json("{\"exportType\":\"json\", \"maxRows\":"+size+"}");
        ObjectMapper mapper = new ObjectMapper();
        Map map= mapper.readValue(res, Map.class);

        Object obj = map.get("data");
        assertEquals(size, ((List)obj).size());

        res = et.json("{\"exportType\":\"json\", \"maxRows\":1, \"page\":2}");
        Map map2= mapper.readValue(res, Map.class);

        Object obj2 = map2.get("data");
        assertEquals(1, ((List)obj2).size());

    }

}
