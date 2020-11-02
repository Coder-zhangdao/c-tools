package com.bixuebihui.jdbc;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

public class SqlFilterTest extends TestCase {

	public void testToString() {
		SqlFilter filter = new SqlFilter();
		assertEquals("", filter.toString());

		filter.addFilter("name", "abc");

		assertEquals("where name like 'abc%'", filter.toString());
	}

	public void testAddFilter() {
		SqlFilter filter = new SqlFilter();
		assertEquals("", filter.toString());

		filter.addFilter("name", "abc");
		filter.addFilter("cnt", 100);

		assertEquals("where name like 'abc%' and cnt = 100", filter.toString());
	}

	public void testOr(){
		SqlFilter filter = new SqlFilter();
		filter.addFilter("name", "abc");
		SqlFilter cond = new SqlFilter();
		cond.addFilter("name", "def");
		SqlFilter cond1 = new SqlFilter();
		cond1.addFilter("name", "dff");
		cond.or(cond1);
		SqlFilter res = filter.or(cond);

		System.out.println(res.toString());

	}


	public void testAddFilters(){
		SqlFilter sf = new 	SqlFilter();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("province", 1L);
		condition.put("city", 12L);
		condition.put("count", null);

		sf.addFilters(condition);
		assertEquals("where province = 1 and city = 12", sf.toString());
		System.out.println(sf.toString());

	}


	public void testInjection(){
		System.out.println(SqlFilter.transactSQLInjection("flksdfjlk ' and 1=1 --"));
		System.out.println(SqlFilter.transactSQLInjection("flksdfjlk sfs"));
	}
}
