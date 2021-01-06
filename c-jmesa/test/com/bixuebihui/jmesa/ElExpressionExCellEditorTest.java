package com.bixuebihui.jmesa;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


import org.junit.jupiter.api.Test;

import javax.el.FunctionMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElExpressionExCellEditorTest {

	@Test
	public void testElExpressionExCellEditorExpressionMapOfStringQ() {
		//fail("Not yet implemented");
	}

	@Test
	public void testElExpressionExCellEditorStringObjectMapOfStringQ() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetVariableResolverObject() {
//		fail("Not yet implemented");
	}

	@Test
	public void testGetFunctionMapper() {
		String temp="'<span title=\\'#{fn:escapeXml(row.body)}\\'>#{fn:escapeXml(fn:abbreviate(row.body,100))}</span>";

		Map<String, ?> map = new HashMap<>();
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
