package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public  class EsObjectTest {
    EsQueryBuilder esObject = new EsQueryBuilder();

    @Test
    public void testMatchAll() throws JsonProcessingException {

        String s = esObject.build(Query.match_all(), 0, 5);

        assertEquals("{\"size\":5,\"query\":{\"match_all\":{}}}", s);
    }


    @Test
    public void testMatchAllWithSort(){
        /**
         * GET bank/_search
         * {
         *   "query": {"match_all": {}},
         *   "sort": [
         *     {
         *       "account_number": "desc"
         *     },
         *     {
         *       "balance": "desc"
         *     }
         *   ]
         * }
         */

    }

    @Test
    public void testMatch() throws JsonProcessingException {

        String s = esObject.build(Query.match(null, null)
                .setFieldQuery("abc","test 123"), 0,5);

        assertEquals("{\"size\":5,\"query\":{\"match\":{\"abc\":{\"query\":\"test 123\"}}}}", s);
    }

}
