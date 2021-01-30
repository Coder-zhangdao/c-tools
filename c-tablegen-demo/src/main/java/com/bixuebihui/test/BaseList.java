package com.bixuebihui.test;
/*
  * BaseList
  * 
  * Notice! Automatically generated file!
  * Do not edit the pojo and dal packages,use `maven tablegen:gen`!
  * Code Generator originally by J.A.Carter
  * Modified by Xing Wanxiang 2008-2021
  * email: www@qsn.so
*/
  import com.bixuebihui.BeanFactory;
  import com.bixuebihui.jdbc.BaseDao;
  import com.bixuebihui.jdbc.IDbHelper;
  import com.bixuebihui.jdbc.MSDbHelper;
  import com.bixuebihui.jdbc.aop.DbHelperAroundAdvice;
  import org.springframework.aop.framework.ProxyFactory;
  import org.springframework.beans.factory.annotation.Autowired;
  import javax.sql.DataSource;
  import java.sql.SQLException;

public abstract class BaseList<T,V> extends BaseDao<T,V>
{
      @Autowired
      DataSource ds;
 public BaseList(){
// try {
//    dbHelper = (IDbHelper) BeanFactory.createObjectById("testDbHelper");
//    }catch (Exception e ) { 
    	MSDbHelper dbHelper0 = new MSDbHelper(); 
    	dbHelper0.setMasterDatasource(ds); 
    	dbHelper0.setDataSource(ds);
    	if (mLog.isDebugEnabled()) {
    			ProxyFactory obj = new ProxyFactory(dbHelper0);
    			obj.addAdvice(new DbHelperAroundAdvice());
    			dbHelper = (IDbHelper) obj.getProxy();
    		} else {
    			dbHelper = dbHelper0;
    		}
    	}
// }
}
