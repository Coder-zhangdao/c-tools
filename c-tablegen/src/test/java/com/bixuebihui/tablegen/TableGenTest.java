package com.bixuebihui.tablegen;

import com.bixuebihui.generated.tablegen.pojo.T_metatable;
import org.apache.commons.dbutils.DbUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableGenTest {

	@Test
	public void testRun() throws SQLException {
		TableGen tg = new TableGen(System.out);
		System.out.println(this.getClass().getResource("/tablegen.prop")
			.getFile());


		tg.run(this.getClass().getResource("/tablegen.prop").getFile());

	}

    @Test
	public void testRunDiff() throws SQLException {
        TableGen tg = new TableGen(System.out);

        String propPath = System.getProperty("user.dir")+"/tablegen.prop";

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
		tg.prefix="";

		String extra_setting2="src/main/resources/pojos.xml";
		String name = "sm_template_suite";
		Assert.assertEquals("Sm_template_suite", tg.getPojoClassName(name));
		Map<String, T_metatable> res = tg.getExtraTableDataFromXml(extra_setting2, null);
		tg.tableData = res;

		Assert.assertEquals("TemplateSuite", tg.getPojoClassName(name));
	}




	@Test
	public void testRunVol2008() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/vol2008_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/vol2008_tablegen.properties")
				.getFile());
	}

	@Test
	public void testRunSms() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/sms_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/sms_tablegen.properties")
				.getFile());
	}

	@Test
	public void testRunTrs() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/trs_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/trs_tablegen.properties")
				.getFile());
	}



	@Test
	public void testRunAppraise() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/appraise_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/appraise_tablegen.properties")
				.getFile());
	}
	@Test
	public void testRunJob() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/job_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/job_tablegen.properties")
				.getFile());
	}

	@Test
	public void testRunPhotolist() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/photolist_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/photolist_tablegen.properties")
				.getFile());
	}

	@Test
	public void testRundns() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/dns_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/dns_tablegen.properties")
				.getFile());
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
	public void testRunMySqlTest() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/tablegen.prop").getFile());
		tg.run(this.getClass().getResource("/tablegen.prop")
				.getFile());
	}

	@Test
	public void testRunDdns() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/ddns_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/ddns_tablegen.properties")
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
	public void testRunCheguan() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/cheguan_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/cheguan_tablegen.properties")
				.getFile());
	}

	@Test
	public void testRunself() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/cheguan_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/cheguan_tablegen.properties")
				.getFile());
	}

	@Test
	public void testGetTableDate() throws SQLException, InstantiationException, IllegalAccessException, IOException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource("/tablegen.prop")
				.getFile());
		try {
			tg.init(this.getClass().getResource("/tablegen.prop")
					.getFile());
			tg.connect();
			tg.getTableData();
			List<String> tables = tg.getTableNames();
			for(String t:tables){
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
		System.out.println(this.getClass().getResource("/tablegen.prop")
				.getFile());
		try {
			tg.init(this.getClass().getResource("/tablegen.prop")
					.getFile());
			tg.connect();
			tg.getTableData();
			boolean res  = tg.initTableData(tg.getTableNames());
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

	@Test
	public void testRunDGW() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/dgw_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/dgw_tablegen.properties")
				.getFile());
	}

	@Test
	public void testRunMissionReport() throws SQLException {
		TableGen tg = new TableGen();
		// this.getClass().getResource("tablegen.properties").getFile()
		System.out.println(this.getClass().getResource(
				"/mr2010_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/mr2010_tablegen.properties")
				.getFile());
	}


	@Test
	public void testRunCyea() throws SQLException {
		TableGen tg = new TableGen();
		System.out.println(this.getClass().getResource(
				"/cyea_tablegen.properties").getFile());
		tg.run(this.getClass().getResource("/cyea_tablegen.properties")
				.getFile());
	}
}
