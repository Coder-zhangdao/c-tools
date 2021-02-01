package com.bixuebihui.datasource;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Driver;
import java.sql.DriverManager;

import org.apache.log4j.helpers.Loader;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.RunScript;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;



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

	public H2DataSource init() throws SQLException {
		//jdbc:h2:mem:test
		try{
			InputStream in = getClass().getResourceAsStream("/h2test.sql");			
			DriverManager.registerDriver((Driver) Class.forName("org.h2.Driver").getDeclaredConstructor().newInstance());
			
			InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);

			RunScript.execute(cp.getConnection(),reader);
		}catch(ClassNotFoundException| NoSuchMethodException
		| InstantiationException|IllegalAccessException | InvocationTargetException e){
			e.printStackTrace();
		}
		return this;
	}
}
