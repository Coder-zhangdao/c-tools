package com.bixuebihui.db;

import com.bixuebihui.jdbc.BaseDao;
import com.bixuebihui.jdbc.ISqlConditionType;
import com.bixuebihui.jdbc.SqlFilter;
import org.apache.commons.lang3.StringUtils;

import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

/**
 * <p>SqlHelper class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class SqlHelper {

	List<BaseFilter> filters;

	/**
	 * <p>Constructor for SqlHelper.</p>
	 *
	 * @param src a {@link SqlHelper} object.
	 */
	public SqlHelper(SqlHelper src) {
		this.filters = new ArrayList<>();
		this.filters.addAll(src.filters);
		if(src.orCond!=null) {
			this.orCond = new ArrayList<>();
			this.orCond.addAll(src.orCond);
		}
		if(src.notCond!=null) {
			this.notCond = new ArrayList<>();
			this.notCond.addAll(src.notCond);
		}
		this.databaseType = src.databaseType;
		this.useNullAsCondition = src.useNullAsCondition;
	}
	private int databaseType = BaseDao.MYSQL;

	List<SqlHelper> orCond = null;
	List<SqlHelper> notCond = null;

	private boolean useNullAsCondition = false;

	/**
	 * <p>Constructor for SqlHelper.</p>
	 */
	public SqlHelper() {
		filters = new ArrayList<>();
	}

	/**
	 * <p>toCondition.</p>
	 *
	 * @return a {@link SqlPocket} object.
	 * @throws java.sql.SQLException if any.
	 */
	protected SqlPocket toCondition() throws SQLException {
		SqlPocket criteria = new SqlPocket();
		if (filters == null || filters.size() <= 0) {
            return criteria;
        }
		for (BaseFilter filter : filters) {
			buildCriteria(criteria, filter);
		}

		StringBuffer condition = criteria.getCondition();
		removeLeadAnd(condition);

		if (orCond != null) {
			condition.insert(0, "(").append(")");
			for (SqlHelper it : orCond) {
				SqlPocket cond = it.toCondition();
				condition.append(" or (")
						.append(trimWhere(cond.getCondition())).append(")");
				criteria.getParams().addAll(cond.getParams());
			}
		}
		if (notCond != null) {
			condition.insert(0, "(").append(")");
			for (SqlHelper it : notCond) {
				SqlPocket cond = it.toCondition();

				condition.append(" and not (")
						.append(trimWhere(cond.getCondition())).append(")");
				criteria.getParams().addAll(cond.getParams());
			}
		}
		if (condition.length() > 0) {
			removeLeadAnd(condition);
			condition.insert(0, " where ");
		}
		return criteria;
	}

	private void removeLeadAnd(StringBuffer condition) {
		if (condition.length() > 0 && condition.indexOf(" and ") == 0) {
			condition.delete(0, 5);
		}
	}

	/**
	 * <p>trimWhere.</p>
	 *
	 * @param trimCond a {@link java.lang.StringBuffer} object.
	 * @return a {@link java.lang.StringBuffer} object.
	 */
	protected StringBuffer trimWhere(StringBuffer trimCond) {
		if(trimCond.indexOf(" where ")==0) {
            trimCond.delete(0, 7);
        }
		return trimCond;
	}


	/**
	 * <p>build.</p>
	 *
	 * @return a {@link SqlPocket} object.
	 * @throws java.sql.SQLException if any.
	 */
	public SqlPocket build() throws SQLException {
		return toCondition();
	}

	private void buildCriteria(SqlPocket criteria, BaseFilter f)
			throws SQLException {
		if (f.value != null || useNullAsCondition || f instanceof NullFilter ) {
			f.build(criteria);
		}
	}


	/**
	 * 增加
	 *
	 * @param condition 条件
	 * @param ignoreNulls 忽略null参数
	 * @return 当前实例
	 */
	public SqlHelper eq(Map<String, Object> condition, boolean ignoreNulls) {
		Set<Entry<String, Object>> e = condition.entrySet();
		for (Entry<String, Object> entry:e) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (ignoreNulls && value == null) {
                continue;
            }
			eq(key, value);
		}
		return this;
	}

	/**
	 * <p>eq.</p>
	 *
	 * @param condition a {@link java.util.Map} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper eq(Map<String, Object> condition) {
		return eq(condition, true);
	}

	/**
	 * <p>Getter for the field <code>databaseType</code>.</p>
	 *
	 * @return a int.
	 */
	public int getDatabaseType() {
		return databaseType;
	}

	/**
	 * <p>or.</p>
	 *
	 * @param cond a {@link SqlHelper} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper or(SqlHelper cond) {
		if (orCond == null) {
            orCond = new ArrayList<>();
        }
		this.orCond.add(cond);
		return this;
	}

	/**
	 * <p>not.</p>
	 *
	 * @param cond a {@link SqlHelper} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper not(SqlHelper cond) {
		if (notCond == null) {
            notCond = new ArrayList<>();
        }
		this.notCond.add(cond);
		return this;
	}

	/**
	 * <p>in.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param value a {@link java.util.Collection} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper in(String field, Collection<Object> value) {
		filters.add(new InFilter(field, value));
		return this;
	}

	/**
	 * <p>in.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param value a {@link SqlString} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper in(String field, SqlString value) {
		filters.add(new InFilter(field, value));
		return this;
	}

	/**
	 * <p>in.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param value an array of {@link java.lang.Object} objects.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper in(String field, Object[] value) {
		filters.add(new InFilter(field, Arrays.asList(value)));
		return this;
	}

	/**
	 * <p>isNull.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper isNull(String field) {
		filters.add(new NullFilter(field));
		return this;
	}

	/**
	 * <p>at.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param value a {@link ISqlConditionType} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper at(String field, ISqlConditionType value) {
		filters.add(new CondFilter(field, value, databaseType));
		return this;
	}

	/**
	 * <p>like.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param value a {@link java.lang.String} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper like(String field, String value) {
		filters.add(new LikeFilter(field, value));
		return this;
	}

	/**
	 * <p>startWith.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param value a {@link java.lang.String} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper startWith(String field, String value) {
		filters.add(new StartWithFilter(field, value));
		return this;
	}

	/**
	 * <p>eq.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper eq(String field, Object value) {
		filters.add(new EqualsFilter(field, value));
		return this;
	}

	/**
	 * <p>ne.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper ne(String field, Object value) {
		filters.add(new NotEqualsFilter(field, value));
		return this;
	}

	/**
	 * <p>eq.</p>
	 *
	 * @param fields an array of {@link java.lang.String} objects.
	 * @param value an array of {@link java.lang.Object} objects.
	 * @return a {@link SqlHelper} object.
	 * @throws java.sql.SQLException if any.
	 */
	public SqlHelper eq(String[] fields, Object[] value) throws SQLException {
		if (fields.length != value.length) {
            throw new SQLException("fields.length must equals value.length:"
                    + StringUtils.join(fields,",") + "->" + StringUtils.join(value,","));
        }
		int i = 0;
		for (String field : fields) {
			filters.add(new EqualsFilter(field, value[i++]));
		}
		return this;
	}

	/**
	 * <p>greaterThan.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper greaterThan(String field, Object value) {
		filters.add(new GtFilter(field, value));
		return this;
	}
	public SqlHelper greaterOrEqualThan(String field, Object value) {
		filters.add(new GeFilter(field, value));
		return this;
	}

	/**
	 * <p>smallerThan.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link SqlHelper} object.
	 */
	public SqlHelper smallerThan(String field, Object value) {
		filters.add(new LtFilter(field, value));
		return this;
	}
	public SqlHelper smallerOrEqualThan(String field, Object value) {
		filters.add(new LeFilter(field, value));
		return this;
	}


	/**
	 * 设置数据库类型
	 *
	 * @param databaseType
	 *            采用BaseDao里的数据类型定义
	 */
	public void setDatabaseType(int databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * <p>isUseNullAsCondition.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isUseNullAsCondition() {
		return useNullAsCondition;
	}

	/**
	 * <p>Setter for the field <code>useNullAsCondition</code>.</p>
	 *
	 * @param useNullAsCondition a boolean.
	 */
	public void setUseNullAsCondition(boolean useNullAsCondition) {
		this.useNullAsCondition = useNullAsCondition;
	}

	/**
	 * <p>clear.</p>
	 */
	public void clear() {
		if(filters!=null) {
            this.filters.clear();
        }
		if(orCond!=null) {
            this.orCond.clear();
        }
		if(notCond!=null) {
            this.notCond.clear();
        }
	}

	protected abstract static class BaseFilter {
		protected final String property;
		protected final Object value;

		public BaseFilter(String property, Object value) {
			this.property = property;
			this.value = value;
		}

		abstract public SqlPocket build(SqlPocket pkt) throws SQLException;

	}

	protected static class InFilter extends BaseFilter {
		public InFilter(String property, Collection<Object> value) {
			super(property, new HashSet<>(value)); //use Hashset to reduce duplicated values
		}

		public InFilter(String property, SqlString value) {
			super(property, value); //use Hashset to reduce duplicated values
		}

		@Override
		public SqlPocket build(SqlPocket sb) throws SQLException {
			if(value instanceof SqlString){
				sb.getCondition().append(" and ").append(property).append(" in (").append(value).append(")");

			}else if (!(value instanceof Collection)) {
                throw new SQLException("InFilter must have a Collection value:"
                        + value + "->" + value.getClass());
            } else {
				int size = ((Collection<?>) value).size();
				sb.addFilter(" and "+
						property + " in (" + StringUtils.repeat("?", ",", size)
						+ ")", value);
			}
			return sb;
		}
	}

	protected static abstract class AbstractBiFilter extends BaseFilter {
		String sign;

		public AbstractBiFilter(String property, Object value, String sign) {
			super(property, value);
			this.sign = sign;
		}

		@Override
		public SqlPocket build(SqlPocket sb) throws SQLException {
			if(value instanceof SqlString ){
				sb.getCondition().append(" and ").append(property).append(sign)
						.append(((SqlString) value).getContent());
			}else if (value != null) {
				sb.getCondition().append(" and ").append(property).append(sign)
						.append("?");
				sb.getParams().add(value);
			} else {
				sb.getCondition().append(" and ").append(property).append(sign)
						.append("null");
			}
			return sb;
		}
	}

	protected static class GtFilter extends AbstractBiFilter {
		public GtFilter(String property, Object value) {
			super(property, value, " > ");
		}
	}

	protected static class LtFilter extends AbstractBiFilter {
		public LtFilter(String property, Object value) {
			super(property, value, " < ");
		}
	}

	protected static class GeFilter extends AbstractBiFilter {
		public GeFilter(String property, Object value) {
			super(property, value, " >= ");
		}
	}

	protected static class LeFilter extends AbstractBiFilter {
		public LeFilter(String property, Object value) {
			super(property, value, " <= ");
		}
	}

	protected static class EqualsFilter extends AbstractBiFilter {
		public EqualsFilter(String property, Object value) {
			super(property, value, " = ");
		}
	}

	protected static class NotEqualsFilter extends AbstractBiFilter {
		public NotEqualsFilter(String property, Object value) {
			super(property, value, " != ");
		}
	}

	protected static class NullFilter extends AbstractBiFilter {
		public NullFilter(String property) {
			super(property, null, " is ");
		}
	}

	protected static class CondFilter extends BaseFilter {
		int databaseType;

		public CondFilter(String property, Object value, int databaseType) {
			super(property, value);
			this.databaseType = databaseType;
		}

		@Override
		public SqlPocket build(SqlPocket sb) throws SQLException {
			if (value != null && value instanceof ISqlConditionType) {
				sb.getCondition().append(
						((ISqlConditionType) value).getConditionSql(property,
								databaseType));
			} else {
				throw new SQLException(
						"CondFilter constructor only accept value of type ISqlConditionType:"
								+ value + "->" + (value == null ? "null" : value.getClass()));
			}
			return sb;
		}
	}

	protected static abstract class BaseLikeFilter extends BaseFilter {
		public BaseLikeFilter(String property, Object value){
			super(property, value);
		}

		public abstract String getConcat();

		@Override
		public SqlPocket build(SqlPocket sb) throws SQLException {
			if (value != null && value instanceof String) {
				sb.getCondition().append(" and ").append(property)
						.append(" like ").append(getConcat());
				sb.getParams().add(value);
			} else {
				throw new SQLException(
						"CondFilter constructor only accept value of type ISqlConditionType:"
								+ value + "->" + (value == null ? "null" : value.getClass()));
			}
			return sb;
		}

	}

	protected static class LikeFilter extends BaseLikeFilter {
		public LikeFilter(String property, Object value){
			super(property, value);
		}

		@Override
		public String getConcat(){ return "concat('%',?,'%')"; }
	}

	protected static class StartWithFilter extends BaseLikeFilter {
		public StartWithFilter(String property, Object value){
			super(property, value);
		}

		@Override
		public String getConcat(){ return "concat(?,'%')";
		}

	}

}
