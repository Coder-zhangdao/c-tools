package com.bixuebihui.jmesa;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.bixuebihui.BeanFactory;
import com.bixuebihui.jdbc.IDbHelper;

import junit.framework.TestCase;

public class EasyTableTest extends TestCase {

	public void testGetColNames() {
		EasyTable et = new EasyTable(dbHelper , null, "select count(*) cnt, myc as c from b limit 3");
		String[] cols = et.getColNames();

		assertEquals("CNT", cols[0]);
		assertEquals("C",cols[1]);

		String sql = "select count(id) CNT, state,  case state when 2 then '审核未通过' when 1 then '已审核' when 0 then '未审核'  else 'unknow' end as 状态 from Zoning_Plot group by state";
		 et = new EasyTable(dbHelper , null,  sql);

		  cols = et.getColNames();

			assertEquals("CNT", cols[0]);
			assertEquals("STATE",cols[1]);
			assertEquals("状态",cols[2]);


	}

	IDbHelper dbHelper = (IDbHelper)BeanFactory.createObjectById("dbHelper");

	public void testEasyTableIDbHelperStringString() {
		EasyTable et = new EasyTable(dbHelper ,null, "select count(*) cnt, c from b limit 3");

		assertEquals("b",et.getId());
		System.out.println(et.getColsList());
		assertEquals("CNT",et.getUniquePropertyName());
		for(String colName: et.getColNames())
			System.out.println("    "+colName);
		System.out.println("labels:");
		for(String colName: et.msp.colLabels)
			System.out.println("=== "+colName);



		String sql = "select count(*) 用户数, min(LastLoginDate) 活跃日期  from `User` where LastLoginDate > '2014-03-01'";
		et = new EasyTable(dbHelper ,"用户数",sql);

		assertEquals("User",et.getId());
		System.out.println(et.getColsList());
		assertEquals("用户数",et.getUniquePropertyName());
		for(String colName: et.getColNames())
			System.out.println("    "+colName);
		System.out.println("labels:");
		for(String colName: et.msp.colLabels)
			System.out.println("=== "+colName);



	}

	public void testEasyTableForStat() throws SQLException {
		EasyTable et  = new EasyTable(dbHelper ,"站内系统私信","select count(id)  数量 from Words where PostBy =0");

		assertEquals("words",et.getId());
		System.out.println(et.getColsList());
		assertEquals("数量",et.getUniquePropertyName());
		for(String colName: et.getColNames())
			System.out.println("    "+colName);
		System.out.println("labels:");
		for(String colName: et.msp.colLabels)
			System.out.println("=== "+colName);

		et.setUseSimpleView(true);
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		String res = et.handleRequestInternal(request , response);
		assertEquals(null, res);
		res = (String) request.getAttribute(et.getId());
		assertTrue(res.startsWith("<div class=\"jmesa\" >\n" +
				"<table id=\"words\"  border=\"0\"  cellpadding=\"0\"  cellspacing=\"0\"  class=\"table\" ><caption>站内系统私信</caption>\n" +
				"	<thead>\n" +
				"	<tr class=\"header\" >\n" +
				"		<td><div onmouseover=\"this.style.cursor='pointer'\"  onmouseout=\"this.style.cursor='default'\"  onclick=\"jQuery.jmesa.setSort('words','数量','0','asc');onInvokeAction('words', 'sort')\" >数量</div><input type=\"hidden\"  name=\"words_s_0_数量\" /></td>\n" +
				"	</tr>\n" +
				"	</thead>\n" +
				"	<tbody class=\"tbody\" >\n" +
				"	<tr id=\"words_row1\"  class=\"odd\"  onmouseover=\"this.className='highlight'\"  onmouseout=\"this.className='odd'\" >\n"));
	}



	public void testEasyTableIDbHelperStringStringString() throws SQLException {
		String tableName = "t_config";
		String pkName ="c_key";
		String baseSql = " select c_key, c_name , c_value from t_config";
		EasyTable et = new EasyTable( dbHelper,  tableName , pkName ,  baseSql );


		String[] cols = et.getColNames();

		assertEquals("C_KEY", cols[0]);
		assertEquals("C_NAME",cols[1]);
		assertEquals("C_VALUE",cols[2]);

		assertEquals("t_config",et.getId());

		assertEquals("c_key",et.getUniquePropertyName());


	}

	public void testRender() throws Exception {
		String tableName = "t_config";
		String pkName ="c_key";
		String baseSql = " select c_key, c_name , c_value from t_config";
		EasyTable et = new EasyTable( dbHelper,  tableName , pkName ,  baseSql );

		HttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response =  new MockHttpServletResponse();

		String res = et.render(request , response );

		assertTrue(res.contains("t_config_row1"));
		//System.out.println(res);
	}

	public void testRenderPager() throws Exception {
		String tableName = "t_config";
		String pkName ="c_key";
		String baseSql = " select c_key, c_name , c_value from t_config";
		EasyTable et = new EasyTable( dbHelper,  tableName , pkName ,  baseSql );
		//int maxRows=50;
		MockHttpServletRequest request = new MockHttpServletRequest();
		//request.setParameter("maxRows",maxRows+"");
		//request.setParameter("ac","max_rows");
		//request.setParameter(tableName+"_mr_", maxRows+"");

		HttpServletResponse response =  new MockHttpServletResponse();

		String res = et.render(request , response );

		assertTrue(res.contains("t_config_row1"));

		System.out.println(res);
	}

	public void testRenderAuto() throws Exception {


		String baseSql = " select c_key, c_name , c_value from t_config";
		EasyTable et = new EasyTable( dbHelper, null,  baseSql );

		HttpServletRequest request = new MockHttpServletRequest();

		HttpServletResponse response =  new MockHttpServletResponse();

		String res = et.render(request , response );

		System.out.println(res);
	}

}
