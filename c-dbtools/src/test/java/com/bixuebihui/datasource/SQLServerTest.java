package com.bixuebihui.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
public class SQLServerTest {


	@Disabled
    @Test
	public void testDbDriver() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
					.getDeclaredConstructor().newInstance();
			String url = "jdbc:sqlserver://localhost:2433;DatabaseName=test";
			String user = "sa";
			String password = "password";
			conn = DriverManager.getConnection(url, user, password);

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(conn!=null)conn.close();
		}
	}

}
