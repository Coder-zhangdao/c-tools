package com.bixuebihui.tablegen;

import java.sql.Connection;

import com.bixuebihui.BeanFactory;
import com.bixuebihui.generated.tablegen.business.T_metatableManager;
import com.bixuebihui.jdbc.IDbHelper;
import com.bixuebihui.jdbc.JDBCUtils;
import com.bixuebihui.jdbc.SqlFileExecutor;

/**
 * @author xwx
 */
public class InstallMetaTable {


	public boolean run() throws Exception {
		IDbHelper dbHelper = (IDbHelper) BeanFactory
				.createObjectById("dbHelper");
		Connection conn = null;
		boolean res = false;
		try {
			conn = dbHelper.getConnection();
			T_metatableManager metatableManager = new T_metatableManager();
			if (!JDBCUtils.tableOrViewExists(null, null, metatableManager.getTableName(),
					conn)) {
				SqlFileExecutor ex = new SqlFileExecutor();
				String sqlFile = this.getClass().getResource("/dbscript/dbscript.mysql.sql")
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
