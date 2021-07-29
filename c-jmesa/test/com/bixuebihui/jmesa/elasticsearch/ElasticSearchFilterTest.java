package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jdbc.SqlFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElasticSearchFilterTest {

    @BeforeEach
    void setUp() {
    }


    @Test
    public void testSimple() {
        ElasticSearchFilter filter = new ElasticSearchFilter();
    }


    @Test
    void toEsObject() {
        ElasticSearchFilter filter = new ElasticSearchFilter();
        filter.addFilter("age", SqlFilter.Comparison.BETWEEN, new Integer[]{10,15});
        filter.addFilter("height", SqlFilter.Comparison.GT, new Integer[]{100});

        String res = filter.toEsObject(0, 10, null);

        assertEquals("{\"size\":10,\"query\":{\"bool\":{\"must\":[{\"range\":{\"age\":{\"from\":10,\"to\":15}}},{\"range\":{\"height\":{\"gt\":100}}}]}}}", res);

    }
}
