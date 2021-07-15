package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EsRequestTest {


    @Test
    public void testMatchAll(){
        EsRequest esRequest= new EsRequest(new EsObject("cmp_endpoint_relation_resp_time*"));

        String s0 = esRequest.getJson(Query.match_all(), 0, 10);
        System.out.println(s0);
    }

}
