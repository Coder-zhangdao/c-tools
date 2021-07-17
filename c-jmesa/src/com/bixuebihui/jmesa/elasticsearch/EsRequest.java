package com.bixuebihui.jmesa.elasticsearch;

import com.bixuebihui.jmesa.elasticsearch.query.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static com.bixuebihui.jmesa.elasticsearch.EsObject.EMPTY_JSON_STRING;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class EsRequest {

    String host = "http://127.0.0.1:9200";

    public static void main(String[] args) {

        EsRequest esRequest = new EsRequest();
        esRequest.setEsObject(new EsObject("test*"));

        String s0 = esRequest.query(Query.match_all(), 0, 10);
        System.out.println(s0);

        System.out.println("===========");


        String s = esRequest.query(Query
                .match(null, null).setFieldQuery("child_endpoint_name",
                        "{GET}/index/sysGenerator"), 0, 10);
        System.out.println(s);

    }

    public EsObject getEsObject() {
        return esObject;
    }

    public void setEsObject(EsObject esObject) {
        this.esObject = esObject;
    }

    public String getHost() {
        return host;
    }

    EsObject esObject;

    public void setHost(String host) {
        this.host = host;
    }

    public String createDoc(String indexName, List<Document> docs) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpEntityEnclosingRequestBase post;
        post = new HttpPost(host +"/"+EsObject.ACTION_BULK);
        StringBuilder sb = new StringBuilder();

        for(Document doc : docs) {
            Map<String, Object> map1 = Maps.newHashMap();
            Map<String, Object> map2 = Maps.newHashMap();
            map2.put("_index", indexName);
            map2.put("_id", doc.id);
            map2.put("_type", "_doc");
            map1.put("index", map2);
            sb.append(objectMapper.writeValueAsString(map1)).append("\n");

            sb.append(objectMapper.writeValueAsString(doc.values)).append("\n");
        }
        post.setEntity(new StringEntity(sb.toString(), APPLICATION_JSON));

        return httpJson(post);
    }

    public String createIndex(String indexName) {

        HttpEntityEnclosingRequestBase post;
        post = new HttpPut(host + "/" + indexName);

        return httpJson(post);
    }

    private String httpJson(HttpEntityEnclosingRequestBase requestBase) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {

            HttpResponse response = httpClient.execute(requestBase);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode() +
                        " with message:" + getResponseString(response));
            }

            return getResponseString(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EMPTY_JSON_STRING;
    }

    public String query(Query query, int from, int size) {
        try {
            HttpGetWithJsonEntity getRequest =
                    HttpGetWithJsonEntity.builder()
                            .withUrl(host + "/" + esObject.indexName + "/" + EsObject.ACTION_SEARCH)
                            .withBodyEntry(esObject.build(query, from, size))
                            .build();

            return httpJson(getRequest);
        } catch (UnsupportedEncodingException | URISyntaxException e) {
            e.printStackTrace();
        }
        return EMPTY_JSON_STRING;
    }

    static class Document{
        String id;
        Map<String, Object> values;

        public Document(String id, Map<String, Object> values) {
            this.id = id;
            this.values = values;
        }
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
