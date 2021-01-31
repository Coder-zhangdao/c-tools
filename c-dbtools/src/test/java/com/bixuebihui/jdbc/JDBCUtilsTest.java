package com.bixuebihui.jdbc;

import java.sql.*;
import java.util.Locale;

import com.bixuebihui.datasource.DataSourceTest;
import com.bixuebihui.datasource.DbcpDataSource;

import com.bixuebihui.dbcon.DatabaseConfig;
import org.junit.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JDBCUtilsTest {

    @Test
	@DisabledIf("!com.bixuebihui.datasource.DataSourceTest.isMysqlAvailable()")
    public void testTableOrViewExists() throws SQLException {
		String tableName = "t_log";

		DbcpDataSource ds = new DbcpDataSource();
		DatabaseConfig cfg = DataSourceTest.getConfigMaster();
		ds.setDatabaseConfig(cfg);
		IDbHelper db = new DbHelper();
		db.setDataSource(ds);

		String schema=null;
		String catalog=null;
		if(cfg.getClassName().contains("h2")){
			tableName= tableName.toUpperCase(Locale.ROOT);
		}


		assertTrue(JDBCUtils.tableOrViewExists(catalog, schema, tableName, db
				.getConnection()));
	}

	@Test
	@DisabledIf("!com.bixuebihui.datasource.DataSourceTest.isMysqlAvailable()")
	public void testColumnOfTableExists() throws SQLException {
		if(!com.bixuebihui.datasource.DataSourceTest.isMysqlAvailable()) {
			return;
		}
		String tableName = "t_log";
		String columnName = "lid";

		DbcpDataSource ds = new DbcpDataSource();
		ds.setDatabaseConfig(DataSourceTest.getConfigMaster());
		IDbHelper db = new DbHelper();
		db.setDataSource(ds);

		assertTrue(JDBCUtils.columnOfTableExists(null, null, tableName,
				columnName, db.getConnection()));
		assertFalse(JDBCUtils.columnOfTableExists(null, null, tableName, "NO",
				db.getConnection()));
	}

}
