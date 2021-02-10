package com.bixuebihui.db;

import com.bixuebihui.jdbc.entity.CountObject;
import com.bixuebihui.jdbc.entity.CountValue;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>Record interface.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public interface Record<T> {
	/**
	 * <p>getVector.</p> 获得单个字段
	 *
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 * @throws java.sql.SQLException if any.
	 */
	List<Object> getVector(String field) throws SQLException;

	//查询多条

	/**
	 * <p>findAll.</p>
	 *
	 * @return a {@link java.util.List} object.
	 * @throws java.sql.SQLException if any.
	 */
	List<T> findAll() throws SQLException;

	//查询单条

	/**
	 * <p>find.</p>
	 *
	 * @return a T object.
	 * @throws java.sql.SQLException if any.
	 */
	T find() throws SQLException;

	//删除

	/**
	 * <p>delete.</p>
	 *
	 * @return a boolean.
	 * @throws java.sql.SQLException if any.
	 */
	boolean delete() throws SQLException;

	//获得单个字段

	/**
	 * <p>get.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link java.lang.Object} object.
	 * @throws java.sql.SQLException if any.
	 */
	Object get(String field) throws SQLException;

	/**
	 * <p>count.</p> 获得数量
	 *
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	int count() throws SQLException;

	/**
	 * <p>countValue.</p> 获得数量
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param fun a {@link GroupFunction} object.
	 * @return a {@link CountValue} object.
	 * @throws java.sql.SQLException if any.
	 */
	CountValue countValue(String field, GroupFunction fun) throws SQLException;

	/**
	 * <p>countObject.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param fun a {@link GroupFunction} object.
	 * @param objectType a {@link java.lang.Class} object.
	 * @param <K> a K object.
	 * @return a {@link CountObject} object.
	 * @throws java.sql.SQLException if any.
	 */
	<K> CountObject<K> countObject(String field, GroupFunction fun, Class<K> objectType) throws SQLException;

	/**
	 * <p>countSum.</p> 获得数量
	 *
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link CountValue} object.
	 * @throws java.sql.SQLException if any.
	 */
	CountValue countSum(String field) throws SQLException;

	/**
	 * <p>getSql.</p> 获得生产的sql
	 *
	 * @return a {@link SqlPocket} object.
	 * @throws java.sql.SQLException if any.
	 */
	SqlPocket getSql() throws SQLException;


	/**
	 * 是否存在至少一条符合条件的记录
	 *
	 * @return true 存在
	 * @throws java.sql.SQLException 数据库异常
	 */
	boolean exists() throws SQLException;

	/**
	 * <p>getStringVector.</p> 获得单个字段
	 *
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 * @throws java.sql.SQLException if any.
	 */
	List<String> getStringVector(String field) throws SQLException;

	/**
	 * 获取字段field的长整型列表
	 *
	 * @param field 字段名
	 * @return List&lt;Long&gt; 长整形列表
	 * @throws java.sql.SQLException 数据库执行出错
	 */
	List<Long> getLongVector(String field) throws SQLException;



	/**
	 * group by function
	 */
	enum GroupFunction {
		/**
		 *  avg(column)	返回某列的平均值
		 */
		AVG,
		//BINARY_CHECKSUM
		//CHECKSUM
		//CHECKSUM_AGG
		/**
		 *  count(column)	返回某列的行数（不包括NULL值）
		 */
		COUNT,
		//COUNT(*)	返回被选行数
		//COUNT(DISTINCT column)	返回相异结果的数目
		/**
		 * first(column)	返回在指定的域中第一个记录的值（SQLServer2000 不支持）
		 */
		FIRST,
		/**
		 * last(column)	返回在指定的域中最后一个记录的值（SQLServer2000 不支持）
		 */
		LAST,

		/**
		 *  max(column)	返回某列的最高值
		 */
		MAX,
		/**
		 *  min(column)	返回某列的最低值
		 */
		MIN,
		/**
		 *  STDDEV_SAMP for MySql, MS SQL Server STDEV(column)
		 */
		STDDEV_SAMP,
		/**
		 * std(column)	STD for MySql , MS SQL Server STDEVP
		 */
		STD,
		/**
		 * sum(column)	返回某列的总和
		 */
		SUM,
		/**
		 * var(column) MySQL 不支持, 使用 variance
		 */
		VAR,
		/**
		 *  VARIANCE(expr);  standard variance 标准方差
		 */
		VARIANCE,
		/**
		 *  varp(column) MySQL 不支持,see var_pop or var_samp
		 */
		VARP
	}

	/**
	 * field = field +1
	 *
	 * @param field  字段名
	 * @return 影响的行数
	 * @throws java.sql.SQLException  数据库异常
	 */
	int inc(String field) throws SQLException;

	/**
	 * 更新字段fields为values指定值
	 *
	 * @param fields 表字段名
	 * @param values 字段值， 如果values是SqlString 类型，将不参与形成预编译语句的占位符，而是原样输出
	 * @return 影响的行数
	 * @throws java.sql.SQLException  数据库异常
	 */
	int update(String[] fields, Object[] values)throws SQLException;

	/**
	 * <p>update.</p>
	 *
	 * @param fields a {@link java.lang.String} object.
	 * @param values a {@link java.lang.Object} object.
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	int update(String fields, Object values)throws SQLException;

}
