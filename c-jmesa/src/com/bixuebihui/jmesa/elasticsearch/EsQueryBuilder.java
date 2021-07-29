package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class EsQueryBuilder {
    public static final String EMPTY_JSON_STRING = "{}";
    static  final Logger logger = LoggerFactory.getLogger(EsQueryBuilder.class);
    static final String ACTION_SEARCH = "_search";
    static final String ACTION_BULK = "_bulk";
    static final String ACTION_CAT = "_cat";
    static final String ACTION_INDICES = "indices";

    static final String queryAllFields = "q=?";
    static final String SIZE = "size";
    static final String FROM = "from";
    static final String _SOURCE = "_source";
    static final String QUERY ="query";



    static ObjectMapper objectMapper = new ObjectMapper();


    public static   String build(Query query,  int from, int size)  {
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
