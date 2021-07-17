package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

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


}
