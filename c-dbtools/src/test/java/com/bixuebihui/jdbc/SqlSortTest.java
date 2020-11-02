package com.bixuebihui.jdbc;

import junit.framework.TestCase;

public class SqlSortTest extends TestCase {

	public void testAddSort() {
		SqlSort sort = new SqlSort();
		assertEquals("",sort.toString());
		sort.addSort("name", "asc");
		assertEquals(" order by name asc", sort.toString());
	}

	public void testToString() {
		SqlSort sort = new SqlSort();
		assertEquals("",sort.toString());
		sort.addSort("name", "asc");
		sort.addSort("cnt", "desc");
		assertEquals(" order by name asc,cnt desc", sort.toString());
	}

}
