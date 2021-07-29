package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.jmesa.limit.RowSelect;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EsRequestTest {
    public static String host= "http://localhost:9200";
    public static String username = "elastic";
    public static String password = "changeme";

    EsRequest esRequest= new EsRequest(host, username, password);


    @Test
    public  void testQuery() {

        esRequest.indexName = "test*";

        String s0 = esRequest.query(Query.match_all(), 0, 10, null);
        System.out.println(s0);

        System.out.println("===========");


        String s = esRequest.query(Query
                .match(null, null).setFieldQuery("child_endpoint_name",
                        "{GET}/index/sysGenerator"), 0, 10, null);
        System.out.println(s);

    }

    @Test
    public void testMatchAll(){
        esRequest.indexName = "test*";
        String s0 = esRequest.query(Query.match_all(), 0, 10, null);
        System.out.println(s0);
    }

    @Test
    @Disabled("run once only")
    void getIndex() {
        String s0 = esRequest.createIndex("test");
        System.out.println(s0);
    }

    @Test
    void createDoc() throws JsonProcessingException {
        esRequest.indexName = "test";

        List<EsRequest.Document> list  = Lists.newArrayList();
        for(int i=0; i<10; i++) {
            EsRequest.Document doc = new EsRequest.Document(i+"", ImmutableMap.<String, Object>builder()
                    .put("my_id", "abc " + i)
                    .put("my_value", i * i)
                    .put("age", i +10)
                    .put("height", i +10+ new Random().nextInt(100))
                    .build());
            list.add(doc);
        }
        String s0 = esRequest.createDoc(esRequest.indexName, list);
        System.out.println(s0);
    }

    @Test
    public void testRange(){
        esRequest.indexName = "test";

        String s0 = esRequest.query(Query.range("age", ImmutableMap.<String, Object>builder()
                .put("from", 11)
                .put("to",18).build()), 0, 10);
        System.out.println(s0);
    }


    @Test
    public void  esJsonToTableJson() throws IOException {
       String  json = "{ " +
                "     \"took\" : 17, " +
                "     \"timed_out\" : false, " +
                "     \"_shards\" : { " +
                "     \"total\" : 1, " +
                "     \"successful\" : 1, " +
                "     \"skipped\" : 0, " +
                "     \"failed\" : 0 " +
                "     }, " +
                "     \"hits\" : { " +
                "     \"total\" : { " +
                "     \"value\" : 2, " +
                "     \"relation\" : \"eq\" " +
                "     }, " +
                "     \"max_score\" : 1.0, " +
                "     \"hits\" : [ " +
                "     { " +
                "     \"_index\" : \"my-index-000001\", " +
                "     \"_type\" : \"_doc\", " +
                "     \"_id\" : \"1\", " +
                "     \"_score\" : 1.0, " +
                "     \"_source\" : { " +
                "     \"color\" : [ " +
                "     \"blue\", " +
                "     \"green\" " +
                "     ] " +
                "     } " +
                "     }, " +
                "     { " +
                "     \"_index\" : \"my-index-000001\", " +
                "     \"_type\" : \"_doc\", " +
                "     \"_id\" : \"2\", " +
                "     \"_score\" : 1.0, " +
                "     \"_source\" : { " +
                "     \"color\" : \"blue\" " +
                "     } " +
                "     } " +
                "     ] " +
                "     } " +
                "     }";

        /**
         *
         {
         "caption":"t_config",
         "titles": [
         "C _key",
         "c_name",
         "c_value"],
         "data": [{
         "c_key":"key1",
         "c_name":"name1",
         "c_value":"value1"}
         ],

         "paging":{"page":2,"maxRows":1,"rowEnd":2,"rowStart":1,"totalRows":3}}
         */

        RowSelect rowSelect = new RowSelect(2, 10000, 50);
        Object result = esRequest.esJsonToTableJson(json, rowSelect);


        System.out.println(result);
        assertEquals("{\n" +
                "  \"caption\" : \"my-index-000001\",\n" +
                "  \"titles\" : [ \"color\" ],\n" +
                "  \"_id\" : [ \"1\", \"2\" ],\n" +
                "  \"data\" : [ {\n" +
                "    \"color\" : [ \"blue\", \"green\" ]\n" +
                "  }, {\n" +
                "    \"color\" : \"blue\"\n" +
                "  } ],\n" +
                "  \"paging\" : {\n" +
                "    \"totalRows\" : 2,\n" +
                "    \"maxRows\" : 10000,\n" +
                "    \"rowEnd\" : 50,\n" +
                "    \"rowStart\" : 0,\n" +
                "    \"page\" : 1\n" +
                "  }\n" +
                "}", result);

    }

    @Test
    public void queryAllIndex(){
        List<String> indexList = esRequest.getAllIndexList();
        System.out.println(StringUtils.join(indexList,","));
    }

    @Test
    public void queryData(){
        esRequest.indexName = "library*";
        String json = esRequest.query(Query.match_all(), 0, 2);
        System.out.println(json);
    }
}
