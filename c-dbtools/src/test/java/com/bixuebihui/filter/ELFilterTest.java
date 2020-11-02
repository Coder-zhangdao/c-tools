package com.bixuebihui.filter;

import junit.framework.TestCase;

public class ELFilterTest extends TestCase {

	public void testEvaluate() {
		ELFilter ef = new ELFilter(null);
		System.out.println("user.home is "+ef.evaluate("${user.home}"));
		System.out.println("java.home is "+ef.evaluate("${JAVA_HOME}"));

	}

}
