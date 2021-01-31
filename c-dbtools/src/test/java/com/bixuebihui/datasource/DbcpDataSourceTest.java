package com.bixuebihui.datasource;

import java.sql.SQLException;

import com.bixuebihui.dbcon.DatabaseConfig;
import junit.framework.TestCase;

public class DbcpDataSourceTest extends TestCase {

	public void testGetConnection() throws SQLException {
		DbcpDataSource ds = new DbcpDataSource();
		DatabaseConfig cfg = DataSourceTest.getConfigMaster();

		ds.setDriverClassName(cfg.getClassName());
		ds.setUrl(cfg.getDburl());
		ds.setUsername(cfg.getUsername());
		ds.setPassword(cfg.getPassword());
		DataSourceTest.dataSourceTest(ds);

	}

	public void testSetDatabaseConfig() throws SQLException {
		DbcpDataSource ds = new DbcpDataSource();

		ds.setDatabaseConfig(DataSourceTest.getConfigMaster());

		DataSourceTest.dataSourceTest(ds);
	}
}
