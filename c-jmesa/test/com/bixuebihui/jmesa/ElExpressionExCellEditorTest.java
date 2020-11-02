package com.bixuebihui.jmesa;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.el.FunctionMapper;

import junit.framework.TestCase;

public class ElExpressionExCellEditorTest extends TestCase {

	public void testElExpressionExCellEditorExpressionMapOfStringQ() {
		//fail("Not yet implemented");
	}

	public void testElExpressionExCellEditorStringObjectMapOfStringQ() {
//		fail("Not yet implemented");
	}

	public void testGetVariableResolverObject() {
//		fail("Not yet implemented");
	}

	public void testGetFunctionMapper() {
		String temp="'<span title=\\'#{fn:escapeXml(row.body)}\\'>#{fn:escapeXml(fn:abbreviate(row.body,100))}</span>";

		Map<String, ?> map =new HashMap<String, Object>();
		ElExpressionExCellEditor e = new ElExpressionExCellEditor("test", temp, map );
		FunctionMapper f = e.getFunctionMapper();

		String name1 = "escapeXml";
		Method m = f.resolveFunction("fn", name1);
		assertEquals(name1, m.getName());

		String name2 = "abbreviate";
		Method m2 = f.resolveFunction("fn", name2);
		assertEquals(name2, m2.getName());

	}

}
