package com.bixuebihui.tablegen;

import com.bixuebihui.generated.tablegen.business.T_metatableManager;
import com.bixuebihui.generated.tablegen.pojo.T_metatable;
import com.bixuebihui.jdbc.IDbHelper;
import com.bixuebihui.tablegen.entry.TableInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;

import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

@Disabled
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

		Map<String, T_metatable> res = tg.setInfo.getExtraTableDataFromXml(extra_setting2, null);
		Assert.assertEquals(1, res.size());
		Assert.assertEquals(1, res.get("sm_template_suite").getColumns().size());
	}

	@Test
	public void testGetPojoClassName() throws IOException {
		TableGen tg = new TableGen();
		tg.getConfig().prefix="";

		String extra_setting2="src/main/resources/pojos.xml";
		String name = "sm_template_suite";
		Assert.assertEquals("Sm_template_suite", tg.getPojoClassName(name));
		Map<String, T_metatable> res = tg.setInfo.getExtraTableDataFromXml(extra_setting2, null);
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
		tg.init(this.getClass().getResource("/tablegen.properties")
				.getFile());
		IDbHelper helper = TableGen.getDbHelper(tg.getDbConfig());
		DatabaseMetaData meta = tg.connect(helper.getConnection());
		tg.setInfo.getTableData(tg.getConfig(), helper, meta);
		LinkedHashMap<String, TableInfo> tables = tg.setInfo.getTableInfos();
		for(TableInfo t:tables.values()){
			System.out.println(t);
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
			IDbHelper helper = TableGen.getDbHelper(tg.getDbConfig());
			DatabaseMetaData meta = tg.connect(helper.getConnection());

			tg.setInfo.getTableData(tg.getConfig(), helper, meta);
			T_metatableManager manager = new T_metatableManager();
			manager.setDbHelper(helper);
			boolean res  = tg.setInfo.initTableData(tg.setInfo.getTableInfos(), manager);
			Assert.assertTrue(res);
		}catch(BatchUpdateException e){
			e.getNextException().printStackTrace();
			e.printStackTrace();
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
