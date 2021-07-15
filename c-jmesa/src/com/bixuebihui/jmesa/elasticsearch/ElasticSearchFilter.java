package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jdbc.ISqlConditionType;
import com.bixuebihui.jdbc.SqlFilter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ElasticSearchFilter extends SqlFilter {

    private String where(StringBuilder res) {
        if(res.length()>0){
            if(res.indexOf(AND)==AND.length()) {
                res.delete(0, AND.length()-1);
            }
            res.insert(0, "where");
        }
        return res.toString();
    }

    private void buildCriteria(StringBuilder criteria, String property,
                               Object value) {
        if (value instanceof String) {
            criteria.append(AND).append(property).append(" like ").append(
                    "'" + transactSQLInjection(value.toString()) + "%'");
        } else if (value instanceof ISqlConditionType){
            criteria.append(((ISqlConditionType)value).getConditionSql(property, getDatabaseType()));
        } else if(value!= null){
            criteria.append(AND).append(property).append(" = ").append(
                    transactSQLInjection(value.toString()));
        } else {
            criteria.append(AND).append(property).append(" is null ");
        }

    }

/**
Test cases:

    GET /maincontent_20210127/_search
    {
        "query":{
        "match_all":{
        }
    }
    }

    GET /maincontent_2021* /_search
    {
        "query":{
        "term" : {
            "biz_id.keyword" : 105982
        }
    }
    }

    GET /maincontent_2021* /_search
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


    GET logstash-iis-news-* /_search
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



    private void buildEsObject(StringBuilder criteria, List<Object> params, Filter filter) {
        criteria.append(AND).append(filter.getProperty());
        switch (filter.getComparison()){
            case IS:
                criteria.append(" = ? ");
                break;
            case IS_NOT:
                criteria.append(" != ? ");
                break;
            case BETWEEN:
                criteria.append(" between ? and ? ");
                break;
            case NOT_BETWEEN:
                criteria.append(" not between ? and ? ");
                break;
            case IS_NULL:
                criteria.append(" is null ");
                break;
            case IS_NOT_NULL:
                criteria.append(" is not null ");
                break;
            case GT:
                criteria.append(" > ? ");
                break;
            case GTE:
                criteria.append(" >= ? ");
                break;
            case LT:
                criteria.append(" < ? ");
                break;
            case LTE:
                criteria.append(" <= ? ");
                break;

            case NOT_IN:
                criteria.append(" not ");
            case IN:
                criteria.append(" in (").append(StringUtils.repeat("?", ",", filter.getValue().length)).append(") ");
                break;

            case NOT_EXISTS:
                criteria.append(" not ");
            case EXISTS:
                //todo filter filter.value
                criteria.append(" exist (").append(filter.getValue()[0]).append(") ");
                break;
            case CONTAIN:
                criteria.append(" like concat(?, '%') ");
                break;

        }
        if(filter.getValue() !=null && filter.getValue().length>0
                && filter.getComparison() != Comparison.EXISTS
                && filter.getComparison() != Comparison.NOT_EXISTS) {
            params.addAll(Lists.newArrayList(filter.getValue()));
        }
    }



}
