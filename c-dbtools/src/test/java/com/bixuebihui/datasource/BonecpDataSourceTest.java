package com.bixuebihui.datasource;

import java.sql.SQLException;

import com.bixuebihui.dbcon.DatabaseConfig;

import junit.framework.TestCase;


public class BonecpDataSourceTest extends TestCase {

	public void testGetConnection() throws SQLException {
		BonecpDataSource ds = new BonecpDataSource();

		DatabaseConfig cfg = DataSourceTest.getConfig();
		ds.setDriverClass(cfg.getClassName());
		ds.setJdbcUrl(cfg.getDburl());
		ds.setUsername(cfg.getUsername());
		ds.setPassword(cfg.getPassword());
		DataSourceTest.dataSourceTest(ds);

	}

	public void testSetDatabaseConfig() throws SQLException {
		BonecpDataSource ds = new BonecpDataSource();

		ds.setDatabaseConfig(DataSourceTest.getConfig());

		DataSourceTest.dataSourceTest(ds);
	}
}
