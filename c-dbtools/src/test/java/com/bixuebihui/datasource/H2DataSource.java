package com.bixuebihui.datasource;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.SQLException;

import org.apache.log4j.helpers.Loader;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;


public class H2DataSource {

	public final JdbcConnectionPool cp = JdbcConnectionPool.
		    create("jdbc:h2:mem:test", "sa", "sa");

	public final JdbcConnectionPool cpRO = JdbcConnectionPool.
		    create("jdbc:h2:mem:test1", "sa", "sa");

	public H2DataSource init() throws SQLException{
		//jdbc:h2:mem:test
		URL configUrl = Loader.getResource("h2test.sql");

		RunScript.execute("jdbc:h2:mem:test", "sa", "sa",
				configUrl.getPath(),
				Charset.forName("UTF-8"),true);
		return this;
	}

	public void close(){
		cp.dispose();
		cpRO.dispose();
	}

	public static void main(String[] args) throws SQLException{
		new H2DataSource().init().close();
	}
}
