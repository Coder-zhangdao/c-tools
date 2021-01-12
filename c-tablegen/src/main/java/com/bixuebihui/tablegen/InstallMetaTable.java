package com.bixuebihui.tablegen;

import java.sql.Connection;

import com.bixuebihui.BeanFactory;
import com.bixuebihui.jdbc.IDbHelper;
import com.bixuebihui.jdbc.JDBCUtils;
import com.bixuebihui.jdbc.SqlFileExecutor;

public class InstallKZTable {

	public static final String KUOZHANBIAOMING = "KUOZHANBIAOMING";

	public boolean run() throws Exception {
		IDbHelper dbHelper = (IDbHelper) BeanFactory
				.createObjectById("dbHelper");
		Connection conn = null;
		boolean res = false;
		try {
			conn = dbHelper.getConnection();
			if (!JDBCUtils.tableOrViewExists(null, null, KUOZHANBIAOMING,
					conn)) {
				SqlFileExecutor ex = new SqlFileExecutor();
				String sqlFile = this.getClass().getResource("/dbscript.sql")
						.getFile();
				ex.execute(conn, sqlFile);
				res = true;
				System.out.println("Install complete!");
			} else {
				System.out.println("Table already exists!");
			}
		} finally {
			JDBCUtils.close(conn);
		}
		return res;
	}
}
