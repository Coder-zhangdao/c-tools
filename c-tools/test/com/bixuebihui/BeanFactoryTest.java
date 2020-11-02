package com.bixuebihui;

import com.bixuebihui.dbcon.DatabaseConfig;
import junit.framework.TestCase;

public class BeanFactoryTest extends TestCase {

	public void testBeanFactory() {

		String _sObjectId="dbConfig";
		DatabaseConfig obj = (DatabaseConfig) BeanFactory.createObjectById(_sObjectId);
		System.out.println(obj+obj.getDburl());
	}

	public void testCreateObjectById() {

		Object o = BeanFactory.createObjectById("dbConfig");
		assertNotNull(o);
		System.out.println(o);
	}


}
