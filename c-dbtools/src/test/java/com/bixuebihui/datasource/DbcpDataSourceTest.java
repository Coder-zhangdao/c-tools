package com.bixuebihui.datasource;

import java.sql.SQLException;

import junit.framework.TestCase;

public class DbcpDataSourceTest extends TestCase {

	public void testGetConnection() throws SQLException {
		DbcpDataSource ds = new DbcpDataSource();

		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost/test?autoReconnect=true&zeroDateTimeBehavior=convertToNull&noAccessToProcedureBodies=true&useUnicode=true&characterEncoding=utf8&mysqlEncoding=utf8&useSSL=false");
		ds.setUsername("test");
		ds.setPassword("test123");
		DataSourceTest.dataSourceTest(ds);

	}

	public void testSetDatabaseConfig() throws SQLException {
		DbcpDataSource ds = new DbcpDataSource();

		ds.setDatabaseConfig(DataSourceTest.getConfigMysqlMaster());

		DataSourceTest.dataSourceTest(ds);
	}
}
