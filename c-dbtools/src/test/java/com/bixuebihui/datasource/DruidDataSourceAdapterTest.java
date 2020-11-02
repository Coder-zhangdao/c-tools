package com.bixuebihui.datasource;

import java.sql.SQLException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DruidDataSourceAdapterTest {

	@Test
	public void testGetConnection() throws SQLException {
		DruidDataSourceAdapter ds = new DruidDataSourceAdapter();

		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost/test?autoReconnect=true&zeroDateTimeBehavior=convertToNull&noAccessToProcedureBodies=true&useUnicode=true&characterEncoding=utf8&mysqlEncoding=utf8&useSSL=false");
		ds.setUsername("test");
		ds.setPassword("test123");
		DataSourceTest.dataSourceTest(ds);
		ds.close();

	}

	@Test
	public void testGetAlias() {
		DruidDataSourceAdapter ds = new DruidDataSourceAdapter();
		System.out.println(ds.getAlias());
		ds.close();
	}

	@Test
	public void testSetAlias() {
		DruidDataSourceAdapter ds = new DruidDataSourceAdapter();
		String name="druid-pool-test";
		ds.setAlias(name);
		assertEquals(name, ds.getAlias());
		ds.close();

	}

	@Test
	public void testSetDatabaseConfig() throws SQLException {
		DruidDataSourceAdapter ds = new DruidDataSourceAdapter();

		ds.setDatabaseConfig(DataSourceTest.getConfigMysqlMaster());

		DataSourceTest.dataSourceTest(ds);
		ds.close();

	}


	@Test
	public void testDbHelper() throws SQLException {
		DruidDataSourceAdapter ds = new DruidDataSourceAdapter();

		ds.setDatabaseConfig(DataSourceTest.getConfigMysqlMaster());

		DataSourceTest.testDbHelper(ds);
		ds.close();

	}

}
