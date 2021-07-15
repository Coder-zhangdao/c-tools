package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.jmesa.limit.Limit;
import org.jmesa.limit.SortSet;

import java.util.HashMap;
import java.util.Map;

public class EsObject {
    static final String ACTION = "_search";
    static final String queryAllFields = "q=?";
    static final String SIZE = "size";
    static final String FROM = "from";
    static final String _SOURCE = "_source";
    static final String QUERY ="query";

    SortSet sortSet;
    /**
     * index name to search
     * 需要检索的索引
     */
    String indexName;
    String method = "GET";



    ObjectMapper objectMapper = new ObjectMapper();

    public EsObject(String indexName) {
        this.indexName = indexName;
    }

    public  String build(Query query,  int from, int size) throws JsonProcessingException {
        Map m = query.toArray();

        HashMap<Object, Object> queryMap = Maps.newHashMap();
        queryMap.put(QUERY, m);
        if(size>0) {
            queryMap.put(SIZE, size);
        }
        if(from>0) {
            queryMap.put(FROM, from);
        }
        String s = objectMapper.writeValueAsString(queryMap);
        System.out.println(s);
        return s;
    }


}
