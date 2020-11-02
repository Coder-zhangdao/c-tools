package com.bixuebihui.util;

import com.bixuebihui.util.gb.Shenfenzheng;
import junit.framework.TestCase;

import java.text.ParseException;
import java.util.Date;

public class ShenfenzhengTest extends TestCase {

	public void testValidCardid() {
		assertEquals(true, Shenfenzheng.isValidCardID("232301197311123155"));
		assertEquals(false, Shenfenzheng.isValidCardID("23230119731112315X"));
		assertEquals(false, Shenfenzheng.isValidCardID("23230119731112315A"));

	}

	public void testGetDateFromCardid() throws ParseException {
		Date dt = new Date(1974 - 1900, 6 - 1, 30);
		assertEquals(dt, Shenfenzheng.getDateFromCardID("110111740630031"));

		Date dt1 = new Date(1973 - 1900, 11 - 1, 12);
		assertEquals(dt1, Shenfenzheng.getDateFromCardID("232301197311123155"));
	}
}
