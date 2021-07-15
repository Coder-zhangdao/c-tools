package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class EsRequest {

    EsObject esObject;


    String host = "http://yz-test1.es6.autohome.com.cn:80";

    public EsRequest(EsObject esObject) {
        this.esObject = esObject;
    }

    public static void main(String[] args) {

        EsRequest esRequest= new EsRequest(new EsObject("cmp_endpoint_relation_resp_time*"));

        String s0 = esRequest.getJson(Query.match_all(), 0, 10);
        System.out.println(s0);

        System.out.println("===========");


        String s = esRequest.getJson(Query
                .match(null, null).setFieldQuery("child_endpoint_name",
                        "{GET}/index/sysGenerator"), 0, 10);
        System.out.println(s);

    }

    public String getJson(Query query, int from, int size){
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()){

            HttpGetWithJsonEntity getRequest =
                    HttpGetWithJsonEntity.builder()
                            .withUrl(host+"/"+esObject.indexName+"/"+ EsObject.ACTION)
                            .withBodyEntry(esObject.build(query, from, size))
                            .build();

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode()+"" +
                        " with message:"+ getResponseString(response));
            }

            return getResponseString(response);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    private String getResponseString(HttpResponse response) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));

        String output;
        StringBuilder sb = new StringBuilder();
        while ((output = br.readLine()) != null) {
            sb.append(output);
        }
        return sb.toString();
    }


}
