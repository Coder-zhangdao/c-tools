package com.bixuebihui.jdbc;

import junit.framework.TestCase;

import java.math.BigDecimal;
import java.text.ParseException;

public class NumberRangeTest extends TestCase {

    public void testGetConditionSql() {
        NumberRange r = new NumberRange(BigDecimal.ONE,BigDecimal.valueOf(3));
        String sql = r.getConditionSql("abc", 1);
        assertEquals(" and abc>= 1 and abc<3", sql);

        r = new NumberRange(BigDecimal.ONE,null);
        sql = r.getConditionSql("abc", 1);
        assertEquals(" and abc>= 1", sql);
    }


    public void testIsNumberRange() {
        assertTrue( NumberRange.isNumberRange("12.3~345.0") );
        assertTrue( NumberRange.isNumberRange("~345") );
        assertTrue( NumberRange.isNumberRange("123~") );
        assertFalse( NumberRange.isNumberRange("~") );
        assertFalse( NumberRange.isNumberRange("adc~") );
    }


    public void testBuild() throws ParseException {
        assertEquals(" and abc>= 12.3 and abc<345.0", NumberRange.build("12.3~345.0").getConditionSql("abc",1) );
        assertEquals(" and abc<345", NumberRange.build("~345").getConditionSql("abc",1) );
        assertEquals(" and abc>= 123", NumberRange.build("123~").getConditionSql("abc",1) );
    }
}
