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
	enum GroupFun{
		AVG,//(column)	返回某列的平均值
		//BINARY_CHECKSUM
		//CHECKSUM
		//CHECKSUM_AGG
		COUNT,//(column)	返回某列的行数（不包括NULL值）
		//COUNT(*)	返回被选行数
		//COUNT(DISTINCT column)	返回相异结果的数目
		FIRST,//(column)	返回在指定的域中第一个记录的值（SQLServer2000 不支持）
		LAST,//(column)	返回在指定的域中最后一个记录的值（SQLServer2000 不支持）
		MAX,//(column)	返回某列的最高值
		MIN,//(column)	返回某列的最低值
		STDDEV_SAMP,//STDDEV_SAMP for MySql, MS SQL Server STDEV,//(column)
		STD,//(column)	STD for MySql , MS SQL Server STDEVP
		SUM,//(column)	返回某列的总和
		VAR,//(column)
		VARP,//(column)
	}

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

	//获得单个字段

	/**
	 * <p>getVector.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 * @throws java.sql.SQLException if any.
	 */
	List<Object> getVector(String field) throws SQLException;

	//获得数量

	/**
	 * <p>count.</p>
	 *
	 * @return a int.
	 * @throws java.sql.SQLException if any.
	 */
	int count() throws SQLException;

	//获得数量

	/**
	 * <p>countValue.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param fun a {@link Record.GroupFun} object.
	 * @return a {@link CountValue} object.
	 * @throws java.sql.SQLException if any.
	 */
	CountValue countValue(String field, GroupFun fun) throws SQLException;

	/**
	 * <p>countObject.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @param fun a {@link Record.GroupFun} object.
	 * @param objectType a {@link java.lang.Class} object.
	 * @param <K> a K object.
	 * @return a {@link CountObject} object.
	 * @throws java.sql.SQLException if any.
	 */
	<K> CountObject<K> countObject(String field, GroupFun fun, Class<K> objectType) throws SQLException;

	//获得数量

	/**
	 * <p>countSum.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link CountValue} object.
	 * @throws java.sql.SQLException if any.
	 */
	CountValue countSum(String field) throws SQLException;


	/**
	 * 是否存在至少一条符合条件的记录
	 *
	 * @return true 存在
	 * @throws java.sql.SQLException 数据库异常
	 */
	boolean exists() throws SQLException;

	//获得生产的sql

	/**
	 * <p>getSql.</p>
	 *
	 * @return a {@link SqlPocket} object.
	 * @throws java.sql.SQLException if any.
	 */
	SqlPocket getSql() throws SQLException;

	/**
	 * 获取字段field的长整型列表
	 *
	 * @param field 字段名
	 * @return List&lt;Long&gt; 长整形列表
	 * @throws java.sql.SQLException 数据库执行出错
	 */
	List<Long> getLongVector(String field) throws SQLException;

	//获得单个字段

	/**
	 * <p>getStringVector.</p>
	 *
	 * @param field a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 * @throws java.sql.SQLException if any.
	 */
	List<String> getStringVector(String field) throws SQLException;

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
