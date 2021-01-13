package com.bixuebihui.jdbc;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * <p>DbHelper class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class DbHelper implements IDbHelper {

	private Connection connection = null;

	private static final Log LOG = LogFactory.getLog(DbHelper.class);

	private DataSource dataSource = null;

	/**
	 * <p>Getter for the field <code>connection</code>.</p>
	 *
	 * @return a {@link java.sql.Connection} object.
	 * @throws java.sql.SQLException if any.
	 */
	@Override
	public Connection getConnection() throws SQLException {
		return connection != null ? connection : dataSource.getConnection();
	}

	/**
	 * {@inheritDoc}
	 *
	 * this method for subclass override
	 */
	@Override
	public Connection getConnection(boolean readOnly) throws SQLException {
		return getConnection();
	}


	/**
	 * <p>freeConnection.</p>
	 *
	 * @param cn a {@link java.sql.Connection} object.
	 * @throws java.sql.SQLException if any.
	 */
	public static void freeConnection(Connection cn) throws SQLException {
		cn.close();
	}

	/**
	 * <p>Constructor for DbHelper.</p>
	 */
	public DbHelper() {
		// use this constructor
	}

	/**
	 * for test only, not use in real application
	 *
	 * @param cn db connection
	 */
	public DbHelper(Connection cn) {
		connection = cn;
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}


	/**
	 * do not use this if you don't know what to do...
	 * because it leading to resource leaking
	 *
	 * @param sql sql
	 * @param cn connection
	 * @return ResultSet must closed after using
	 * @throws java.sql.SQLException db error
	 * @deprecated  ResultSet must closed, before close the connection, is not recommend to use this
	 */
	@Deprecated
	public ResultSet executeQuery(String sql, @NotNull Connection cn)
			throws SQLException {
		ResultSet rs;
		try (Statement stmt = cn.createStatement()) {
			rs = stmt.executeQuery(sql);
		} catch (SQLException ex) {
			dumpSql(ex,sql, null);
			throw ex;
		}
		return rs;
	}

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	@Override
	public int executeNoQuery(String sql) throws SQLException {
		Connection cn = this.getConnection(false);
		try (Statement stmt = cn.createStatement()){
			return stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			dumpSql(ex,sql, null);

			throw ex;
		} finally {
			close(cn);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.bixuebihui.zichan.dal.IDbHelper#executeNoQuery(java.lang.String[])
	 */

	/**
	 * public java.sql.Result exeSql(String strSQl) {
	 *
	 * return new Result(); }
	 *
	 * @param stmt statement to be closed
	 */
	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException sqlexception) {
				LOG.error(sqlexception);
			}
		}
	}

	/**
	 * <p>close.</p>
	 *
	 * @param rs a {@link java.sql.ResultSet} object.
	 */
	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException sqlexception) {
				LOG.error(sqlexception);
			}
		}
	}

	// 用于事务处理

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @param cn a {@link java.sql.Connection} object.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	public int executeNoQuery(String sql, @NotNull  Connection cn) throws SQLException {
		try (Statement stmt = cn.createStatement()){
			return stmt.executeUpdate(sql);
		} catch (SQLException ex) {
			dumpSql(ex,sql, null);
			throw ex;
		}
	}

	// 用于事务处理

	/**
	 * <p>close.</p>
	 *
	 * @param conn a {@link java.sql.Connection} object.
	 */
	public static void close(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				freeConnection(conn);
			}
		} catch (SQLException sqlexception) {
			LOG.error(sqlexception);
		}
	}

	private static void dumpSql(SQLException ex, String sql, Object[] params) {
		LOG.warn(ex.getMessage());
		LOG.warn("==="+sql);
		if(params!=null) {
			for (Object o : params) {
				LOG.warn("===" + o);
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public int executeNoQueryBatch(String strSql, Iterable<Object[]> params) throws SQLException {
		return executeNoQueryBatch(strSql, params, null);
	}

	/** {@inheritDoc} */
	@Override
	public int executeNoQueryBatch(String strSql, int total, ParamsIterator.CurrentParameters cur, Connection cn) throws SQLException {
		return executeNoQueryBatch(strSql, new ParamsIterator(total, cur), cn);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.bixuebihui.zichan.dal.IDbHelper#executeQuery(java.lang.String,
	 * com.bixuebihui.util.db.RowCallbackHandler)
	 */

	private static void dumpSql(SQLException ex, String[] sql) {
		LOG.warn(ex.getMessage());
		for(String s:sql) {
			LOG.warn("=="+s);
		}
	}

	/** {@inheritDoc} */
	@Override
	public Object executeScalar(String strSql) throws SQLException {
		return executeScalarSession(strSql,null);
	}

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param sql an array of {@link java.lang.String} objects.
	 * @return a count of influenced records.
	 * @throws java.sql.SQLException if any.
	 */
	@Override
	public int executeNoQuery(String[] sql) throws SQLException {
		Connection cn = this.getConnection(false);
		cn.setAutoCommit(false);
		int n = 0;
		try(Statement stmt = cn.createStatement() ){
			for (String s :sql) {
				if (StringUtils.isNotBlank(s)) {
					n += stmt.executeUpdate(s);
				}
			}
			cn.commit();
		} catch (SQLException ex) {
			cn.rollback();
			dumpSql(ex,sql);
			throw ex;
		} finally {
			try {
				cn.setAutoCommit(true);
			} catch (SQLException e) {
				LOG.warn(e);
			}
			close(cn);
		}
		return n;
	}


	/**
	 * 批量在事条里执行SQL语句数组
	 *
	 * @param sqls 批量在事条里执行SQL语句数组
	 * @return 每一个SQL执行结果的数组
	 * @throws java.sql.SQLException 数据库出错
	 */
	public int[] executeUpdate(String[] sqls) throws SQLException {

		if (sqls==null || sqls.length < 1) {
			return new int[0];
		}

		Connection cn= getConnection(false);
		cn.setAutoCommit(false);
		int[] j=new int[sqls.length];
		int step=-1;
		try (Statement oStmt = cn.createStatement()){
			for (int i = 0; i < sqls.length; i++) {
				j[i] = oStmt.executeUpdate(sqls[i]); //NOSONAR
				step =i;
			}
			cn.commit();
		} catch (SQLException e) {
			cn.rollback();

			SQLException ex = new SQLException("执行SQL语句出错"
					+ (step < 0 ? "" : ":" + sqls[step])
					+ " DbHelper.ExecuteUpdate" + e.getMessage());
			dumpSql(ex, sqls);

			throw ex;

		} finally {
			close(cn);
		}
		return j;
	}


	/** {@inheritDoc} */
	@Override
	public @NotNull
	List<Map<String, Object>> exeQuery(String sql) throws SQLException {
		Connection cn = getConnection(true);
		try (Statement stmt = cn.createStatement();
			 ResultSet rset = stmt.executeQuery(sql) ){
			return resultSet2Vector(rset);
		} catch (SQLException ex) {
			dumpSql(ex,sql,null);
			throw ex;
		} finally {
			close(cn);
		}
	}

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param strSql an array of {@link java.lang.String} objects.
	 * @param cn a {@link java.sql.Connection} object.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	@Override
	public int executeNoQuery(String[] strSql, @NotNull  Connection cn)
			throws SQLException {
		int n = 0;
		try (Statement stmt = cn.createStatement()){
			for (String s : strSql) {
				if (!StringUtils.isNotBlank(s)) {
					continue;
				}
				n += stmt.executeUpdate(s);
			}
		} catch (SQLException ex) {
			dumpSql(ex,strSql);
			throw ex;
		}
		return n;
	}

	public static String clob2Str(Clob clobObject) throws SQLException, IOException {
		InputStream in = clobObject.getAsciiStream();
		StringWriter w = new StringWriter();
		IOUtils.copy(in, w, Charset.defaultCharset());
		return w.toString();
	}

	/** {@inheritDoc} */
	@Override
	public int executeNoQuery(String sql, Object[] params, @NotNull Connection cn)
			throws SQLException {

		try (Statement p = ArrayUtils.isEmpty(params) ? cn.createStatement() : cn.prepareStatement(sql)){
			if (p instanceof PreparedStatement) {
				PreparedStatement pre = (PreparedStatement) p;
				this.fillParameter((PreparedStatement) p, params, null);
				return  pre.executeUpdate();
			} else {
				return  p.executeUpdate(sql);
			}
		} catch (SQLException ex) {
			dumpSql(ex,sql, params);
			throw ex;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * 批处里数组, 用于Insert, delete或update
	 */
	@Override
	public int executeNoQueryBatch(String sql, Iterable<Object[]> params,
								   Connection cn) throws SQLException {

		boolean needRelease = false;
		if (cn == null) {
			cn = this.getConnection(false);
			needRelease = true;
		}
		int n;
		try (PreparedStatement p = cn.prepareStatement(sql) ){
			for (Object[] param : params) {
				this.fillParameter(p, param, null);
				p.addBatch();
			}
			int[] updateCounts = p.executeBatch();
			n = updateCounts.length;
		} catch (SQLException ex) {
			for(Object[] ps:params) {
				dumpSql(ex,sql, ps);
			}
			throw ex;
		} finally {
			if (needRelease) {
				close(cn);
			}
		}
		return n;
	}

	/** {@inheritDoc} */
	@Override
	public void executeQuery(String sql, RowCallbackHandler handle)
			throws SQLException {
		Connection cn = this.getConnection(true);
		try (Statement stmt = cn.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)){
			while (rs.next()) {
				handle.processRow(rs);
			}
		} catch (SQLException ex) {
			dumpSql(ex, sql, null);
			throw ex;
		} finally {
			close(cn);
		}

	}

	/**
	 * (non-Javadoc)
	 *
	 * @param cn connection
	 * @param sql sql语句
	 * @throws java.sql.SQLException db error
	 * @return single object, may be integer or string
	 * @see IDbHelper#executeScalar(java.lang.String)
	 */
	public Object executeScalarSession(String sql, Connection cn) throws SQLException {

		boolean localCn = cn==null;

		cn = localCn ?  this.getConnection(true):cn;
		try(
				Statement stmt = cn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)){
			if (rs.next()) {
				return  rs.getObject(1);
			}
			return null;
		} catch (SQLException ex) {
			dumpSql(ex, sql, null);
			throw ex;
		} finally {
			if(localCn) {
				close(cn);
			}
		}
	}

	/**
	 * <p>resultSet2Vector.</p>
	 *
	 * @param rs a {@link java.sql.ResultSet} object.
	 * @return a {@link java.util.List} object.
	 * @throws java.sql.SQLException if any.
	 */
	public @NotNull
	List<Map<String, Object>> resultSet2Vector(ResultSet rs) throws SQLException {
		List<Map<String, Object>> result = new ArrayList<>();
		//System.out.println("No data"); //http://stackoverflow.com/a/6813771/1484621
		if (!rs.isBeforeFirst() ) {
			return result;
		}

		ResultSetMetaData rsmdQuery = rs.getMetaData();
		int iColumnNumber = rsmdQuery.getColumnCount();

		while (rs.next()) {
			Map<String, Object> mapColumn = new HashMap<>(32);
			for (int i = 1; i <= iColumnNumber; i++) {

				String colTypeName = rsmdQuery.getColumnTypeName(i).toUpperCase();

				String colName = rsmdQuery.getColumnName(i);

				if(!StringUtils.isBlank(rsmdQuery.getColumnLabel(i))) {
					colName = rsmdQuery.getColumnLabel(i);
				}

				if(mapColumn.containsKey(colName) && rs.getObject(i)==null){
					//同一列名的已存在且不为空，则保留当前值，用于多表联合查询，但后表无记录的情况
					continue;
				}

				switch (colTypeName) {
					case "NUMBER":
					case "INT":
					case "INTEGER":
					case "TINYINT":
					case "TINYINT UNSIGNED":
					case "INTEGER IDENTITY":
					case "COUNTER":
					case "NUMERIC":
					case "DECIMAL":
					case "MEDIUMINT":
					case "INT UNSIGNED":
					case "MEDIUMINT UNSIGNED":

						dealNumber(rs, rsmdQuery, mapColumn, i, colTypeName, colName);

						break;
					case "BIGINT":
					case "BIGINT UNSIGNED":
						mapColumn.put(colName,
								rs.getLong(i));
						break;
					case "SMALLINT":
						mapColumn.put(colName,
								rs.getInt(i));
						break;
					case "VARCHAR2":
					case "VARCHAR":
					case "NVARCHAR2":
					case "NVARCHAR":
					case "TEXT":
					case "MEDIUMTEXT":
					case "LONGTEXT":
					case "CHAR":
					case "NTEXT":
					case "NCHAR":
					case "LONGVARCHAR":
					case "LONGCHAR": {
						String temp = rs.getString(i);
						if (temp == null) {
							temp = "";
						}
						mapColumn.put(colName, temp);
						break;
					}
					case "DATETIME":
					case "TIMESTAMP":
						// 10.17
						if (rs.getTimestamp(i) != null) {
							mapColumn.put(colName, rs.getTimestamp(i));
						} else {
							// 1970 year
							Timestamp d1970 = new Timestamp(0);
							mapColumn.put(colName, d1970);
						}
						break;
					case "DATE":
						java.util.Date dt = rs.getDate(i);
						if (dt != null) {
							Timestamp tm = new Timestamp(
									dt.getTime());
							mapColumn.put(colName, tm);
						} else {
							Timestamp d1970 = new Timestamp(0);
							mapColumn.put(colName, d1970);
						}

						break;
					case "LONG": {
						String temp = "";
						byte[] mybyte = rs.getBytes(i);
						if (mybyte != null) {
							temp = new String(mybyte, Charset.defaultCharset());
						}
						mapColumn.put(colName, temp);
						break;
					}
					case "RAW":
					case "VARCHAR () FOR BIT DATA":
						byte[] bb = rs.getBytes(i);
						mapColumn.put(colName, bb == null ? new byte[0]
								: bb);
						break;
					case "CLOB": {
						String temp = null;
						try {
							temp = clob2Str(rs.getClob(i));
						} catch (IOException e) {
							LOG.error(e);
						}
						mapColumn.put(colName, temp);
						break;
					}
					case "BOOL":
					case "BIT": {
						Boolean temp = rs.getBoolean(i);
						mapColumn.put(colName, temp);
						break;
					}
					case "FLOAT": {
						Float temp = rs.getFloat(i);
						mapColumn.put(colName, temp);
						break;
					}
					case "DOUBLE": {
						Double temp = rs.getDouble(i);
						mapColumn.put(colName, temp);
						break;
					}
					default:
						mapColumn.put(colName, rs.getObject(i));
						String string = " -colName:";
						LOG.warn(
								"Unknow data type, ask xwx@live.cn for adding this to DbHelper.resultSet2Vector colTypeName: "
										+ colTypeName + string + colName);
						break;
				}
			}
			result.add(mapColumn);
		}
		return result;
	}

	private void dealNumber(ResultSet rset, ResultSetMetaData rsmdQuery, Map<String, Object> columnMap, int i, String colTypeName, String colName) throws SQLException {
		if("TINYINT".equals(colTypeName) && rsmdQuery.getPrecision(i)==1) {
			columnMap.put(colName,	rset.getBoolean(i));
		} else if("TINYINT".equals(colTypeName) || "TINYINT UNSIGNED".equals(colTypeName)) {
			columnMap.put(colName,	rset.getByte(i));
		} else if (rsmdQuery.getScale(i) <= 0) {
			// BigDecimal or Integer
			if (rset.getObject(i) instanceof BigDecimal) {
				BigDecimal a = (BigDecimal) rset.getObject(i);
				if (a.scale() > 0
						|| a.compareTo(new BigDecimal(
						Integer.MAX_VALUE)) > 0) {
					columnMap.put(colName,
							rset.getObject(i));
				} else {
					columnMap.put(colName,
							rset.getInt(i));
				}
			}else if (rsmdQuery.getPrecision(i)>9){
				columnMap.put(colName,
						rset.getLong(i));
			}else {
				columnMap.put(colName,
						rset.getInt(i));
			}
		} else {
			columnMap.put(colName,
					rset.getDouble(i));

		}
	}

	/**
	 * <p>executeScalar.</p>
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.sql.SQLException if any.
	 */
	@Override
	public Object executeScalar(String sql, Object[] params)
			throws SQLException {
		return executeScalar(sql, params, null);
	}


	/**
	 * <p>fillParameter.</p>
	 *
	 * @param p a {@link java.sql.PreparedStatement} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @throws java.sql.SQLException if any.
	 */
	public void fillParameter(PreparedStatement p, Object[] params)
			throws SQLException {
		fillParameter(p, params, null);
	}

	/** {@inheritDoc} */
	@Override
	public Object executeScalar(String sql, Object[] params, Connection cn)
			throws SQLException {
		if (params == null || params.length == 0) {
			return executeScalarSession(sql, cn);
		}

		boolean localCn = cn==null;

		cn= localCn? this.getConnection(true): cn;

		try (PreparedStatement p = cn.prepareStatement(sql) ){
			fillParameter(p, params, null);
			try(ResultSet rs = p.executeQuery()) {
				if (rs.next()) {
					return rs.getObject(1);
				}
			}
		} catch (SQLException ex) {
			dumpSql(ex, sql, params);
			throw ex;
		} finally {
			if(localCn) {
				close(cn);
			}
		}
		return null;
	}

	/**
	 * <p>fillParameter.</p>
	 *
	 * @param p a {@link java.sql.PreparedStatement} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @param targetSqlTypes an array of int objects.
	 * @throws java.sql.SQLException if any.
	 */
	public void fillParameter(PreparedStatement p, Object[] params,
							  int[] targetSqlTypes) throws SQLException {

		if (ArrayUtils.isEmpty(params)) {
			return;
		}

		if (ArrayUtils.isNotEmpty(targetSqlTypes) && targetSqlTypes.length != params.length) {
			throw new SQLException(
					"Intenal error: The params and the targetSqlTypes must have same length!");
		}

		if (ArrayUtils.isNotEmpty(targetSqlTypes)) {
			for (int i = 1; i <= params.length; i++) {
				p.setObject(i, params[i - 1], targetSqlTypes[i - 1]);
			}
		} else {
			for (int i = 1; i <= params.length; i++) {
				Object pa = params[i - 1];
				if (pa == null) {
					p.setNull(i, java.sql.Types.VARCHAR, "String");
					LOG.debug("不支持空数据(参数["
							+ i
							+ "])! Null is not support in DbHelper.executeNoQuery(String sql, Object[] params), there may be error!");
				} else if (pa instanceof Integer) {
					p.setInt(i, (Integer) pa);
				} else if (pa instanceof Long) {
					p.setLong(i, (Long) pa);
				} else if (pa instanceof java.sql.Timestamp) {
					p.setTimestamp(i, (Timestamp) pa);
				} else if (pa instanceof java.sql.Date) {
					p.setDate(i, (Date) pa);
				} else if (pa instanceof java.util.Date) {
					p.setDate(i, new Date(((java.util.Date) pa).getTime()));
				} else if (pa instanceof Double) {
					p.setDouble(i, (Double) pa);
				} else if (pa instanceof String) {
					p.setString(i, ((String) pa));
				} else if (pa instanceof ClobString) {
					p.setObject(i, pa.toString());
				} else {
					p.setObject(i, pa);
				}
			}
		}
	}

	/**
	 * 用于执行带参数的非查询语句
	 *
	 * @param sql 参数化sql语句
	 * @param params 参数
	 * @return (字段)) 结果集
	 * @throws java.sql.SQLException 数据库出错
	 */
	@Override
	public List<Map<String, Object>> executeQuery(String sql, Object[] params) throws SQLException {
		Connection cn = getConnection(true);
		ResultSet rs = null;

		try (Statement p = ArrayUtils.isEmpty(params) ? cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY) : cn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY)
		) {
			if (p instanceof PreparedStatement) {
				fillParameter((PreparedStatement) p, params);
				rs = ((PreparedStatement) p).executeQuery();
			} else {
				rs = p.executeQuery(sql);
			}
			return this.resultSet2Vector(rs);

		} catch (SQLException ex) {
			dumpSql(ex, sql, params);
			throw ex;
		} finally {
			close(rs);
			close(cn);
		}

	}

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @param targetSqlTypes an array of int objects.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	@Override
	public int executeNoQuery(String sql, Object[] params, int[] targetSqlTypes)
			throws SQLException {
		return executeNoQuery(sql, params, targetSqlTypes, null);

	}

	/** {@inheritDoc} */
	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/** {@inheritDoc} */
	@Override
	public int executeNoQuery(String sql, Object[] params,
							  int[] targetSqlTypes, Connection conn) throws SQLException {
		boolean localCn  = conn == null;
		Connection cn =  localCn ? getConnection(false) : conn;
		try (Statement p = ArrayUtils.isNotEmpty(params)? cn.prepareStatement(sql) : cn.createStatement()){
			if (p instanceof PreparedStatement) {
				fillParameter((PreparedStatement) p, params, targetSqlTypes);
				((PreparedStatement) p).execute();
			} else {
				p.execute(sql);
			}
			return p.getUpdateCount();
		} catch (SQLException ex) {
			dumpSql(ex, sql, params);
			throw ex;
		} finally {
			if(localCn) {
				close(cn);
			}
		}


	}

	/**
	 * <p>executeNoQuery.</p>
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	@Override
	public int executeNoQuery(String sql, Object[] params) throws SQLException {

		return executeNoQuery(sql, params, new int[0]);

	}

	/**
	 * <p>executeQuery.</p>
	 *
	 * @param sql a {@link java.lang.String} object.
	 * @param params an array of {@link java.lang.Object} objects.
	 * @param handle a {@link RowCallbackHandler} object.
	 * @throws java.sql.SQLException if any.
	 */
	@Override
	public void executeQuery(String sql, Object[] params,
							 RowCallbackHandler handle) throws SQLException {
		Connection cn = getConnection(true);
		try (Statement p = ArrayUtils.isNotEmpty(params)? cn.prepareStatement(sql) : cn.createStatement()){
			if (p instanceof PreparedStatement) {
				fillParameter((PreparedStatement) p, params, null);
			}
			try(ResultSet rs = (p instanceof PreparedStatement) ? ((PreparedStatement) p).executeQuery(): p.executeQuery(sql)) {
				while (rs.next()) {
					handle.processRow(rs);
				}
			}
		}catch(SQLException ex){
			dumpSql(ex, sql, params);
			throw ex;
		}finally {
			DbUtils.closeQuietly(cn);
		}

	}

	/** {@inheritDoc} */
	@Override
	public @NotNull <T> List<T> executeQuery(String sql, Object[] params,
											 @NotNull RowMapperResultReader<T> handle) throws SQLException {
		Connection cn = getConnection(true);
		try (Statement p = ArrayUtils.isNotEmpty(params)? cn.prepareStatement(sql) : cn.createStatement()){
			if (p instanceof PreparedStatement) {
				fillParameter((PreparedStatement) p, params, null);
			}
			try(ResultSet rs = (p instanceof PreparedStatement) ? ((PreparedStatement) p).executeQuery(): p.executeQuery(sql)) {
				return handle.processResultSet(rs);
			}
		}catch(SQLException ex){
			//可能超时，输出sql和参数
			dumpSql(ex, sql, params);
			throw ex;
		}finally {
			DbUtils.closeQuietly(cn);
		}

	}

}
