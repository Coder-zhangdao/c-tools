package com.bixuebihui.datasource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bixuebihui.dbcon.DatabaseConfig;
import com.bixuebihui.jdbc.AbstractBaseDao;
import com.bixuebihui.jdbc.DbHelper;
import com.bixuebihui.jdbc.IDbHelper;
import com.bixuebihui.sequence.SequenceUtils;

import junit.framework.TestCase;

public class DataSourceTest extends TestCase {

	private static final Log log = LogFactory.getLog(DataSourceTest.class);

	// this is a superclass for test DataSource!
	public void testDummy() {
		assertEquals(true, true);
	}

	public static String getDummySql(Connection cn) throws SQLException {
		IDbHelper db = new DbHelper(cn);
		AbstractBaseDao mybd = new AbstractBaseDao(db);
		return mybd.getDummySql();

	}

	public static List<Long> getIds(DataSource ds, int count) throws SQLException {
		IDbHelper db = new DbHelper();
		db.setDataSource(ds);
		AbstractBaseDao mybd = new AbstractBaseDao(db) {
			@Override
			public String getTableName() {
				return "t_log";
			}
		};
		return mybd.ar().limit(0, count).getLongVector("lid");
	}

	public static void dataSourceTest(DataSource ds) throws SQLException {
		Date dtStart = new Date();

		String sql = getDummySql(ds.getConnection());

		for (int i = 0; i < 20; i++) {
			Connection cn = null;
			ResultSet rs = null;
			Statement stmt = null;
			try {
				cn = ds.getConnection();
				//log.info("cn = " + cn.toString());
				stmt = cn.createStatement();

				rs = stmt.executeQuery("select 1+" + i + " " + sql);

				if (rs.next()) {
					assertEquals(1 + i, rs.getInt(1));
				} else {
					log.info("dataSourceTest error select");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;

			} finally {

				DbUtils.closeQuietly(cn, stmt, rs);
			}
		}
		Date dtEnd = new Date();
		log.info(ds + " used time:" + (dtEnd.getTime() - dtStart.getTime()) / 1000.0);
	}



	public static void testDbHelper(DataSource ds){
		DbHelper dbHelper = new DbHelper();
		dbHelper.setDataSource(ds);

		String keyName="what_is_love";
		long v1 = SequenceUtils.getInstance().getNextKeyValue(keyName, dbHelper);
		long v2 = SequenceUtils.getInstance().getNextKeyValue(keyName, dbHelper);

		assertTrue(v1+1==v2);
	}


	public static void dataSourceTestCursor(DataSource ds) throws SQLException {
		Date dtStart = new Date();

		for (int i = 1; i < 2000; i++) {
			Connection cn = null;
			ResultSet rs = null;
			PreparedStatement stmt = null;
			try {
				cn = ds.getConnection();
				stmt = cn.prepareStatement("select lid, content from t_log where lid=?");
				stmt.setInt(1, i);

				rs = stmt.executeQuery();

				if (rs.next()) {
					assertEquals(i, rs.getInt(2));
				} else {
					log.info("dataSourceTestCursor error select region_id=" + i);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;

			} finally {

				DbUtils.closeQuietly(cn, stmt, rs);
			}
		}
		Date dtEnd = new Date();
		log.info(ds + " used time:" + (dtEnd.getTime() - dtStart.getTime()) / 1000.0);
	}

	public static void dataSourceTestCursor1(DataSource ds) throws SQLException {
		Date dtStart = new Date();

		for (int i = 1; i < 2000; i++) {
			Connection cn = null;
			ResultSet rs = null;
			Statement stmt = null;
			try {
				cn = ds.getConnection();

				stmt = cn.createStatement();
				rs = stmt.executeQuery("select content, lid from t_log where lid=" + i);

				if (rs.next()) {
					assertEquals(i, rs.getInt(2));

				} else {
					log.info("dataSourceTestCursor1 error select");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;

			} finally {

				DbUtils.closeQuietly(cn, stmt, rs);
			}
			synchronized (dtStart) {
				try {
					dtStart.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Date dtEnd = new Date();
		log.info(ds + " used time:" + (dtEnd.getTime() - dtStart.getTime()) / 1000.0);
	}

	public static void dataSourceTestCursor2(DataSource ds) throws SQLException {
		Date dtStart = new Date();

		List<Long> ids = getIds(ds, 100);

		for (int i = 1; i < ids.size(); i++) {
			Connection cn = null;
			ResultSet rs = null;
			Statement stmt = null;
			try {
				cn = ds.getConnection();

				stmt = cn.createStatement();
				Random r = new Random();
				int k = r.nextInt();
				rs = stmt.executeQuery(
						"select content, lid+(" + k + ") from t_log where lid=" + ids.get(i));

				if (rs.next()) {
					assertEquals(ids.get(i) + k, rs.getInt(2));
				} else {
					log.info("dataSourceTestCursor2 error select");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw e;

			} finally {

				DbUtils.closeQuietly(cn, stmt, rs);
			}
			synchronized (dtStart) {
				try {
					dtStart.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		Date dtEnd = new Date();
		log.info(ds + " used time:" + (dtEnd.getTime() - dtStart.getTime()) / 1000.0);
	}



	public static DatabaseConfig getConfig() {
		return getConfigMysqlMaster();
	}

	public static DatabaseConfig getConfigWest() {

		DatabaseConfig cfg = new DatabaseConfig();
		cfg.setAlias("oracle");
		cfg.setClassName("oracle.jdbc.driver.OracleDriver");
		cfg.setUsername("oracle");
		cfg.setPassword("oracle123");
		cfg.setDburl(
				"jdbc:oracle:thin:@(description=(address=(host=localhost)(protocol=tcp)(port=1521))(connect_data=(sid=testdev)))");
		cfg.setMaxActive(60);

		cfg.setMaxCheckOutCount(200);
		cfg.setSleepIntervalInSeconds(2000);
		cfg.setMaxOpenPreparedStatements(500);

		cfg.setMaxIdle(10);
		// cfg.setMaxWaitTime(10000);

		return cfg;

	}

	public static DatabaseConfig getConfigDerby() {
	    String dbName = "/tmp/data/jijian";
		File file = new File(dbName);
		try {
			FileUtils.deleteDirectory(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		DatabaseConfig cfg = new DatabaseConfig();
		cfg.setAlias("derby");
		cfg.setClassName("org.apache.derby.jdbc.EmbeddedDriver");
		cfg.setUsername("");
		cfg.setPassword("");
		cfg.setDburl("jdbc:derby:"+dbName+";create=true");

		return cfg;

	}

	public static DatabaseConfig getConfigH2() {

		DatabaseConfig cfg = new DatabaseConfig();
		cfg.setAlias("h2");
		cfg.setClassName("org.h2.Driver");
		cfg.setUsername("sa");
		cfg.setPassword("sa");
		cfg.setDburl("jdbc:h2:mem:test");

		return cfg;

	}

	public static DatabaseConfig getConfigMysqlMaster() {

		DatabaseConfig cfg = new DatabaseConfig();
		cfg.setAlias("test1");
		cfg.setClassName("com.mysql.jdbc.Driver");
		cfg.setUsername("test");
		cfg.setPassword("test123");
		cfg.setDburl(
				"jdbc:mysql://localhost:3306/test?autoReconnect=true&useUnicode=true&characterEncoding=utf-8&mysqlEncoding=utf8&useSSL=false");

		return cfg;

	}

	public static DatabaseConfig getConfigMysqlReadOnly() {

		DatabaseConfig cfg = new DatabaseConfig();
		cfg.setAlias("testRO");
		cfg.setClassName("com.mysql.jdbc.Driver");
		cfg.setUsername("test");
		cfg.setPassword("test123");
		cfg.setDburl(
				"jdbc:mysql://localhost:3306/test?autoReconnect=true&useUnicode=true&characterEncoding=utf-8&mysqlEncoding=utf8&useSSL=false");

		return cfg;

	}

}
