package com.bixuebihui.util;

import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

public class CookieUtilTest extends TestCase {

	public void testSetCookie() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		 // Cookie arg0=null;
		  String name="mypasswd";
		String value="is not a null";
		Integer time = 0;
		CookieUtil.setCookie(name, value, time , response);
		Cookie res = response.getCookie(name);
		assertEquals("is+not+a+null",res.getValue());
		//expect(res.addCookie(arg0));
	}

	public void testGetCookie() {
		//fail("Not yet implemented");
	}

}
