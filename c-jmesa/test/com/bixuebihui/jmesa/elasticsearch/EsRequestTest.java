package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EsRequestTest {

    @Test
    public void testMatchAll(){
        EsRequest esRequest= new EsRequest();
        esRequest.setEsObject(new EsObject("test*"));

        String s0 = esRequest.query(Query.match_all(), 0, 10);
        System.out.println(s0);
    }

    @Test
    @Disabled("run once only")
    void getIndex() {
        EsRequest esRequest= new EsRequest();
        String s0 = esRequest.createIndex("test");
        System.out.println(s0);
    }

    @Test
    void createDoc() throws JsonProcessingException {
        EsRequest esRequest= new EsRequest();
        esRequest.setEsObject(new EsObject("test"));

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
        String s0 = esRequest.createDoc(esRequest.esObject.indexName, list);
        System.out.println(s0);
    }

    @Test
    public void testRange(){
        EsRequest esRequest= new EsRequest();
        esRequest.setEsObject(new EsObject("test"));

        String s0 = esRequest.query(Query.range("age", ImmutableMap.<String, Object>builder()
                .put("from", 11)
                .put("to",18).build()), 0, 10);
        System.out.println(s0);
    }


    @Test
    public void  esJsonToTableJson() throws IOException {
       String  json = "{\n" +
                "     \"took\" : 17,\n" +
                "     \"timed_out\" : false,\n" +
                "     \"_shards\" : {\n" +
                "     \"total\" : 1,\n" +
                "     \"successful\" : 1,\n" +
                "     \"skipped\" : 0,\n" +
                "     \"failed\" : 0\n" +
                "     },\n" +
                "     \"hits\" : {\n" +
                "     \"total\" : {\n" +
                "     \"value\" : 2,\n" +
                "     \"relation\" : \"eq\"\n" +
                "     },\n" +
                "     \"max_score\" : 1.0,\n" +
                "     \"hits\" : [\n" +
                "     {\n" +
                "     \"_index\" : \"my-index-000001\",\n" +
                "     \"_type\" : \"_doc\",\n" +
                "     \"_id\" : \"1\",\n" +
                "     \"_score\" : 1.0,\n" +
                "     \"_source\" : {\n" +
                "     \"color\" : [\n" +
                "     \"blue\",\n" +
                "     \"green\"\n" +
                "     ]\n" +
                "     }\n" +
                "     },\n" +
                "     {\n" +
                "     \"_index\" : \"my-index-000001\",\n" +
                "     \"_type\" : \"_doc\",\n" +
                "     \"_id\" : \"2\",\n" +
                "     \"_score\" : 1.0,\n" +
                "     \"_source\" : {\n" +
                "     \"color\" : \"blue\"\n" +
                "     }\n" +
                "     }\n" +
                "     ]\n" +
                "     }\n" +
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

        Object result = new EsRequest().esJsonToTableJson(json);


        System.out.println(result);
        assertEquals("{\n" +
                "  \"caption\" : \"my-index-000001\",\n" +
                "  \"titles\" : [ \"color\" ],\n" +
                "  \"data\" : [ {\n" +
                "    \"color\" : [ \"blue\", \"green\" ]\n" +
                "  }, {\n" +
                "    \"color\" : \"blue\"\n" +
                "  } ],\n" +
                "  \"paging\" : {\n" +
                "    \"totalRows\" : 2\n" +
                "  }\n" +
                "}", result);

    }
}
