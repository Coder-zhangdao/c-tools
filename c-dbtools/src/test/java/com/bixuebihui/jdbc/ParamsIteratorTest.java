package com.bixuebihui.jdbc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParamsIteratorTest {
    @Test
    public void iterator() throws Exception {
        ParamsIterator pi = new ParamsIterator(10, index -> new Object[]{index, index * 10});

        long[] sum = new long[2];
        pi.forEach(objects -> {
            sum[0] += (int) objects[0];
            sum[1] += (int) objects[1];
        });

        assertEquals(45, sum[0]);
        assertEquals(450, sum[1]);
    }



}
