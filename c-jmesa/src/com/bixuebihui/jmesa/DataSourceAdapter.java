package com.bixuebihui.jmesa;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.bixuebihui.BeanFactory;
import com.bixuebihui.jdbc.IDbHelper;


/**
 * 用于实现DataSource接口，用于JNDI查询，可实现容器外JUnit测试
 * 因为没有详细测试，请不要用于生产环境，仅用于单元测试
 * @author Dr.Xing
 *
 */
public class DataSourceAdapter implements DataSource {

	IDbHelper dbHelper = (IDbHelper)BeanFactory.createObjectById("dbHelper");

	private PrintWriter out = null;
	private int timeout = 0;


	public Connection getConnection() throws SQLException {
		return dbHelper.getConnection();
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		return dbHelper.getConnection();
	}

	public PrintWriter getLogWriter() throws SQLException {
		return out;
	}

	public int getLoginTimeout() throws SQLException {

		return timeout;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		this.out = out;
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		timeout = seconds;
	}



	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}



	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}



}
