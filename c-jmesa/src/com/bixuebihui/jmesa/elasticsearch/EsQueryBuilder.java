package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author xwx
 */
public class EsQueryBuilder {
    public static final String EMPTY_JSON_STRING = "{}";
    static  final Logger logger = LoggerFactory.getLogger(EsQueryBuilder.class);
    static final String ACTION_SEARCH = "_search";
    static final String ACTION_BULK = "_bulk";

    static final String QUERY_ALL_FIELDS = "q=?";
    static final String SIZE = "size";
    static final String FROM = "from";
    static final String SOURCE = "_source";
    static final String QUERY ="query";



    static ObjectMapper objectMapper = new ObjectMapper();


    public static String build(Query query, int from, int size, LinkedHashMap<String, String> sort)  {
        Map queryConditions = query.toArray();

        HashMap<Object, Object> body = Maps.newHashMap();
        body.put(QUERY, queryConditions);
        if(size>0) {
            body.put(SIZE, size);
        }
        if(from>0) {
            body.put(FROM, from);
        }

        if(sort!=null && !sort.isEmpty()){
            body.put("sort", sort);
        }

        String s = EMPTY_JSON_STRING;
        try {
            s = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.debug(s);
        return s;
    }


}
