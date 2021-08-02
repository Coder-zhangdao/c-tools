package com.bixuebihui.db;

import com.bixuebihui.jdbc.BaseDao;
import com.bixuebihui.jdbc.SqlFilter;
import com.bixuebihui.jdbc.SqlSort;
import com.bixuebihui.jdbc.entity.CountObject;
import com.bixuebihui.jdbc.entity.CountValue;
import org.apache.commons.lang3.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: Administrator Date: 13-4-18 Time: 下午5:59 To
 * change this template use File | Settings | File Templates.
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class DbImpl<T, V> extends BaseDao<T, V> implements ActiveRecord<T>,
		SimpleStatement<T,V> {

	/**
	 * NOTE that: class field make this class can't use in spring as singleton!
	 */
	SqlSort orderStack = new SqlSort();
	SqlHelper filterStack = new SqlHelper();
	SqlLimit limit;
	String resultFields;


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActiveRecord<T> in(String field, Object[] values) {
		filterStack.in(field, values);
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> in(String field, Object values) {
		if(values instanceof Collection) {
			filterStack.in(field, (Collection) values);
		} else if(values instanceof SqlString) {
			filterStack.in(field, (SqlString)values);
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
	public ActiveRecord<T> ne(String field, Object value) {
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

	protected ActiveRecord<T> order(String field, int order) {
		orderStack.addSort(field, order == ORDER_DESC ? "desc" : "asc");
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
	public boolean insertAndReferences(T entity) {
		return false; // To change body of implemented methods use File |
		// Settings | File Templates.
	}

	/** {@inheritDoc} */
	@Override
	public boolean insert(List<T> entities) {
		return false; // To change body of implemented methods use File |
		// Settings | File Templates.
	}

	/** {@inheritDoc} */
	@Override
	public boolean insertOrUpdate(T entity) throws SQLException {
		V key = getId(entity);
		T obj =selectByKey(key);
		if(obj==null) {
			return insert(entity);
		}else{
			return updateByKey(entity);
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean insertOrUpdate(List<T> entity) {
		//super.getDbHelper().executeNoQueryBatch(strSql, params)
		return false; // To change body of implemented methods use File |
		// Settings | File Templates.
	}

	/** {@inheritDoc} */
	@Override
	public T findByKey(V id) throws SQLException {
		return selectByKey(id);
	}

	/** {@inheritDoc} */
	@Override
	public List<T> findAllByKeys(V[] ids) throws SQLException {
		return selectByKeys(ids);
	}

	/** {@inheritDoc} */
	@Override
	public boolean update(T entity) throws SQLException {
		return updateByKey(entity);
	}

	/** {@inheritDoc} */
	@Override
	public boolean updateAndReferences(T entity) {
		return false; // To change body of implemented methods use File |
		// Settings | File Templates.
	}

	/** {@inheritDoc} */
	@Override
	public boolean update(List<T> entities) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete(T entity) {
		return deleteByKey(getId(entity));
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<T> findAll() {
		try {
			SqlPocket p = this.getSql();
			String where = p.getCondition().toString();
			Object[] params = p.getParams().toArray();
			return select(where, params, parseOrder(), limit.getBegin(),
					limit.getEnd());
		} finally {
			clear();
		}
	}

	@Override
	public <K> List<K> findAll(Class<K> clz) {
		SqlPocket p = this.getSql();
		String where = p.getCondition().toString();
		Object[] params = p.getParams().toArray();
		String select = "select " + resultFields + " from " + getTableName() + " ";
		return select(select, where, parseOrder(), params, limit.getBegin(),
				limit.getEnd(), clz);
	}

	private void clear() {
		this.filterStack.clear();
		this.orderStack.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int countAll() {
		try {
			SqlPocket p = this.getSql();
			String where = p.getCondition().toString();
			Object[] params = p.getParams().toArray();
			return countWhere(where, params);
		} finally {
			clear();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T find() {
		try {
			SqlPocket p = this.getSql();
			String where = p.getCondition().toString();
			Object[] params = p.getParams().toArray();
			List<T> res = select(where, params, parseOrder(),
					SqlLimit.LIMIT_ONE.getBegin(), SqlLimit.LIMIT_ONE.getEnd());
			if (res.size() > 0) {
				return res.get(0);
			}
			return null;
		} finally {
			clear();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete() {
		try {
			SqlPocket p = this.getSql();
			String where = p.getCondition().toString();
			Object[] params = p.getParams().toArray();
			if (where.length() < 10) {
				return false;
			}
			String sql = "delete from " + this.getTableName() + " " + where;
			return 0 < this.getDbHelper().executeNoQuery(sql, params);
		} finally {
			clear();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String get(String field) {
		try {
			field = SqlFilter.transactSQLInjection(field);
			SqlPocket p = this.getSql();
			String where = p.getCondition().toString();
			Object[] params = p.getParams().toArray();
			String sql = "select " + field + " from " + this.getTableName()
					+ " " + where;
			Object obj = this.getDbHelper().executeScalar(sql, params);
			return obj == null ? null : obj.toString();
		} finally {
			clear();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SqlPocket getSql()  {
		return filterStack.build();
	}

	private String parseOrder() {
		return this.orderStack.toString();
	}

	// from basedao

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T mapRow(ResultSet rs, int index) {
		throw new NotImplementedException();
	}

	/** {@inheritDoc} */
	@Override
	public boolean updateByKey(T info)  {
		throw new NotImplementedException();
	}

	/** {@inheritDoc} */
	@Override
	public boolean insertDummy() throws SQLException {
		throw new NotImplementedException();
	}

	/** {@inheritDoc} */
	@Override
	public V getId(T info) {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void setId(T info, V id) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc} */
	@Override
	public V getNextKey() {
		return null;
	}

	@Override
	protected void setIdLong(T info, long id) {

	}

	/** {@inheritDoc} */
	@Override
	public String getTableName() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getKeyName() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public boolean insert(T entity) {
		throw new NotImplementedException();
	}


	/** {@inheritDoc} */
	@Override
	public List<Object> getVector(String field) {
		throw new NotImplementedException();
	}


	/** {@inheritDoc} */
	@Override
	public List<Long> getLongVector(String field) {
		throw new NotImplementedException();
	}


	/** {@inheritDoc} */
	@Override
	public Record<T> last() {
		return null;
	}

	@Override
	public ActiveRecord<T> greaterOrEqualThan(String field, Object value) {
		return null;
	}

	@Override
	public ActiveRecord<T> smallerOrEqualThan(String field, Object value) {
		return null;
	}


	/** {@inheritDoc} */
	@Override
	public SqlHelper getCondStack() {
	    return null;
	}


	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> or(SqlHelper andStack) {
		return null;
	}


	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> asc(String field) {
		return null;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActiveRecord<T> desc(String field) {
		return null;
	}


	/** {@inheritDoc} */
	@Override
	public List<String> getStringVector(String field) {
		return null;
	}


	/** {@inheritDoc} */
	@Override
	public ActiveRecord<T> alias(String shortName) {
		return null;
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
		return null;
	}


	/** {@inheritDoc} */
	@Override
	public CountValue countValue(String field, GroupFunction fun) {
		throw new NotImplementedException();
	}


	/** {@inheritDoc} */
	@Override
	public CountValue countSum(String field) {
		return countValue(field, GroupFunction.SUM);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean exists() {
		throw new NotImplementedException();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int inc(String field) {
		try {
			SqlPocket sqlPocket = this.getSql();
			String whereClause = sqlPocket.getCondition().toString();
			Object[] params = sqlPocket.getParams().toArray();

			return getDbHelper().executeNoQuery("update " + getTableName() + " set " + field + "=" + field + "+1 " + whereClause, params);
		} finally {
			clear();
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public int update(String[] fields, Object[] values) {
		throw new NotImplementedException();
	}

	/** {@inheritDoc} */
	@Override
	public int update(String fields, Object values) {
		throw new NotImplementedException();
	}


	/** {@inheritDoc} */
	@Override
	public <K> CountObject<K> countObject(String field, GroupFunction fun, Class<K> objectType)
			 {
		throw new NotImplementedException();
	}



}
