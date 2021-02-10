package com.bixuebihui.jdbc;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlFilterTest {

	@Test
	public void testToString() {
		SqlFilter filter = new SqlFilter();
		assertEquals("", filter.toString());

		filter.addFilter("name", "abc");

		assertEquals("where name like 'abc%'", filter.toString());
	}

	@Test
	public void testAddFilter() {
		SqlFilter filter = new SqlFilter();
		assertEquals("", filter.toString());

		filter.addFilter("name", "abc");
		filter.addFilter("cnt", 100);

		assertEquals("where name like 'abc%' and cnt = 100", filter.toString());
	}

	@Test
	public void testOr(){
		SqlFilter filter = new SqlFilter();
		filter.addFilter("name", "abc");
		SqlFilter cond = new SqlFilter();
		cond.addFilter("name", "def");
		SqlFilter cond1 = new SqlFilter();
		cond1.addFilter("name", "dff");
		cond.or(cond1);
		SqlFilter res = filter.or(cond);

		assertEquals("where( name like 'abc%') or (( name like 'def%') or ( name like 'dff%'))",
				res.toString());

	}


	@Test
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



	@Test
	public void testInjection(){
		assertEquals("", SqlFilter.transactSQLInjection("flksdfjlk ' and 1=1 --"));
		assertEquals("flksdfjlk sfs",SqlFilter.transactSQLInjection("flksdfjlk sfs"));
	}

    @Test
    void transactSQLInjection() {
		String res = SqlFilter.transactSQLInjection("';--");
		assertEquals("", res);
    }
}
