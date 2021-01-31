package com.bixuebihui.jdbc;

import com.bixuebihui.datasource.DataSourceTest;
import com.bixuebihui.datasource.DbcpDataSource;
import com.bixuebihui.db.ActiveRecord;
import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class BaseDaoTest extends TestCase {

	public void testGetCount() throws SQLException {
		DbcpDataSource ds = new DbcpDataSource();
		ds.setDatabaseConfig(DataSourceTest.getConfig());
		IDbHelper db = new DbHelper();
		db.setDataSource(ds);
		AbstractBaseDao bd = new AbstractBaseDao(db) {

			@Override
			public String getTableName() {
				return " (select lid from t_log) t ";
			}

		};
		System.out.println(bd.addAlias(bd.getTableName()));

		System.out.println(bd.getCount(bd.getTableName(), ""));
	}

	public void testGetPagingSqlOracle() {
	}


	private void derbySql() throws SQLException {
		DbcpDataSource ds = new DbcpDataSource();
		ds.setDatabaseConfig(DataSourceTest.getConfigDerby());
		IDbHelper db = new DbHelper();
		db.setDataSource(ds);
		AbstractBaseDao bd = new AbstractBaseDao(db) {

			@Override
			public String getTableName() {
				return "test";
			}

		};

		System.out.println(bd.getPagingSql("select * from test", 20, 40));
	}

	public void testGetPagingSql() throws SQLException {

		derbySql();
	}


	public void testGetLastInsertID() throws SQLException {
		DbcpDataSource ds = new DbcpDataSource();
		ds.setDatabaseConfig(DataSourceTest.getConfigMaster());
		DbHelper db = new DbHelper();
		db.setDataSource(ds);
		Connection cn = db.getConnection();
		try{
			db.executeNoQuery("DROP TABLE IF EXISTS test ",cn);
			db.executeNoQuery("create table if not exists test(id int not null AUTO_INCREMENT, name varchar(100), primary key(id))",
					cn);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		AbstractBaseDao bd = new AbstractBaseDao(db) {

			@Override
			public String getTableName() {
				return "test";
			}

		};

		bd.getDbHelper().executeNoQuery("insert into test (name)values('this is test')",null,cn);

		Long res;
		try {
			res = bd.getLastInsertId(cn);
		}catch (SQLException e){
			e.printStackTrace();
			cn.close();
			return;
		}
		assertEquals(1,res.intValue());
		cn.close();
	}


	public void testGetUpdateSql() throws SQLException {
		DbcpDataSource ds = new DbcpDataSource();
		ds.setDatabaseConfig(DataSourceTest.getConfigDerby());
		IDbHelper db = new DbHelper();
		db.setDataSource(ds);
		AbstractBaseDao bd = new AbstractBaseDao(db) {

			@Override
			public String getTableName() {
				return "test";
			}

		};

		assertTrue("update test set files1=?, fieldss2=? ".equals(bd
				.getUpdateSql(new String[] { "files1", "fieldss2" }, "")));
		assertTrue("update test set files1=? where 1=1".equals(bd.getUpdateSql(
				new String[] { "files1" }, "where 1=1")));
	}

	public void testAr() throws SQLException {
		DbcpDataSource ds = new DbcpDataSource();
		ds.setDatabaseConfig(DataSourceTest.getConfigDerby());
		DbHelper db = new DbHelper();
		db.setDataSource(ds);
		Connection cn = db.getConnection();

		//boolean res = JDBCUtils.tableOrViewExists(null, null, "%", cn);
		try{
			db.executeNoQuery("drop table test",cn);
			db.executeNoQuery("create table test(id int, name varchar(100))",
					cn);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		class T {
			Long id;
			String name;
		}
		class BD extends AbstractBaseDao {
			BD(IDbHelper db) {
				super(db);
			}

			@Override
			public T mapRow(ResultSet rs, int index) throws SQLException {
				T t = new T();
				t.id = rs.getLong("id");
				t.name = rs.getString("name");
				return t;
			}

			@Override
			public String getTableName() {
				return "test";
			}
		}

		AbstractBaseDao bd = new BD(db);
		bd.getDbHelper().executeNoQuery(
				"insert into test(id,name)values(123,'bac')");
		bd.getDbHelper().executeNoQuery(
				"insert into test(id,name)values(234,'bac')");
		Object obj = bd.ar().eq("id", 123).find();
		System.out.println(((T) obj).id);

		ActiveRecord<Object> ar = bd.ar();
        //Record<Object> ar1 =ar.eq("id", 123).clone();


		Object obj1 = ar.eq("id", 123).find();
		System.out.println(((T) obj1).id);
		int res1 = ar.last().count();
		assertEquals(1,res1);
	}


	public void testSelectK() throws SQLException{
		DbcpDataSource ds = new DbcpDataSource();
		ds.setDatabaseConfig(DataSourceTest.getConfigDerby());
		DbHelper db = new DbHelper();
		db.setDataSource(ds);
		Connection cn = db.getConnection();

		try{
			db.executeNoQuery("drop table test",cn);}catch(SQLException ex){
				ex.printStackTrace();
			}
		try{
			db.executeNoQuery("create table test(id int, name varchar(100))",
					cn);
			db.executeNoQuery("insert into test(id , name) values(100,'abc')",
					cn);
			db.executeNoQuery("insert into test(id , name) values(200,'efg')",
					cn);
			db.executeNoQuery("insert into test(id , name) values(300,'MKD')",
					cn);
		}catch(SQLException ex){
			ex.printStackTrace();
		}

		class BD extends AbstractBaseDao {
			BD(IDbHelper db) {
				super(db);
			}
		}

		BD mybd = new BD(db);

		String select = "select id from test";
		String whereClause=" where id>100";
		String orderBy=" order by id";
		int rowStart=0;
		int rowEnd=100;

		List<Long> res = mybd.select(select, whereClause, orderBy, null, rowStart, rowEnd, Long.class);
		assertEquals(2, res.size());
		System.out.println(res);
	}


	public void testGetSingleObject() throws SQLException{
		DbcpDataSource ds = new DbcpDataSource();
		ds.setDatabaseConfig(DataSourceTest.getConfigDerby());
		DbHelper db = new DbHelper();
		db.setDataSource(ds);
		Connection cn = db.getConnection();

		try{
			db.executeNoQuery("drop table test",cn);}catch(SQLException ex){
				ex.printStackTrace();
			}
		try{
			db.executeNoQuery("create table test(id int, name varchar(100), dt timestamp  default current_timestamp)",
					cn);
			db.executeNoQuery("insert into test(id , name, dt) values(100,'abc', current_timestamp)",
					cn);
			db.executeNoQuery("insert into test(id , name, dt) values(200,'efg', current_timestamp)",
					cn);
			db.executeNoQuery("insert into test(id , name, dt) values(300,'MKD', null)",
					cn);
		}catch(SQLException ex){
			ex.printStackTrace();
		}

		class BD extends AbstractBaseDao {
			BD(IDbHelper db) {
				super(db);
			}

		}

		BD mybd = new BD(db);

		String select = "select id, name, dt from test";
		String whereClause=" where id>100";
		String orderBy=" order by id";
		Simple s = new Simple();
		Simple obj = mybd.getSingleObject(select+whereClause+orderBy, new Object[]{}, s);

		assertEquals(200, obj.getId());
		assertEquals("efg", obj.getName());
		System.out.println(obj);

		List<Simple> res = mybd.select(select, whereClause, orderBy, null, 0, 100, Simple.class);
		for(Simple s1:res){
			System.out.println(s1);
		}

	}

	public static class Simple{
		private String name;

		public String getName() {
			return name;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}

		public void setName(String name) {
			this.name = name;
		}
		public Timestamp getDt() {
			return dt;
		}
		public void setDt(Timestamp dt) {
			this.dt = dt;
		}

		public String toString(){
			return "id="+id+", name="+ name +", dt="+dt;
		}
		private long id;
		private Timestamp dt;
	}


	public void testGetFirstLast() throws SQLException{
		DbcpDataSource ds = new DbcpDataSource();
		ds.setDatabaseConfig(DataSourceTest.getConfigDerby());
		DbHelper db = new DbHelper();
		db.setDataSource(ds);
		Connection cn = db.getConnection();

		try{
			db.executeNoQuery("drop table test1",cn);}catch(SQLException ex){
				//ex.printStackTrace();
			}
		try{
			db.executeNoQuery("create table test1(id int, name varchar(100), dt timestamp  default current_timestamp, primary key (id))",
					cn);
			db.executeNoQuery("insert into test1(id , name, dt) values(300,'MKD', null)",
					cn);
			db.executeNoQuery("insert into test1(id , name, dt) values(100,'abc', current_timestamp)",
					cn);
			db.executeNoQuery("insert into test1(id , name, dt) values(200,'efg', current_timestamp)",
					cn);
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		class DbObj{
			long id;
			String name;
			Timestamp dt;
		}

		class BD extends BaseDao<DbObj, Long> {
			public BD(IDbHelper db) {
				super(db);
			}
			public DbObj mapRow(ResultSet rs, int index) throws SQLException {
				DbObj o= new DbObj();
				 o.id = rs.getLong(1);
				 o.name = rs.getString(2);
				 o.dt = rs.getTimestamp(3);
				 return o;
			}

			@Override
			public String getKeyName() {
				return "ID";
			}
			@Override
			public String getTableName() {
				return "test1";
			}
			@Override
			public boolean insertDummy() throws SQLException {
				return false;
			}
			@Override
			public Long getId(DbObj info) {
				return info.id;
			}
			@Override
			public void setId(DbObj info, Long id) {
				info.id = id;
			}
			@Override
			public Long getNextKey() {
				return null;
			}

		}

		BD mybd = new BD(db);


		assertEquals(300, mybd.getLast().id);
		assertEquals(100, mybd.getFirst().id);

		assertEquals("MKD", mybd.getLast().name);
		assertEquals("abc", mybd.getFirst().name);
		System.out.println(mybd.getLast().dt);
		System.out.println(mybd.getFirst().dt);

	}
}
