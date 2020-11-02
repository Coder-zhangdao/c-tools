package com.bixuebihui.datasource;

import com.bixuebihui.ConnectionManagerTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for com.bixuebihui.datasource");
		//$JUnit-BEGIN$
		suite.addTestSuite(DbcpDataSourceTest.class);
		suite.addTestSuite(BitmechanicDataSourceTest.class);
		suite.addTestSuite(ConnectionManagerTest.class);
		//$JUnit-END$
		return suite;
	}

}
