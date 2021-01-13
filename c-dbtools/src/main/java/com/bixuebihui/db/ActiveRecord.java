package com.bixuebihui.db;

import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-4-18
 * Time: 下午5:57
 * To change this template use File | Settings | File Templates.
 *
 * @author xingwx
 * @version $Id: $Id
 */
public interface ActiveRecord<T> extends Record<T> {

    /**
     * Constant <code>ORDER_ASC=0</code>
     */
    int ORDER_ASC = 0;
    /** Constant <code>ORDER_DESC=1</code> */
    int ORDER_DESC=1;

    // ActiveRecord<T> ignoreBlank(boolean conditionWhenNotBlank);

    //条件

    /**
     * <p>in.</p>
     *
     * @param field a {@link java.lang.String} object.
     * @param values an array of {@link java.lang.Object} objects.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> in(String field, Object[] values);

    /**
     * <p>in.</p>
     *
     * @param field a {@link java.lang.String} object.
     * @param values a {@link java.lang.Object} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> in(String field, Object values);

    /**
     * <p>like.</p>
     *
     * @param field a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> like(String field, String value);

    /**
     * <p>startWith.</p>
     *
     * @param field a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> startWith(String field, String value);

    /**
     * <p>eq.</p>
     *
     * @param field a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> eq(String field, Object value);

    /**
     * <p>ne.</p>
     *
     * @param field a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> ne(String field, Object value);

    /**
     * <p>eq.</p>
     *
     * @param fields an array of {@link java.lang.String} objects.
     * @param value an array of {@link java.lang.Object} objects.
     * @return a {@link ActiveRecord} object.
     * @throws java.sql.SQLException if any.
     */
    ActiveRecord<T> eq(String[] fields, Object[] value) throws SQLException;

    /**
     * <p>greaterThan.</p>
     *
     * @param field a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> greaterThan(String field, Object value);

    /**
     * <p>smallerThan.</p>
     *
     * @param field a {@link java.lang.String} object.
     * @param value a {@link java.lang.Object} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> smallerThan(String field, Object value);

    /**
     * <p>asc.</p>
     *
     * @param field a {@link java.lang.String} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> asc(String field);

    /**
     * <p>desc.</p>
     *
     * @param field a {@link java.lang.String} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> desc(String field);

    /**
     * <p>limit.</p>
     *
     * @param begin a int.
     * @param num a int.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> limit(int begin, int num);


    /**
     * <p>last.</p>
     *
     * @return a {@link Record} object.
     */
    Record<T> last();

    ActiveRecord<T> greaterOrEqualThan(String field, Object value);

    ActiveRecord<T> smallerOrEqualThan(String field, Object value);

    /**
     * <p>getCondStack.</p>
     *
     * @return a {@link SqlHelper} object.
     */
    SqlHelper getCondStack();

    /**
     * <p>or.</p>
     *
     * @param andStack a {@link SqlHelper} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> or(SqlHelper andStack);

    /**
     * <p>alias.</p>
     *
     * @param shortName a {@link java.lang.String} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> alias(String shortName);

    /**
     * <p>fields.</p>
     *
     * @param resultFields a {@link java.lang.String} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> fields(String resultFields);

    /**
     * <p>join.</p>
     *
     * @param joinClause a {@link java.lang.String} object.
     * @return a {@link ActiveRecord} object.
     */
    ActiveRecord<T> join(String joinClause);
}
