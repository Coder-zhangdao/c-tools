package com.bixuebihui.jmesa.elasticsearch;

public class EsRequest {
    final String GET="GET";
    final String POST ="POST";

    final String DEFAULT_CONTENT_TYPE="application/json";


    String path;
    String method;
    String data;
    String query;



}
