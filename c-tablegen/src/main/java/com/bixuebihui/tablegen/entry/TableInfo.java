package com.bixuebihui.tablegen.entry;

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author xwx
 */
public class TableInfo {
   String name;
   String comment;
   List<ColumnData> fields;

    public TableInfo(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", fields=" + fields +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        if(StringUtils.isBlank(comment)){
            return name;
        }
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ColumnData> getFields() {
        return fields;
    }

    public void setFields(List<ColumnData> fields) {
        this.fields = fields;
    }
}
