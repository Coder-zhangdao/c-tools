package com.bixuebihui.datasource;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import org.apache.log4j.helpers.Loader;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;


public class H2DataSource {

	public final JdbcConnectionPool cp = JdbcConnectionPool.
		    create("jdbc:h2:mem:test", "sa", "sa");

	public final JdbcConnectionPool cpRO = JdbcConnectionPool.
		    create("jdbc:h2:mem:test1", "sa", "sa");

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		new H2DataSource().init().close();
	}

	public void close(){
		cp.dispose();
		cpRO.dispose();
	}

	public H2DataSource init() throws SQLException, ClassNotFoundException {
		//jdbc:h2:mem:test
		URL configUrl = Loader.getResource("h2test.sql");
		Class.forName("org.h2.Driver");

		RunScript.execute("jdbc:h2:mem:test", "sa", "sa",
				configUrl.getPath(),
				StandardCharsets.UTF_8,true);
		return this;
	}
}
