package com.bixuebihui.tablegen;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import com.bixuebihui.BeanFactory;
import com.bixuebihui.jdbc.IDbHelper;

import static org.junit.jupiter.api.Assertions.*;

public class TableUtilsTest {
	static IDbHelper dbHelper ;

	@BeforeAll
	public static void setUp() {
		dbHelper=(IDbHelper) BeanFactory.createObjectById("dbHelper");
	}

	@Test
	public void testGetTableKeys() throws SQLException {

		DatabaseMetaData meta = dbHelper.getConnection().getMetaData();
		ResultSet primaryKeys = meta.getPrimaryKeys(null, null,"VRMS_PRJMAPCOLLEGES");
		while (primaryKeys.next())
		{
		String primaryKeyColumn = primaryKeys.getString("COLUMN_NAME");
		System.out.println("Primary Key Column: " + primaryKeyColumn);
		}


		List<String> v = TableUtils.getTableKeys(meta, null, null, "VRMS_PRJMAPCOLLEGES");
		for(String s:v){
			System.out.println(s);
		}
	}

	@Test
	public void testGetTableImportedKeys() throws SQLException {
		DatabaseMetaData metaData = dbHelper.getConnection().getMetaData();

		String tableName="t_log";
		String schema="test";
		StopWatch sw = new StopWatch();
		sw.start();
		List<ForeignKeyDefinition> v = TableUtils.getTableImportedKeys(metaData, null, schema, tableName);
		for(ForeignKeyDefinition s:v){
			System.out.println(s);
		}
		sw.stop();
		System.out.println("use time: "+sw.getTotalTimeMillis()+"ms");
	}

	@Test
	public void testGetTableExportedKeys() throws SQLException {
		//need root to run
		//dbHelper.executeNoQuery("SET GLOBAL innodb_stats_on_metadata = 0;\r\n" );
		DatabaseMetaData metaData = dbHelper.getConnection().getMetaData();

		String tableName="Forum";
		String schema="test";
		String catalog="test";
		StopWatch sw = new StopWatch();
		sw.start();
		List<ForeignKeyDefinition> v = TableUtils.getTableExportedKeys(metaData, catalog, schema, tableName);
		for(ForeignKeyDefinition s:v){
			System.out.println(s);
		}
		sw.stop();
		System.out.println("use time: "+sw.getTotalTimeMillis()+"ms");
	}

	//我不想把短暂的人生投入到无限的忽悠事业中去
	//
	@Test
	public void testGetTableData() {

	}

	@Test
	public void testMatchTableName() {
		Map<String, String> inlist = new HashMap<>();
		inlist.put("WPVM_(\\w+)", "WPVM_(\\w+)");
		inlist.put("DIC_(\\w+)", "DIC_(\\w+)");
		String tableName = "WPVM_COMPANYINFO";

		assertTrue(TableUtils.matchTableName(inlist, tableName));
		tableName = "DIC_INSURANCE";

		assertTrue(TableUtils.matchTableName(inlist, tableName));
		tableName = "MYDIC_INSURANCE";

		assertFalse(TableUtils.matchTableName(inlist, tableName));

	}

	@Test
	public void testMatchTableName1() {
		Map<String, String> inlist = new HashMap<>();
		inlist.put("tbl_MMResult_(\\w+)", "tbl_MMResult_(\\w+)");
		inlist.put("tbl_SMResult_(\\w+)", "tbl_SMResult_(\\w+)");
		String tableName = "tbl_MMResult_0101";

		assertTrue(TableUtils.matchTableName(inlist, tableName));

		tableName = "tbl_SMResult_0102";

		assertTrue(TableUtils.matchTableName(inlist, tableName));

	}



	@Test
	public void testGetColumnData11() throws SQLException {
		DatabaseMetaData metaData = dbHelper.getConnection().getMetaData();
		String tableName="VRMS_ALLOWANCE";
		String catalog="WESTPLAN";
		List<ColumnData> v = TableUtils.getColumnData(metaData, catalog, null, tableName).fields;
		for(ColumnData c:v){
			System.out.println(c);
		}
	}
	@Test
	public void testGetColumnData() throws SQLException {
		DatabaseMetaData meta = dbHelper.getConnection().getMetaData();

		//create table test1(id int auto_increment, num decimal(10,6) , primary key(id));

		String tableName ="t_metatable";
		List<ColumnData> v = TableUtils.getColumnData(meta, null, null, tableName).fields;
		for(ColumnData s:v){
			System.out.println(s+", java type = "+s.getJavaType());
		}
	}

    @org.junit.jupiter.api.Test
    void getTableImportedKeys() throws SQLException {
		String tableName = "Businesses_details";
		List<ForeignKeyDefinition> res = TableUtils.getTableImportedKeys(dbHelper.getConnection().getMetaData(),
				"test", "test", tableName);
		assertEquals(1, res.size());

		Map<String, List<ForeignKeyDefinition>> map = TableUtils.getAllMySQLImportKeys(dbHelper, "ssll");

		List<ForeignKeyDefinition> newList = map.get(tableName);


		////这里getTableImportedKeys sFKTable返回的表名是小写的(5.6.21-enterprise-commercial-advanced mac os),不知为什么?
		//
		assertEquals(res.get(0).toString().toLowerCase(), newList.get(0).toString().toLowerCase());

    }

    @org.junit.jupiter.api.Test
    void getTableExportedKeys() throws SQLException {
		List<ForeignKeyDefinition> res = TableUtils.getTableExportedKeys(dbHelper.getConnection().getMetaData(),
				"ssll", "ssll", "Businesses");
		assertEquals(1, res.size());
    }

    @org.junit.jupiter.api.Test
    void getAllMySQLExportKeys() throws SQLException {
		Map<String, List<ForeignKeyDefinition>> res = TableUtils.getAllMySQLImportKeys(dbHelper, "ssll");
		for(List<ForeignKeyDefinition> list: res.values()){
			for(ForeignKeyDefinition fk: list){
				System.out.println(fk.toString());
			}
		}
		assertTrue( res.size()>1 );
    }

    @org.junit.jupiter.api.Test
    void getAllMySQLImportKeys() throws SQLException {
		Map<String, List<ForeignKeyDefinition>> res = TableUtils.getAllMySQLExportKeys(dbHelper, "ssll");
		for(List<ForeignKeyDefinition> list: res.values()){
			for(ForeignKeyDefinition fk: list){
				System.out.println(fk.toString());
			}
		}
		assertTrue(res.size()>1);
    }
}
