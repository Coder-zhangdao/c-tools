package com.bixuebihui.db;

import com.bixuebihui.jdbc.*;
import com.bixuebihui.jdbc.entity.CountObject;
import com.bixuebihui.jdbc.entity.CountValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: Administrator Date: 13-4-18 Time: 下午5:57 To
 * change this template use File | Settings | File Templates.
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class ActiveRecordImpl<T, V> implements ActiveRecord<T> {

	private static final Log log = LogFactory.getLog(ActiveRecordImpl.class);
	/**
	 * NOTE that: class field make this class can't use in spring as singleton!
	 */
	SqlSort orderStack;
	SqlHelper filterStack;
	SqlLimit limit;
	BaseDao<T, V> operator;
	Connection cn;
	private SqlPocket sqlPocket;
	private boolean useLast=false;

	private String tableAlias;
	private String resultFields =" * ";
	private List<String> joins;


	private void init(BaseDao<T, V> operator){
		this.operator = operator;
		orderStack = new SqlSort();
		filterStack = new SqlHelper();
		limit = SqlLimit.LIMIT_MAX;

	}

	/**
	 * <p>Constructor for ActiveRecordImpl.</p>
	 *
	 * @param operator a {@link BaseDao} object.
	 */
	public ActiveRecordImpl(BaseDao<T, V> operator) {
		init(operator);
	}

	/**
	 * <p>Constructor for ActiveRecordImpl.</p>
	 *
	 * @param operator a {@link BaseDao} object.
	 * @param cn a {@link java.sql.Connection} object.
	 */
	public ActiveRecordImpl(BaseDao<T, V> operator, Connection cn) {
		this.cn = cn;
		init(operator);
	}


	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> in(String field, Object[] values) {
		filterStack.in(field, values);
		return this;
	}


	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> in(String field, Object values) {
		if(!(values instanceof Collection) && !(values instanceof SqlString)){
			throw new  IllegalArgumentException("InFilter values must be Collection or SqlString objects");
		}
		if (values instanceof Collection) {
			filterStack.in(field, (Collection) values);
		} else {
			filterStack.in(field, (SqlString) values);
		}

		return this;
	}


	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> like(String field, String value) {
		filterStack.like(field, value);
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> startWith(String field, String value) {
		filterStack.startWith(field, value);
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> eq(String field, Object value) {
		filterStack.eq(field, value);
		return this;
	}


	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> ne(String field, Object value){
		filterStack.ne(field, value);
		return this;
	}


	/** {@inheritDoc} */

	@Override
	public ActiveRecord<T> eq(String[] fields, Object[] value)
			throws SQLException {
		filterStack.eq(fields, value);
		return this;
	}


	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> greaterThan(String field, Object value) {
		filterStack.greaterThan(field, value);
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> smallerThan(String field, Object value) {
		filterStack.smallerThan(field, value);
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public SqlHelper getCondStack(){
		return filterStack;
	}

	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> or(SqlHelper andStack){
		filterStack.or(andStack);
		return this;
	}

	protected ActiveRecord<T> order(String field, int order) {
		orderStack.addSort(field, order == ORDER_DESC ? SqlSort.Sort.DESC	: SqlSort.Sort.ASC);
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> limit(int begin, int num) {
		limit = new SqlLimit(begin, num);
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public List<T> findAll() throws SQLException {
		try {
			sqlPocket = this.getSql();
			String where = formWhereClause();
			Object[] params = sqlPocket.getParams().toArray();

			return operator.selectWithJoin(resultFields, where, params, parseOrder(),
					limit.getBegin(), limit.getEnd());
		} finally {
			clear();
		}
	}

	/**
	 * <p>formWhereClause.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected String formWhereClause() {
		String res = sqlPocket.getCondition().toString();
		if(StringUtils.isNotBlank(this.tableAlias) ){
			StringBuilder join =new StringBuilder();
			if(joins !=null && joins.size()>0){
				for(String t : joins) {
					join.append(t);
				}
			}
			res = " "+this.tableAlias +" "+join+" "+res;
		}
		return res;
	}

	private void clear() {
		this.filterStack.clear();
		this.orderStack.clear();
		this.joins =null;
		this.limit = SqlLimit.LIMIT_MAX;
		useLast = false;
		this.cn = null;
	}


	/** {@inheritDoc} */
	@Override
	public T find() throws SQLException {
		try {
			sqlPocket = this.getSql();
			String where = formWhereClause();
			Object[] params = sqlPocket.getParams().toArray();
			List<T> res = operator.selectWithJoin(resultFields, where, params, parseOrder(),
					SqlLimit.LIMIT_ONE.getBegin(), SqlLimit.LIMIT_ONE.getEnd());
			if (res.size() > 0) {
				return res.get(0);
			}
			return null;
		} finally {
			clear();
		}

	}

	/** {@inheritDoc} */
	@Override
	public boolean delete() throws SQLException {
		try {
			sqlPocket = this.getSql();
			String where = formWhereClause();
			Object[] params = sqlPocket.getParams().toArray();
			if (where.length() < 10) {
				return false;
			}
			String sql = "delete from " + operator.getTableName() + " " + where;

			if(limit!=null && limit.getBegin()==0 && limit.getEnd()>0) {
				sql +=" limit "+limit.getEnd();
			}
			if(cn==null) {
				return 0 < operator.getDbHelper().executeNoQuery(sql, params);
			}
			return 0 < operator.getDbHelper().executeNoQuery(sql, params, cn);
		} finally {
			clear();
		}
	}

	/** {@inheritDoc} */
	@Override
	public Object get(String field) throws SQLException {
		try {
			String sql = getVectorSql(field);
			Object[] params = sqlPocket.getParams().toArray();
			if(cn==null) {
				return operator.getDbHelper().executeScalar(sql, params);
			}
			return operator.getDbHelper().executeScalar(sql, params,cn);
		} finally {
			clear();
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<Object> getVector(String field) throws SQLException {
		try {
			String sql = getVectorSql(field);
			Object[] params = sqlPocket.getParams().toArray();
			final List<Object> res = new ArrayList<>();
			RowCallbackHandler handle = resultSet -> res.add(resultSet.getObject(1));
			operator.getDbHelper().executeQuery(sql, params, handle );

			return res;
		} finally {
			clear();
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<Long> getLongVector(String field) throws SQLException {
		try {
			String sql = getVectorSql(field);
			Object[] params = sqlPocket.getParams().toArray();
			final List<Long> res = new ArrayList<>();

			operator.getDbHelper().executeQuery(sql, params, new LongReader(res) );

			return res;
		} finally {
			clear();
		}
	}

	private String parseLimitSql() throws SQLException{
		if(operator.getDBTYPE()==BaseDao.MYSQL){

			if(limit==null) {
				limit = SqlLimit.LIMIT_ONE;
			}

			return limit.toString();
		}else{
			log.warn("limit not implemented for this type of BaseDao.getDBTYPE()="+operator.getDBTYPE());
			return "";
		}
	}


	private String getVectorSql(String field) throws SQLException {
		field = SqlFilter.transactSQLInjection(field);
		sqlPocket = this.getSql();
		String where = formWhereClause();
		where += " "+ parseOrder();
		where += " "+parseLimitSql();

		return "select " + field + " from " + operator.getTableName()
				+ " " + where;
	}

	/** {@inheritDoc} */
	@Override
	public SqlPocket getSql() throws SQLException {
		if(useLast && sqlPocket!=null) {
			return sqlPocket;
		}
		sqlPocket = filterStack.build();
		return sqlPocket;
	}

	private String parseOrder() {
		return this.orderStack.toString();
	}

	/** {@inheritDoc} */
	@Override
	public int count() throws SQLException {
		try {
			sqlPocket = this.getSql();
			String where = formWhereClause();
			Object[] params = sqlPocket.getParams().toArray();
			return operator.countWhere(where, params);
		} finally {
			clear();
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean exists() throws SQLException {
		try {
			sqlPocket = this.getSql();
			String where = formWhereClause();
			Object[] params = sqlPocket.getParams().toArray();
			return operator.exists(where, params);
		} finally {
			clear();
		}
	}


	/**
	 * <p>Constructor for ActiveRecordImpl.</p>
	 *
	 * @param src a {@link ActiveRecordImpl} object.
	 */
	public ActiveRecordImpl(ActiveRecordImpl src) {
		this.filterStack = new SqlHelper(src.filterStack);
		this.limit = new SqlLimit(src.limit);
		this.orderStack = new SqlSort(this.orderStack);
	}

	/** {@inheritDoc} */
	@Override
	public Record<T> last() {
		if(sqlPocket==null){
			log.error("ERROR: ActiveRecordImpl.last() - there not conditions set to use last.");
		}
		useLast = true;
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> asc(String field) {
		return this.order(field, ORDER_ASC);
	}

	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> desc(String field) {
		return this.order(field, ORDER_DESC);
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getStringVector(String field) throws SQLException {
		try {
			String sql = getVectorSql(field);
			Object[] params = sqlPocket.getParams().toArray();
			final List<String> res = new ArrayList<>();
			RowCallbackHandler handle = resultSet -> res.add(resultSet.getString(1));
			operator.getDbHelper().executeQuery(sql, params, handle );

			return res;
		} finally {
			clear();
		}
	}

	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> alias(String shortName) {
		tableAlias = shortName;
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> fields(String resultFields) {
		this.resultFields = resultFields;
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> join(String joinClause) {
		if (joins == null) {
			joins = new ArrayList<>();
		}
		joins.add(joinClause);
		return this;
	}

	/** {@inheritDoc} */
	@Override
	@Deprecated
	public CountValue countSum(String field) throws SQLException {
		 return countValue(field, GroupFun.SUM);
	}

	/** {@inheritDoc} */
	@Override
	@Deprecated
	public CountValue countValue(String field, GroupFun fun)
			throws SQLException {
		try {
			sqlPocket = this.getSql();

			String where = formWhereClause();

			Object[] params = sqlPocket.getParams().toArray();

			List<CountValue> res =  operator.countGroupValue(field, fun.toString(), where,
					null, null,params);

			return res.size()==0 ? new CountValue(): res.get(0);
		} finally {
			clear();
		}
	}

	/** {@inheritDoc} */
	@Override
	public int inc(@NotNull String field) throws SQLException {
		try {
			sqlPocket = this.getSql();
			String whereClause = formWhereClause();
			Object[] params = sqlPocket.getParams().toArray();
			String sql ="update "+operator.getTableName()
					+" set "+field+"="+field+"+1 "+ whereClause;
			if(cn==null) {
				return operator.getDbHelper().executeNoQuery(sql, params);
			}
			return operator.getDbHelper().executeNoQuery(sql, params,cn);
		} finally {
			clear();
		}

	}

	/** {@inheritDoc} */
	@Override
	public int update(@NotNull String[] fields, @NotNull Object[] values) throws SQLException {
		try {
			if( fields.length != values.length) {
				throw new IllegalArgumentException("fields and values must have some length");
			}

			sqlPocket = this.getSql();
			String whereClause = formWhereClause();

			List<Object> params = new ArrayList<>();
			StringBuilder sb = new StringBuilder();
			int i=0;
			for(String field:fields){
				Object val = values[i];
				if(val instanceof SqlString){
					sb.append(field).append(" = ").append(val.toString());
				}else{
					sb.append(field).append(" = ? ");
					params.add(val);
				}
				i++;
				if(i<fields.length) {
					sb.append(",");
				}
			}

			Collections.addAll(params, sqlPocket.getParams().toArray());

			String sql ="update "+operator.getTableName()+" set "+
					sb.toString()+ whereClause;
			if(cn==null) {
				return operator.getDbHelper().executeNoQuery(sql, params.toArray());
			}
			return operator.getDbHelper().executeNoQuery(sql, params.toArray(), cn);
		} finally {
			clear();
		}
	}

	/** {@inheritDoc} */
	@Override
	public int update(@NotNull  String fields, Object values) throws SQLException {
		return update(new String[]{fields}, new Object[]{values});
	}

	/** {@inheritDoc} */
	@Override
	public <K> CountObject<K> countObject(String field, GroupFun fun, Class<K> objectType)
			throws SQLException {
		try {
			sqlPocket = this.getSql();

			String where = formWhereClause();

			Object[] params = sqlPocket.getParams().toArray();

			List<CountObject<K>> res =  operator.countGroupObject(field, fun.toString(), where,
					null, null, objectType, params);

			return res.size() == 0 ? new CountObject<>() : res.get(0);
		} finally {
			clear();
		}
	}



}
