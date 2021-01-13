package com.bixuebihui.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * <p>SqlFilter class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 * TODO use SqlHelper is better
 */
public class SqlFilter {

	/**
	 * Constant <code>AND=" and "</code>
	 */
	public static final String AND = " and ";
	List<Filter> filters = null;
	private int databaseType = BaseDao.MYSQL;

	List<SqlFilter> orCond = null;

	private boolean useNullAsCondition = false;


	/**
	 * <p>toCondition.</p>
	 *
	 * @return a {@link java.lang.StringBuilder} object.
	 */
	protected StringBuilder toCondition(){
		StringBuilder criteria = new StringBuilder();
		if (filters==null || filters.isEmpty()) {
            return criteria;
        }
		for (Filter filter : filters) {
			buildCriteria(criteria, filter.getProperty(), filter.getValue());
		}

		if(criteria.length()>0 && criteria.indexOf(AND)==0){
			criteria.delete(0, 4);
		}

		if(orCond!=null){
			criteria.insert(0, "(").append(")");
			for(SqlFilter it:orCond){
				criteria.append(" or (").append(it.toCondition()).append(")");
			}
		}
		return criteria;
	}

	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@Override
	public String toString() {
		StringBuilder res = toCondition();

		if(res.length()>0){
			if(res.indexOf(AND)==AND.length()) {
				res.delete(0, AND.length()-1);
			}
			res.insert(0, "where");
		}
		return res.toString();
	}

	private void buildCriteria(StringBuilder criteria, String property,
							   Object value) {
		if (value instanceof String) {
			criteria.append(AND).append(property).append(" like ").append(
					"'" + transactSQLInjection(value.toString()) + "%'");
		} else if (value instanceof ISqlConditionType){
			criteria.append(((ISqlConditionType)value).getConditionSql(property, getDatabaseType()));
		} else if(value!= null){
			criteria.append(AND).append(property).append(" = ").append(
					transactSQLInjection(value.toString()));
		} else {
			criteria.append(AND).append(property).append(" is null ");
		}

	}

	/**
	 * 防止sql注入, 会改变原始数据,不建议使用
	 *
	 * @param sql sql语句
	 * @return 过滤特殊字符: 单引号, 分号,注释
	 */
	public static String transactSQLInjection(String sql) {
		return sql.replaceAll(".*([';]+|(--)+).*", "");
	}

	/**
	 * <p>addFilter.</p>
	 *
	 * @param property a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link SqlFilter} object.
	 */
	public SqlFilter addFilter(String property, Object value) {
		if(!useNullAsCondition && value==null) {
            return this;
        }
		if(filters==null) {
            filters = new ArrayList<>();
        }
		filters.add(new Filter(property, value));
		return this;
	}

	/**
	 * 增加
	 *
	 * @param condition 查询条件
	 * @param ignoreNulls 忽略空值
	 * @return 当前对象
	 */
	public SqlFilter  addFilters(Map<String, Object> condition, boolean ignoreNulls){
		for (Entry<String, Object> entry : condition.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (ignoreNulls && value == null) {
                continue;
            }

			addFilter(key, value);
		}
		return this;
	}

	/**
	 * <p>addFilters.</p>
	 *
	 * @param condition a {@link java.util.Map} object.
	 * @return a {@link SqlFilter} object.
	 */
	public SqlFilter  addFilters(Map<String, Object> condition){
		return addFilters(condition, true);
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
	 * @param cond a {@link SqlFilter} object.
	 * @return a {@link SqlFilter} object.
	 */
	public SqlFilter or(SqlFilter cond){
		if(orCond==null) {
            orCond = new ArrayList<>();
        }
		this.orCond.add(cond);
		return this;
	}

	/**
	 * 设置数据库类型
	 *
	 * @param databaseType 采用BaseDao里的数据类型定义
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

	private static class Filter {
		private final String property;
		private final Object value;

		public Filter(String property, Object value) {
			this.property = property;
			this.value = value;
		}

		public String getProperty() {
			return property;
		}

		public Object getValue() {
			return value;
		}
	}

}
