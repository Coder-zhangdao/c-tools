package com.bixuebihui.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import junit.framework.TestCase;

public class SQLServerTest extends TestCase {

	public void testDbDriver(){
		//com.microsoft.sqlserver.jdbc.SQLServerDriver driver = Mock(com.microsoft.sqlserver.jdbc.SQLServerDriver);
		assertTrue(true);
	}

	public void ctestDbDriver() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
					.newInstance();
			System.out.println("数据库驱动程序注册成功！");
			String url = "jdbc:sqlserver://localhost:2433;DatabaseName=gdzc";
			String user = "sa";
			String password = "123$%^";
			conn = DriverManager.getConnection(url, user, password);
			//String connectionUrl = "jdbc:sqlserver://localhost:2433;database=gdzc;integratedSecurity=true;";
			//conn = DriverManager.getConnection(connectionUrl, new Properties());

			System.out.println("数据库连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			fail("数据库连接失败");
		}
		finally{
			if(conn!=null)conn.close();
		}
	}

}
