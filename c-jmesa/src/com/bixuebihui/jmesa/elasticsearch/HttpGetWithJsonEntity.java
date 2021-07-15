package com.bixuebihui.jmesa.elasticsearch;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class HttpGetWithJsonEntity extends HttpEntityEnclosingRequestBase {
    public final static String METHOD_NAME = "GET";

    public static Builder builder(){
        return new Builder();
    }

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public static class Builder{
        String url;
        String body;
        public Builder withUrl(String url){
            this.url = url;
            return this;
        }
        public Builder withBodyEntry(String body){
            this.body = body;
            return this;
        }

        public HttpGetWithJsonEntity build() throws URISyntaxException, UnsupportedEncodingException {
            HttpGetWithJsonEntity request = new HttpGetWithJsonEntity();
            request.setURI(new URI(this.url));
            request.setEntity(new StringEntity(this.body,APPLICATION_JSON));
            return request;
        }
    }


}
