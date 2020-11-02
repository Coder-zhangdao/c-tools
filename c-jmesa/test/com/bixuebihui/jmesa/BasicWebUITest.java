package com.bixuebihui.jmesa;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.bixuebihui.BeanFactory;

import junit.framework.TestCase;

public class BasicWebUITest extends TestCase {

	String id="view_businesses_per_category";

	BasicWebUI ui =  ( BasicWebUI) BeanFactory.createObjectById(id);
	//IBaseListService service;

	public void setUp(){

		//ui.setService(service);
		ui.setId(id);
		String[] params={"10142"};
		((BasicListService)ui.getListService()).setSqlParams(params);
	}


	public void testRender() throws Exception {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		String res = ui.render(request, response);
		System.out.println(res);
		assertTrue(res.indexOf(id)>0);

	}



	public void testGetColsTemplate() throws Exception {
		HttpServletRequest request = new MockHttpServletRequest();
		HttpServletResponse response = new MockHttpServletResponse();
		String res = ui.render(request, response);
		System.out.println(res);
		assertTrue(res.indexOf(id)>0);

	}

}
