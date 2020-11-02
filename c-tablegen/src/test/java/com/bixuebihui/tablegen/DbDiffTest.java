package com.bixuebihui.tablegen;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bixuebihui.dbcon.DatabaseConfig;

import org.junit.Assert;
import org.junit.Test;

public class DbDiffTest {

	@Test
	public void testFindTables() {
		Assert.fail("Not yet implemented");
	}

	@Test
	public void testDumpDiffTabs() {
		List<String> tab1 =new ArrayList<String>();
		tab1.add("tab1");
		tab1.add("tab3");
		List<String> tab2 =new ArrayList<>();
		tab2.add("tab2");
		tab2.add("tab4");

		tab2.add("tab1");
		DbDiff dd = new DbDiff();

		dd.dumpDiffTabs(tab1, tab2);

	}

	@Test
	public void testDumpDiffCols(){
		DbDiff dd = new DbDiff();

		List<ColumnData> cols1 = new ArrayList<>();
		ColumnData col1 = new ColumnData("col1", "int", 10, true);
		ColumnData col2 = new ColumnData("col2", "varchar", 20, true);
		ColumnData col3 = new ColumnData("col3", "varchar", 30, true);
		ColumnData col4 = new ColumnData("col3", "varchar", 60, true);
		ColumnData col5 = new ColumnData("col5", "varchar", 60, true);
		cols1.add(col1);
		cols1.add(col2);
		cols1.add(col4);
		cols1.add(col5);

		List<ColumnData> cols2 = new ArrayList<>();

		cols2.add(col1);
		cols2.add(col2);
		cols2.add(col3);
		dd.dumpDiffCols(cols1, cols2);
	}


	@Test
	public void testDbDiff() throws SQLException{

		DatabaseConfig srccfg = new DatabaseConfig();
		String className="com.mysql.jdbc.Driver";
		srccfg.setClassName(className);

		String dburl="jdbc:mysql://db.issll.com:3306/ssll";
		String username="ssll";
		String password="ssll123";

		srccfg.setDburl(dburl);
		srccfg.setUsername(username);
		srccfg.setPassword(password);
		srccfg.setAlias("srccfg");

		System.out.println(srccfg);


		DatabaseConfig dstcfg= new DatabaseConfig();
		dstcfg.setClassName(className);
		dstcfg.setAlias("dstcfg");


		dburl="jdbc:mysql://db.issll.com:3306/ssllalpha";
		username="ssllalpha";
		password="ssllalpha123";

		dstcfg.setDburl(dburl);
		dstcfg.setUsername(username);
		dstcfg.setPassword(password);

		System.out.println(dstcfg);

		DbDiff dd = new DbDiff();
		dd.db1 = new DbDiff.Database(dd.makeDataSource(srccfg).getConnection().getMetaData());
		dd.db2 = new DbDiff.Database(dd.makeDataSource(dstcfg).getConnection().getMetaData());
		dd.compareTables();


	}

}
