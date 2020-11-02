package com.bixuebihui.jdbc;

import java.sql.*;

import org.apache.commons.dbutils.DbUtils;

import com.bixuebihui.datasource.BitmechanicDataSource;
import com.bixuebihui.datasource.DataSourceTest;
import com.bixuebihui.datasource.DbcpDataSource;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JDBCUtilsTest {

    @Test
    public void testTableOrViewExists() throws SQLException {
		String tableName = "t_log";

		DbcpDataSource ds = new DbcpDataSource();
		ds.setDatabaseConfig(DataSourceTest.getConfigMysqlMaster());
		IDbHelper db = new DbHelper();
		db.setDataSource(ds);

		assertTrue(JDBCUtils.tableOrViewExists(null, null, tableName, db
				.getConnection()));
	}

	@Test
	public void testColumnOfTableExists() throws SQLException {
		String tableName = "t_log";
		String columnName = "lid";

		DbcpDataSource ds = new DbcpDataSource();
		ds.setDatabaseConfig(DataSourceTest.getConfigMysqlMaster());
		IDbHelper db = new DbHelper();
		db.setDataSource(ds);

		assertTrue(JDBCUtils.columnOfTableExists(null, null, tableName,
				columnName, db.getConnection()));
		assertFalse(JDBCUtils.columnOfTableExists(null, null, tableName, "NO",
				db.getConnection()));
	}

}
