// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:58
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   DataType.java

package com.bixuebihui.util.db;


public class DataType {

    public DataType(String _name, int _type) {
        this(_name, _type, 0);
    }

    public DataType(String _name, int _type, int _maxLength) {
        name = _name;
        type = _type;
        maxLength = _maxLength;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getMaxLength() {
        return maxLength < 0 ? -maxLength : maxLength;
    }

    public boolean isCharData() {
        int nType = getType();
        return nType == 12 || nType == 1 || nType == -1 || nType == 2005;
    }

    public boolean isLengthDefinedByUser() {
        return maxLength < 0;
    }

    public String toString() {
        return "Name=" + name + " Type=" + type;
    }

    public boolean equals(Object _obj) {
        return (_obj instanceof DataType) && ((DataType) _obj).name.compareToIgnoreCase(name) == 0;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public static final int LENGTH_UNKOWN = 0;
    private String name;
    private int type;
    private int maxLength;
}
