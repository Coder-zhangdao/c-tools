package com.bixuebihui.jmesa.elasticsearch.query;


import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Map;

public class Params implements ArrayableInterface {
    Map<String, Object> rawParams;
    Map<String, Object> params;

    public static <T> String getParamName(Class<T> claz) {
        String[] all = claz.getName().split("\\.");
        String last = all[all.length - 1];
        return camelToSnake(last);
    }

    // Function to convert camel case
    // string to snake case string
    public static String camelToSnake(String str)
    {

        // Empty String
        String result = "";

        // Append first character(in lower case)
        // to result string
        char c = str.charAt(0);
        result = result + Character.toLowerCase(c);

        // Tarverse the string from
        // ist index to last index
        for (int i = 1; i < str.length(); i++) {

            char ch = str.charAt(i);

            // Check if the character is upper case
            // then append '_' and such character
            // (in lower case) to result string
            if (Character.isUpperCase(ch)) {
                result = result + '_';
                result
                        = result
                        + Character.toLowerCase(ch);
            }

            // If the character is lower case then
            // add such character into result string
            else {
                result = result + ch;
            }
        }

        // return the result
        return result;
    }

    public Map<String, Object> getRawParams() {
        return rawParams;
    }

    public Params setRawParams(Map<String, Object> rawParams) {
        this.rawParams = rawParams;
        return this;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Params setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public Params setParam(String key, Object value) {
        this.params.put(key, value);
        return this;
    }

    protected String getBaseName() {
        return getParamName(this.getClass());
    }

    /**
     * Converts the params to an array. A default implementation exist to create
     * the an array out of the class name (last part of the class name)
     * and the params.
     *
     * @return array Filter array
     */
    public Map toArray() {
        Map data = Maps.newHashMap();

        data.put(this.getBaseName(), this.getParams());

        if (this.rawParams!=null && !this.rawParams.isEmpty()) {
            data.putAll(rawParams);
        }

        return this._convertArrayable(data);
    }

    /**
     * Cast objects to arrays.
     *
     * @param array array
     * @return array
     */
    protected Map _convertArrayable(Map array) {
        Map arr = Maps.newHashMap();

        array.forEach((key,value) -> {
            if (value instanceof ArrayableInterface) {
                ArrayableInterface v = (ArrayableInterface) value;
                arr.put(v instanceof NameableInterface
                        ? ((NameableInterface) v).getName() : key, v.toArray());
            } else if (value instanceof Map) {
                arr.put(key, this._convertArrayable((Map) value));
            } else {
                if(value==null){
                    arr.put(key, Collections.emptyMap());
                }else {
                    arr.put(key, value);
                }
            }
        });

        return arr;
    }

}
