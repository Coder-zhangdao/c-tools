package com.bixuebihui.cache;


/**
 * @author xwx
 */
public class DictionaryItem {

    private String id;
    private String value;
    private String sort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        return "{" + id + "," + value + "," + sort + "}";
    }
}
