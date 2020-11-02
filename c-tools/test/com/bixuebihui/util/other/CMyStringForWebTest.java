package com.bixuebihui.util.other;

import junit.framework.TestCase;

public class CMyStringForWebTest extends TestCase {

	public void testFilterToText() {

		String _sContent="adlkj&amp;'";
		assertEquals("adlkj＆‘",CMyStringForWeb.filterToText(_sContent));

		_sContent="adlkj&amp;'\\";
		assertEquals("adlkj＆‘＼",CMyStringForWeb.filterToText(_sContent));

		_sContent="adlkj\"";
		assertEquals("adlkj“",CMyStringForWeb.filterToText(_sContent));
		_sContent="<adlkj\"";
		assertEquals("〈adlkj“",CMyStringForWeb.filterToText(_sContent));
		_sContent=">adlkj\"";
		assertEquals("〉adlkj“",CMyStringForWeb.filterToText(_sContent));
		_sContent=">adlkj\"\n";
		assertEquals("〉adlkj“",CMyStringForWeb.filterToText(_sContent));

	}

}
