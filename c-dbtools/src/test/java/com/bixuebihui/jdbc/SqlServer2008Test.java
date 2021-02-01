package com.bixuebihui.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import junit.framework.TestCase;


public class SqlServer2008Test extends TestCase{

	public void testDummy(){
		assertTrue(true);
	}

	public void commentout_testDbDriver() throws SQLException {
		Connection conn = null;
		try {
			DriverManager.registerDriver((Driver)Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").getDeclaredConstructor().newInstance());
			
			System.out.println("数据库驱动程序注册成功！");
			String url = "jdbc:sqlserver://192.168.1.13:1433;DatabaseName=ibdmt";
			String user = "sa";
			String password = "123123";
			conn = DriverManager.getConnection(url, user, password);
			//String connectionUrl = "jdbc:sqlserver://localhost:2433;database=gdzc;integratedSecurity=true;";
			//conn = DriverManager.getConnection(connectionUrl, new Properties());
			executeGetTables(conn);


			System.out.println("数据库连接成功");
		} catch (Exception e) {
			e.printStackTrace();
			fail("数据库连接失败");
		}
		finally{
			if(conn!=null)conn.close();
		}
	}


	public static void executeGetTables(Connection con) {
		   try {
		      DatabaseMetaData dbmd = con.getMetaData();

		      String[] tableTypes = { "TABLE", "VIEW" };

		      ResultSet rs = dbmd.getTables(null, "dbo", "%", tableTypes);
		      ResultSetMetaData rsmd = rs.getMetaData();

		      // Display the result set data.
		      int cols = rsmd.getColumnCount();
		      while(rs.next()) {
		         for (int i = 1; i <= cols; i++) {
		            System.out.println(rs.getString(i));
		         }
		      }
		      rs.close();
		   }

		   catch (Exception e) {
		      e.printStackTrace();
		   }
		}
}
