package com.bixuebihui.cache;

import junit.framework.TestCase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ObjectCacheTest extends TestCase {

    ObjectCache oc;
    String key = "objtest";

    protected void setUp() {
        System.out.println("Start...");

        oc = new ObjectCache();
    }

    protected void tearDown() {
        oc.destroy();
        System.out.println("Done.");

    }

    public void testObjectCache() {
    }

    public void testBatchGet() throws SQLException {

        List<String> arr = new ArrayList<String>();
        arr.add("10101");
        arr.add("10102");
        Map<?, ?> m = oc.batchGet(key, arr);
        assertEquals(m.size(), 2);
        System.out.println(m);
    }


    public void testSelectById() throws SQLException {
        oc.selectById(key, "10101");
    }

    public void testUpdateByKey() throws SQLException {
        DictionaryItem info = (DictionaryItem) oc.selectById(key, "10101");
        String oldsort = info.getSort();
        info.setSort("123");
        assertTrue(oc.updateByKey(key, info.getId(), info));
        assertEquals("123", info.getSort());
        info.setSort(oldsort);
        assertTrue(oc.updateByKey(key, info.getId(), info));
        assertEquals(oldsort, info.getSort());
    }


    public void testInsertAutoNewKey() throws SQLException {
//		DictionaryItem info = (DictionaryItem) oc.selectById(key, "10101");
//
//		assertTrue(oc.insertAutoNewKey(key, info));
//		assertTrue(!"10101".equals(info.getMs_id()));
//		assertTrue(oc.deleteByKey(key,info.getMs_id()));
    }

}
