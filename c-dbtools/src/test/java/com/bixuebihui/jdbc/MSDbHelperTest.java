package com.bixuebihui.jdbc;

import com.bixuebihui.datasource.BitmechanicDataSource;
import com.bixuebihui.datasource.DataSourceTest;
import com.bixuebihui.dbcon.DatabaseConfig;
import org.easymock.EasyMockSupport;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;


public class MSDbHelperTest extends EasyMockSupport {

	@Test
	public void testGetConnection() throws SQLException {

	}

	@Test
	public void testGetConnectionBoolean() throws SQLException {
		MSDbHelper db = new MSDbHelper();

		BitmechanicDataSource ds = new BitmechanicDataSource();
		DatabaseConfig ro = DataSourceTest.getConfigMysqlReadOnly();
		ds.setDatabaseConfig(ro);
		db.setDataSource(ds);

		BitmechanicDataSource master = new BitmechanicDataSource();
		DatabaseConfig ma = DataSourceTest.getConfigMysqlMaster();
		master.setDatabaseConfig(ma);

		db.setMasterDatasource(master);

		Connection cn1 = db.getConnection(true);
		assertEquals(ro.getDburl(), cn1.getMetaData().getURL());

		Connection cn2 = db.getConnection(false);
		assertEquals(ma.getDburl(), cn2.getMetaData().getURL());
	}

	@Test
	public void testForceUseMasterDb() throws SQLException{
		MSDbHelper db = new MSDbHelper();

		BitmechanicDataSource ds = new BitmechanicDataSource();
		DatabaseConfig ro = DataSourceTest.getConfigMysqlReadOnly();
		ds.setDatabaseConfig(ro);
		db.setDataSource(ds);

		BitmechanicDataSource master = new BitmechanicDataSource();
		DatabaseConfig ma = DataSourceTest.getConfigMysqlMaster();
		master.setDatabaseConfig(ma);

		db.setMasterDatasource(master);


		AbstractBaseDao dao = new AbstractBaseDao(db){
			@Override
			public String getTableName() {
				return "t_log";
			}
			@Override
            protected String getInsertSql() {
				return "insert into "+this.getTableName()+" (lid, content) values(?,?)";
			}

			@Override
			protected Object[] getInsertObjs(Object info) {
				return new Object[]{1,"this is a test"};
			}

		};


		dao.beginForceMasterDB();
		Connection conn = dao.dbHelper.getConnection();
		assertFalse(conn.isReadOnly());
		dao.ar().eq("lid", 1).delete();
		assertNull(dao.ar().eq("lid", 1).find());

		dao.insert(new Object());

		assertEquals((long) dao.ar().eq("lid", 1).getLongVector("lid").get(0), 1L);
		dao.endForceMasterDB();

		Connection connRO = dao.dbHelper.getConnection(true);
		assertTrue(connRO.isReadOnly());
	}




}
