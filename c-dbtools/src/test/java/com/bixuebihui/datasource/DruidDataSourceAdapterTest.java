package com.bixuebihui.datasource;

import java.sql.SQLException;

import com.bixuebihui.dbcon.DatabaseConfig;
import org.junit.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import static org.junit.Assert.assertEquals;

public class DruidDataSourceAdapterTest {

	@Test
	@DisabledIf("!com.bixuebihui.datasource.DataSourceTest.isMysqlAvailable()")
	public void testGetConnection() throws SQLException {
		DruidDataSourceAdapter ds = new DruidDataSourceAdapter();

		DatabaseConfig cfg = DataSourceTest.getConfigMaster();

		ds.setDriverClassName(cfg.getClassName());
		ds.setUrl(cfg.getDburl());
		ds.setUsername(cfg.getUsername());
		ds.setPassword(cfg.getPassword());
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
	@DisabledIf("!com.bixuebihui.datasource.DataSourceTest.isMysqlAvailable()")
	public void testSetDatabaseConfig() throws SQLException {
		DruidDataSourceAdapter ds = new DruidDataSourceAdapter();

		ds.setDatabaseConfig(DataSourceTest.getConfigMaster());

		DataSourceTest.dataSourceTest(ds);
		ds.close();

	}


	@Test
	@DisabledIf("!com.bixuebihui.datasource.DataSourceTest.isMysqlAvailable()")
	public void testDbHelper() throws SQLException {
		DruidDataSourceAdapter ds = new DruidDataSourceAdapter();

		ds.setDatabaseConfig(DataSourceTest.getConfigMaster());

		DataSourceTest.testDbHelper(ds);
		ds.close();

	}

}
