package com.bixuebihui.jdbc;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import javax.script.ScriptEngine;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


/**
 * <p>SqlFilter class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 * TODO use @see SqlHelper is better
 */
public class SqlFilter {

	/**
	 * Constant <code>AND=" and "</code>
	 */
	public static final String AND = " and ";
	protected List<Filter> filters = null;
	protected List<SqlFilter> orCond = null;
	protected List<SqlFilter> subGroups = new ArrayList<>();
	protected Operator subGroupsJoinOperator;
	private int databaseType = BaseDao.MYSQL;
	private boolean useNullAsCondition = false;

	/**
	 * 防止sql注入, 会改变原始数据,不建议使用
	 *
	 * @param sql sql语句
	 * @return 过滤特殊字符: 单引号, 分号,注释
	 */
	public static String transactSQLInjection(String sql) {
		return sql.replaceAll(".*([';]+|(--)+).*", "");
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void addSubGroupCondition(Operator operator, SqlFilter... filters){
		this.subGroups.addAll(Arrays.asList(filters));
		this.subGroupsJoinOperator =  operator;
	}

	/**
	 * <p>toCondition.</p>
	 *
	 * @return a {@link java.lang.StringBuilder} object.
	 * @deprecated use toSqlObject
	 */
	@Deprecated
	protected StringBuilder toCondition(){
		StringBuilder criteria = new StringBuilder();
		if (isNoFilters()) {
            return criteria;
        }
		for (Filter filter : filters) {
			buildCriteria(criteria, filter.getProperty(), filter.getValue());
		}

		if(criteria.length()>0 && criteria.indexOf(AND)==0){
			criteria.delete(0, 4);
		}

		if(subGroups!=null && subGroups.size()>0){
			if(criteria.length()>1) {
				criteria.insert(0, "(").append(") ").append(AND).append(" ");
			}
			criteria.append(" (");
			for(SqlFilter it:subGroups){
				criteria.append(" ").append(subGroupsJoinOperator)
						.append(" (").append(it.toCondition()).append(")");
			}
			criteria.append(")");
		}

		if(orCond!=null){
			criteria.insert(0, "(").append(")");
			for(SqlFilter it:orCond){
				criteria.append(" or (").append(it.toCondition()).append(")");
			}
		}
		return criteria;
	}

	protected boolean isNoFilters(){
		return (filters==null || filters.isEmpty())
				&& subGroups.isEmpty();
	}
	/**
	 * Generate SqlObject: sql string with parameters
	 * @return SqlObject
	 */
	public SqlObject toSqlObject(){
		SqlObject res = new SqlObject();
		List<Object> params = new ArrayList<>();
		StringBuilder criteria = new StringBuilder();
		if (isNoFilters()) {
			res.setSqlString(criteria.toString());
			return res;
		}
		if(filters!= null) {
			for (Filter filter : filters) {
				buildSqlObject(criteria, params, filter);
			}
		}

		if(criteria.length()>0 && criteria.indexOf(AND)==0){
			criteria.delete(0, 4);
		}

		if(subGroups!=null && subGroups.size()>0){
			if(criteria.length()>1) {
				criteria.insert(0, "(").append(") ").append(AND).append(" ");
			}
			criteria.append(" (");

			criteria.append(subGroups.stream().map((it) -> {
					SqlObject sqlObject = it.toSqlObject();
					params.addAll(Lists.newArrayList(sqlObject.getParameters()));
					return sqlObject.getSqlString().substring("where".length());
				}).collect(Collectors.joining(") "+subGroupsJoinOperator.toString()+" ("))
			);

			criteria.append(")");
		}


		if(orCond!=null){
			criteria.insert(0, " (").append(")");
			for(SqlFilter it:orCond){
				SqlObject subObj= it.toSqlObject();
				criteria.append(" or (").append(subObj.getSqlString().substring("where".length())).append(")");
				params.addAll(Lists.newArrayList(subObj.getParameters()));
			}
		}

		res.setSqlString(where(criteria));
		res.setParameters(params.toArray());
		return res;
	}

	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	@Override
	@Deprecated
	public String toString() {
		return where(toCondition());
	}

	private String where(StringBuilder res) {
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


	private void buildSqlObject(StringBuilder criteria, List<Object> params, Filter filter) {
		criteria.append(AND).append(filter.getProperty());
		switch (filter.comparison){
			case IS:
				criteria.append(" = ? ");
				break;
			case IS_NOT:
				criteria.append(" != ? ");
				break;
			case BETWEEN:
				criteria.append(" between ? and ? ");
				break;
			case NOT_BETWEEN:
				criteria.append(" not between ? and ? ");
				break;
			case IS_NULL:
				criteria.append(" is null ");
				break;
			case IS_NOT_NULL:
				criteria.append(" is not null ");
				break;
			case GT:
				criteria.append(" > ? ");
				break;
			case GTE:
				criteria.append(" >= ? ");
				break;
			case LT:
				criteria.append(" < ? ");
				break;
			case LTE:
				criteria.append(" <= ? ");
				break;

			case NOT_IN:
				criteria.append(" not ");
				criteria.append(" in (").append(StringUtils.repeat("?", ",", filter.value.length)).append(") ");
				break;
			case IN:
				criteria.append(" in (").append(StringUtils.repeat("?", ",", filter.value.length)).append(") ");
				break;

			case NOT_EXISTS:
				criteria.append(" not ");
				criteria.append(" exist (").append(filter.value[0]).append(") ");

				break;
			case EXISTS:
				//todo filter filter.value
				criteria.append(" exist (").append(filter.value[0]).append(") ");
				break;
			case CONTAIN:
				criteria.append(" like concat('%', ?, '%') ");
				break;
			case START_WITH:
				criteria.append(" like concat(?, '%') ");
				break;

			default:

		}
		if(filter.value!=null && filter.value.length>0
				&& filter.comparison!=Comparison.EXISTS
				&& filter.comparison!=Comparison.NOT_EXISTS) {
			params.addAll(Lists.newArrayList(filter.value));
		}
	}

	/**
	 * <p>addFilter.</p>
	 *
	 * @param property a {@link java.lang.String} object.
	 * @param comparison   operation to compare the property vs value
	 * @param value a {@link java.lang.Object} object.
	 * @return a {@link SqlFilter} object.
	 */
	public SqlFilter addFilter(String property, Comparison comparison, Object[] value) {
		if(!useNullAsCondition && value==null) {
            return this;
        }
		if(filters==null) {
            filters = new ArrayList<>();
        }

		filters.add(new Filter(property, comparison, value));

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

			addFilter(key,value instanceof String ? Comparison.CONTAIN : Comparison.IS, new Object[]{value});
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
	 * 设置数据库类型
	 *
	 * @param databaseType 采用BaseDao里的数据类型定义
	 */
	public void setDatabaseType(int databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * <p>or.</p>
	 *
	 * @param cond a {@link SqlFilter} object.
	 * @return a {@link SqlFilter} object.
	 * @deprecated since 1.4.1
	 */
	@Deprecated
	public SqlFilter or(SqlFilter cond){
		if(orCond==null) {
            orCond = new ArrayList<>();
        }
		this.orCond.add(cond);
		return this;
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

	public SqlFilter is(String field, Object value){
		return this.addFilter(field, Comparison.IS, new Object[]{value});
	}

	public SqlFilter isNot(String field, Object value){
		return this.addFilter(field, Comparison.IS_NOT, new Object[] {value});
	}

	public SqlFilter isNull(String field){
		return this.addFilter(field, Comparison.IS_NULL, null);
	}

	public SqlFilter isNotNull(String field){
		return this.addFilter(field, Comparison.IS_NOT_NULL, null);
	}

	public SqlFilter in(String field, Object[] value){
		return this.addFilter(field, Comparison.IN, value);
	}

	public SqlFilter notIn(String field, Object[] value){
		return this.addFilter(field, Comparison.NOT_IN, value);
	}

	public SqlFilter between(String field, Object valueLeft, Object valueRight){
		return this.addFilter(field, Comparison.BETWEEN, new Object[]{valueLeft,valueRight});
	}

	public SqlFilter notBetween(String field, Object valueLeft, Object valueRight){
		return this.addFilter(field, Comparison.NOT_BETWEEN,  new Object[]{valueLeft,valueRight});
	}

	public SqlFilter gt(String field, Object value){
		return this.addFilter(field, Comparison.GT, new Object[]{ value});
	}

	public SqlFilter lt(String field, Object value){
		return this.addFilter(field, Comparison.LT,  new Object[]{value});
	}

	public SqlFilter gte(String field, Object value){
		return this.addFilter(field, Comparison.GTE,  new Object[]{value});
	}

	public SqlFilter lte(String field, Object value){
		return this.addFilter(field, Comparison.LTE,  new Object[]{value});
	}

	public SqlFilter contain(String field, Object value){
		return this.addFilter(field, Comparison.CONTAIN,  new Object[]{value});
	}

	public SqlFilter startWith(String field, Object value){
		return this.addFilter(field, Comparison.START_WITH,  new Object[]{value});
	}

	public enum Operator{
		AND, OR
	}
	/**
	 * Used for filters
	 * since 4.3
	 */
	public enum Comparison {
		/**
		 * =
		 */
		IS,

		/**
		 * !=
		 */
		IS_NOT,

		/**
		 * &gt;
		 */
		GT,

		/**
		 * &gt;=
		 */
		GTE,

		/**
		 * &lt;
		 */
		LT,

		/**
		 * &lt;=
		 */
		LTE,

		/**
		 * in (...)
		 */
		IN,

		/**
		 * not in (...)
		 */
		NOT_IN,

		/**
		 * is null
		 */
		IS_NULL,
		/**
		 * is not null
		 */
		IS_NOT_NULL,
		/**
		 * need sub query, not implemented yet
		 */
		EXISTS,
		/**
		 * need sub query, not implemented yet
		 */
		NOT_EXISTS,

		/**
		 * between (from_value, to_value) inclusive
		 */
		BETWEEN,
		/**
		 * not between (from_value, to_value) inclusive
		 */
		NOT_BETWEEN,


		/**
		 * like concat('%', value, '%')
		 */
		CONTAIN,

		/**
		 * like concat( value, '%')
		 */
		START_WITH,

	}

	public static class Filter {
		private final String property;
		private final Object[] value;
		private final Comparison comparison;

		public Filter(String property,  Comparison comparison, Object[] value) {
			this.property = property;
			this.comparison = comparison;
			this.value = value;
		}

		public String getProperty() {
			return property;
		}

		public Comparison getComparison() {
			return comparison;
		}

		public Object[] getValue() {
			return value;
		}
	}

	//the tow below is dangerous to use because sql injection
//	public SqlFilter exists(String field, String value){
//		return this.addFilter(field, Comparison.EXISTS, value);
//	}
//	public SqlFilter notExists(String field, String value){
//		return this.addFilter(field, Comparison.NOT_EXISTS, value);
//	}
}
