package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.jmesa.limit.SortSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class EsObject {
    public static final String EMPTY_JSON_STRING = "{}";
    static  final Logger logger = LoggerFactory.getLogger(EsObject.class);
    static final String ACTION_SEARCH = "_search";
    static final String ACTION_BULK = "_bulk";

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



    ObjectMapper objectMapper = new ObjectMapper();

    public EsObject(String indexName) {
        this.indexName = indexName;
    }

    public  String build(Query query,  int from, int size)  {
        Map m = query.toArray();

        HashMap<Object, Object> queryMap = Maps.newHashMap();
        queryMap.put(QUERY, m);
        if(size>0) {
            queryMap.put(SIZE, size);
        }
        if(from>0) {
            queryMap.put(FROM, from);
        }
        String s = EMPTY_JSON_STRING;
        try {
            s = objectMapper.writeValueAsString(queryMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.debug(s);
        return s;
    }


}
