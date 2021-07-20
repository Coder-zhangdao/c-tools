package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jdbc.SqlFilter;
import com.bixuebihui.jmesa.elasticsearch.query.Bool;
import com.bixuebihui.jmesa.elasticsearch.query.Query;
import com.bixuebihui.jmesa.elasticsearch.query.Range;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class ElasticSearchFilter extends SqlFilter {


/**
Test cases:

    GET /content/_search
    {
        "query":{
        "match_all":{
        }
    }
    }

    GET /content* /_search
    {
        "query":{
        "term" : {
            "biz_id.keyword" : 105982
        }
    }
    }

    GET /content* /_search
    {
        "query": {
        "bool" : {
            "must" : { "term" : { "biz_id" : 105982 } },
            "should" : [
            { "term" : { "author_name" : "alice" }},
            { "term" : { "is_delete" :  0 } },
            { "term" : { "is_publish" : 1 } }
        ],
            "minimum_should_match" : 1
        }

    }
    }


    GET logstash-* /_search
    {
        "size": 20,
        "from":0,
        "query": {
            "match": {
                "cs_uri_stem": "get"
            }，
        }
       "sort": [
          {"@timestamp": {"order": "asc", "format": "strict_date_optional_time_nanos"}}
       ],
    }

 match vs term: term is an exact query, match is a fuzzy query
    */

    private List objToList(Object value){
        if(value instanceof  List ){
            return (List)value;
        }
        if(value instanceof Object[]){
            return Arrays.asList((Object[])value);
        }
        return Arrays.asList(value);
    }

    public String toEsObject(String indexName, int from, int size) {
        if (filters==null || filters.isEmpty()) {
            return EsQueryBuilder.build(Query.match_all(), from, size);
        }

        Bool criteria = new Bool();

        for (Filter filter : filters) {
            buildEsObject(criteria, filter);
        }
        return EsQueryBuilder.build(criteria, from, size);

        //TODO or/and group
    }

        private void buildEsObject(Bool criteria, Filter filter) {
        Query queryFilter;
        switch (filter.getComparison()){
            case IS:
                queryFilter = Query.term(filter.getProperty(), filter.getValue());
                criteria.addMust(queryFilter);
                break;
            case IS_NOT:
                queryFilter = Query.term(filter.getProperty(), filter.getValue());
                criteria.addMustNot(queryFilter);
                break;
            case BETWEEN:
                List list = objToList(filter.getValue());
                queryFilter = Query.range(null, null).between(filter.getProperty(),list.get(0), list.get(1) );
                criteria.addMust(queryFilter);
                break;
            case NOT_BETWEEN: {
                List list1 = objToList(filter.getValue());
                queryFilter = Query.range(null, null).between(filter.getProperty(), list1.get(0), list1.get(1));
                criteria.addMustNot(queryFilter);
                }
                break;
            case IS_NULL:
                queryFilter = Query.term(filter.getProperty(), null);
                criteria.addMust(queryFilter);
                break;
            case IS_NOT_NULL:
                queryFilter = Query.term(filter.getProperty(), null);
                criteria.addMustNot(queryFilter);
                break;
            case GT:
                queryFilter = Query.range(filter.getProperty(), ImmutableMap.builder().put(Range.GT, objToList(filter.getValue()).get(0)).build());
                criteria.addMust(queryFilter);
                break;
            case GTE:
                queryFilter = Query.range(filter.getProperty(), ImmutableMap.builder().put(Range.GTE, objToList(filter.getValue()).get(0)).build());
                criteria.addMust(queryFilter);
                break;
            case LT:
                queryFilter = Query.range(filter.getProperty(), ImmutableMap.builder().put(Range.LT, objToList(filter.getValue()).get(0)).build());
                criteria.addMust(queryFilter);
                break;
            case LTE:
                queryFilter = Query.range(filter.getProperty(), ImmutableMap.builder().put(Range.LTE, objToList(filter.getValue()).get(0)).build());
                criteria.addMust(queryFilter);
                break;

            case NOT_IN:
                queryFilter = Query.terms(filter.getProperty(), Lists.newArrayList(filter.getValue()));
                criteria.addMustNot(queryFilter);
            case IN:
                queryFilter = Query.terms(filter.getProperty(), Lists.newArrayList(filter.getValue()));
                criteria.addMust(queryFilter);
                break;

            case NOT_EXISTS:
                queryFilter = Query.exists(filter.getProperty());

                criteria.addMustNot(queryFilter);
            case EXISTS:
                queryFilter = Query.exists(filter.getProperty());

                criteria.addMust(queryFilter);
                break;
            case CONTAIN:
                queryFilter = Query.wildcard(filter.getProperty(), filter.getValue()[0].toString()+"*",1);

                criteria.addMust(queryFilter);
                break;
        }

    }



}
