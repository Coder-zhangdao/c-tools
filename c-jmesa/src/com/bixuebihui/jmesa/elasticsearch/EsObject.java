package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class EsObject {
    static final String action = "_search";
    static final String queryAllFields = "q=?";
    static final String size = "size";
    static final String from = "from";
    static final String _source = "_source";
    /**
     * index name to search
     * 需要检索的索引
     */
    String indexName;
    String method = "GET";
    ObjectMapper objectMapper = new ObjectMapper();
    public  String build(Query query) throws JsonProcessingException {
        Map m = query.toArray();

        HashMap<Object, Object> queryMap = Maps.newHashMap();
               queryMap.put("query", m);
        String s = objectMapper.writeValueAsString(queryMap);
        System.out.println(s);
        return s;
    }


}
