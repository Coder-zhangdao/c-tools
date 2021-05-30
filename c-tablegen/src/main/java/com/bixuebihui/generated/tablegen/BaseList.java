package com.bixuebihui.generated.tablegen;
/**
 * BaseList
 * <p>
 * WARNING! Automatically generated file!
 * Do not edit!
 * Code Generator by J.A.Carter
 * Modified by Xing Wanxiang 2008
 * (c) www.goldjetty.com
 */

import com.bixuebihui.BeanFactory;
import com.bixuebihui.jdbc.BaseDao;
import com.bixuebihui.jdbc.IDbHelper;
import com.bixuebihui.jdbc.MSDbHelper;
import com.bixuebihui.jdbc.aop.DbHelperAroundAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

/**
 * @author xwx
 */
public abstract class BaseList<T, V> extends BaseDao<T, V> {

    @Autowired
    DataSource ds;

    public BaseList() {
        try {
            dbHelper = (IDbHelper) BeanFactory.createObjectById("dbHelper");
        }catch (Exception e ) {
            MSDbHelper dbHelper0 = new MSDbHelper();
            dbHelper0.setMasterDatasource(ds);
            dbHelper0.setDataSource(ds);
            if (LOG.isDebugEnabled()) {
                ProxyFactory obj = new ProxyFactory(dbHelper0);
                obj.addAdvice(new DbHelperAroundAdvice());
                dbHelper = (IDbHelper) obj.getProxy();
            } else {
                dbHelper = dbHelper0;
            }
        }
    }

}
