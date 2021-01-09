package com.bixuebihui.db;

import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 13-4-18
 * Time: 下午6:04
 * To change this template use File | Settings | File Templates.
 * 简单的数据库查询
 * @author xingwx
 * @version $Id: $Id
 */
public interface SimpleStatement<T,V> {


    //插入单个

    /**
     * <p>insert.</p>
     *
     * @param entity a T object.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    boolean insert(T entity)throws SQLException;

    //插入并插入依赖

    /**
     * <p>insertAndReferences.</p>
     *
     * @param entity a T object.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    boolean insertAndReferences(T entity)throws SQLException;

    //插入多个

    /**
     * <p>insert.</p>
     *
     * @param entities a {@link java.util.List} object.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    boolean insert(List<T> entities)throws SQLException;

    //插入或更新

    /**
     * <p>insertOrUpdate.</p>
     *
     * @param entity a T object.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    boolean insertOrUpdate(T entity) throws SQLException;

    //插入或更新多个

    /**
     * <p>insertOrUpdate.</p>
     *
     * @param entity a {@link java.util.List} object.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    boolean insertOrUpdate(List<T> entity)throws SQLException;

    //通过主键获得

    /**
     * <p>findByKey.</p>
     *
     * @param id a V object.
     * @return a T object.
     * @throws java.sql.SQLException if any.
     */
    T findByKey(V id)throws SQLException;

    //通过多个主键获得

    /**
     * <p>findAllByKeys.</p>
     *
     * @param id an array of V[] objects.
     * @return a {@link java.util.List} object.
     * @throws java.sql.SQLException if any.
     */
    List<T> findAllByKeys(V[] id)throws SQLException;

    //更新

    /**
     * <p>update.</p>
     *
     * @param entity a T object.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    boolean update(T entity) throws SQLException;


    //更新并更新依赖

    /**
     * <p>updateAndReferences.</p>
     *
     * @param entity a T object.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    boolean updateAndReferences(T entity)throws SQLException;

    //批量更新

    /**
     * <p>update.</p>
     *
     * @param entities a {@link java.util.List} object.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    boolean update(List<T> entities)throws SQLException;

    //删除单个

    /**
     * <p>delete.</p>
     *
     * @param entity a T object.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    boolean delete(T entity)throws SQLException;

    //删除单个

    /**
     * <p>deleteByKey.</p>
     *
     * @param id a V object.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    boolean deleteByKey(V id)throws SQLException;

    //批量删除

    /**
     * <p>deleteByKeys.</p>
     *
     * @param id an array of V[] objects.
     * @return a boolean.
     * @throws java.sql.SQLException if any.
     */
    boolean deleteByKeys(V[] id)throws SQLException;

    //获得所有

    /**
     * <p>findAll.</p>
     *
     * @return a {@link java.util.List} object.
     * @throws java.lang.IllegalAccessException if any.
     * @throws java.sql.SQLException            if any.
     * @throws java.lang.InstantiationException if any.
     */
    List<T> findAll() throws IllegalAccessException, SQLException, InstantiationException;

    //获得总数

    /**
     * <p>countAll.</p>
     *
     * @return a int.
     * @throws java.sql.SQLException if any.
     */
    int countAll() throws SQLException;
}
