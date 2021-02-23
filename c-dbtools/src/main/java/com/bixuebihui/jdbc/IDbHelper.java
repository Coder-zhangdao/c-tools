package com.bixuebihui.jdbc;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * <p>IDbHelper interface.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public interface IDbHelper {


	/**
	 * Example: final List names = new LinkedList();
	 * dbHelper.executeQuery("SELECT user_mc FROM jiao_user", new
	 * RowCallbackHandler() { public void processRow(ResultSet rs) throws
	 * SQLException { names.add(rs.getString(1)); };
	 *
	 * @param sql
	 *            SQL语句
	 * @param handle
	 *            处理接口
	 * @throws java.sql.SQLException
	 *             数据库出错
	 */
	void executeQuery(String sql, RowCallbackHandler handle) throws SQLException;

	/**
	 * <p>executeQuery.</p>
	 *
	 * @param sql    a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @param handle a {@link RowCallbackHandler} object.
	 * @throws java.sql.SQLException if any.
	 */
	void executeQuery(String sql, Object[] params, RowCallbackHandler handle) throws SQLException;

	/**
	 * must change the return value to {@code List<Map<String,Object>> }
	 *
	 * @param sql    SQL 语句
	 * @param params SQL 参数
	 * @return List of Map
	 * @throws java.sql.SQLException 数据库异常
	 */
	List<Map<String, Object>> executeQuery(String sql, Object[] params) throws SQLException;

	/**
	 * <p>executeScalar.</p>
	 *
	 * @param strSql a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.sql.SQLException if any.
	 */
	Object executeScalar(String strSql) throws SQLException;

	/**
	 * <p>executeScalar.</p>
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.sql.SQLException if any.
	 */
	Object executeScalar(String sql, Object[] params) throws SQLException;

	/**
	 * <p>executeScalar.</p>
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @param cn a {@link java.sql.Connection} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.sql.SQLException if any.
	 */
	Object executeScalar(String sql, Object[] params, Connection cn) throws SQLException;

	/**
	 * must change the return value to {@code List<Map<String,Object>> }
	 *
	 * @param strSql SQL 语句
	 * @return List of Map
	 * @throws java.sql.SQLException 数据库异常
	 */
	List<Map<String, Object>> exeQuery(String strSql) throws SQLException;

	/**
	 * <p>getConnection.</p>
	 *
	 * @return a {@link java.sql.Connection} object.
	 * @throws java.sql.SQLException if any.
	 */
	Connection getConnection() throws SQLException;

	/**
	 * <p>executeQuery.</p>
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @param handle a {@link RowMapperResultReader} object.
	 * @param <T> a T object.
	 * @return a {@link java.util.List} object.
	 * @throws java.sql.SQLException if any.
	 */
	@NotNull
	<T> List<T> executeQuery(String sql, Object[] params, RowMapperResultReader<T> handle) throws SQLException;

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param strSql a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @param cn a {@link java.sql.Connection} object.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	int executeNoQuery(String strSql, Object[] params, Connection cn) throws SQLException;

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @param targetSqlTypes an array of int.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	int executeNoQuery(String sql, Object[] params, int[] targetSqlTypes) throws SQLException;

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @param targetSqlTypes an array of int.
	 * @param conn a {@link java.sql.Connection} object.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	int executeNoQuery(String sql, Object[] params, int[] targetSqlTypes, Connection conn) throws SQLException;

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param strSql a {@link java.lang.String} object.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	int executeNoQuery(String strSql) throws SQLException;

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	int executeNoQuery(String sql, Object[] params) throws SQLException;

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param strSql an array of {@link java.lang.String} objects.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	int executeNoQuery(String[] strSql) throws SQLException;

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param strSql an array of {@link java.lang.String} objects.
	 * @param cn a {@link java.sql.Connection} object.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	int executeNoQuery(String[] strSql, Connection cn) throws SQLException;


	/**
	 * 批处里数组, 用于Insert, delete或update
	 *
	 * @param strSql
	 *            参数化SQL语句
	 * @param params
	 *            参数数组列表
	 * @param cn
	 *            数据库连接,如为空,则自动获得
	 * @return 成功执行数量
	 * @throws java.sql.SQLException
	 *             数据库出错
	 */
	int executeNoQueryBatch(String strSql, Iterable<Object[]> params, Connection cn) throws SQLException;

	/**
	 * <p>executeNoQueryBatch.</p>
	 *
	 * @param strSql a {@link java.lang.String} object.
	 * @param params a {@link java.lang.Iterable} object.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	int executeNoQueryBatch(String strSql, Iterable<Object[]> params) throws SQLException;

	/**
	 * <p>executeNoQueryBatch.</p>
	 *
	 * @param strSql a {@link java.lang.String} object.
	 * @param total a int.
	 * @param cur a {@link ParamsIterator.CurrentParameters} object.
	 * @param cn a {@link java.sql.Connection} object.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	int executeNoQueryBatch(String strSql, int total, ParamsIterator.CurrentParameters cur, Connection cn) throws SQLException;

	/**
	 * 读写分离的连接获取，readOnly=true for select, readOnly=false for
	 * update,insert,delete,create, alter etc.
	 *
	 * @param readOnly
	 *            readOnly=true for select, readOnly=false for
	 *            update,insert,delete,create, alter etc
	 * @return 返回只读链接
	 * @throws java.sql.SQLException
	 *             数据库出错
	 */
	Connection getConnection(boolean readOnly) throws SQLException;

    DataSource getDataSource();

    /**
	 * <p>setDataSource.</p>
	 *
	 * @param connManager a {@link javax.sql.DataSource} object.
	 */
	void setDataSource(DataSource connManager);

	/**
	 * <p>close.</p>
	 *
	 * @throws java.sql.SQLException if any.
	 */
	void close() throws SQLException;
}
