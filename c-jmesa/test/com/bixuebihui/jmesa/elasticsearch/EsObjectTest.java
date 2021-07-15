package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public  class EsObjectTest {
    EsObject esObject = new EsObject();

    @Test
    public void testMatchAll() throws JsonProcessingException {

        String s = esObject.build(Query.match_all());

        assertEquals("{\"query\":{\"match_all\":{}}}", s);
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

}
