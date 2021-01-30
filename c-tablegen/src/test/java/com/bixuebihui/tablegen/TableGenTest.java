package com.bixuebihui.tablegen;

import com.bixuebihui.generated.tablegen.pojo.T_metatable;
import com.bixuebihui.tablegen.entry.TableInfo;import org.apache.commons.dbutils.DbUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TableGenTest {

	@Test
	public void testRun() throws SQLException {
		TableGen tg = new TableGen(System.out);
		System.out.println(this.getClass().getResource("/tablegen.properties")
			.getFile());

		tg.run(this.getClass().getResource("/tablegen.properties").getFile());

	}



    @Test
	public void testRunDiff() throws SQLException {
        TableGen tg = new TableGen(System.out);

        String propPath = System.getProperty("user.dir")+ "/tablegen.properties";

        tg.run(propPath);

    }


	@Test
	public void testGetExtraTableDataFromXml() throws IOException {
		TableGen tg = new TableGen();

		String extra_setting2="src/main/resources/pojos.xml";

		List<String> tableNames2= new ArrayList<>();
		tableNames2.add("sm_template_suite");
		Map<String, T_metatable> tableData2 = null;
		Map<String, T_metatable> res = tg.getExtraTableDataFromXml(extra_setting2, tableData2);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals(1, res.get("sm_template_suite").getColumns().size());
	}

	@Test
	public void testGetPojoClassName() throws IOException {
		TableGen tg = new TableGen();
		tg.config.prefix="";

		String extra_setting2="src/main/resources/pojos.xml";
		String name = "sm_template_suite";
		Assert.assertEquals("Sm_template_suite", tg.getPojoClassName(name));
		Map<String, T_metatable> res = tg.getExtraTableDataFromXml(extra_setting2, null);
		tg.setInfo.setTableDataExt(res);

		Assert.assertEquals("TemplateSuite", tg.getPojoClassName(name));
	}





	@Test
	public void testRunSpider() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/spider_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/spider_tablegen.properties")
				.getFile());
	}

	@Test
	public void testRunVerycard() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/verycard_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/verycard_tablegen.properties")
				.getFile());
	}



	@Test
	public void testRunVolManager() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()IOException
		System.out.println(this.getClass().getResource(
				"/volmanage_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/volmanage_tablegen.properties")
				.getFile());
	}

	@Test
	public void testGetTableDate() throws SQLException, InstantiationException, IllegalAccessException, IOException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource("/tablegen.properties")
				.getFile());
		try {
			tg.init(this.getClass().getResource("/tablegen.properties")
					.getFile());
			tg.connect();
			tg.getTableData();
			LinkedHashMap<String, TableInfo> tables = tg.setInfo.getTableInfos();
			for(TableInfo t:tables.values()){
				System.out.println(t);
			}

		} finally {
			DbUtils.close(tg.getConnection());
		}
	}

	@Test
	public void testInitTableDateExt() throws SQLException, InstantiationException, IllegalAccessException, IOException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource("/tablegen.properties")
				.getFile());
		try {
			tg.init(this.getClass().getResource("/tablegen.properties")
					.getFile());
			tg.connect();
			tg.getTableData();
			boolean res  = tg.initTableData(tg.setInfo.getTableInfos());
			Assert.assertTrue(res);
		}catch(BatchUpdateException e){
			e.getNextException().printStackTrace();
			e.printStackTrace();
		}

		finally {
			DbUtils.close(tg.getConnection());
		}
	}

	@Test
	public void testMakedir() {
		TableGen tg = new TableGen();
		System.out.println(this.getClass().getResource("/tablegen.properties")
				.getFile());
		tg.init(null);
		Assert.assertTrue(tg.makeDir());
	}

}
