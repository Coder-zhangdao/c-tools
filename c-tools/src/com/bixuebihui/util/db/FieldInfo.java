// Decompiled by DJ v3.8.8.85 Copyright 2005 Atanas Neshkov  Date: 2006-1-26 15:56:58
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   FieldInfo.java

package com.bixuebihui.util.db;


// Referenced classes of package com.bixuebihui.util.database:
//            DBType, DataType

public class FieldInfo {

    public FieldInfo(DBType _dbType, String _dataTypeName, int _dataLength, boolean _nullable, int _columnID, String _dataDefault, int _dataScale) {
        dataTypeName = _dataTypeName;
        dataType = _dbType.getDataType(_dataTypeName);
        dataLength = _dataLength;
        nullable = _nullable;
        columnID = _columnID;
        dataDefault = _dataDefault;
        dataScale = _dataScale;
    }

    public String getDataTypeName() {
        return dataTypeName;
    }

    public DataType getDataType() {
        return dataType;
    }

    public int getDataLength() {
        return dataLength;
    }

    public boolean isNullable() {
        return nullable;
    }

    public int getColumnID() {
        return columnID;
    }

    public String getDataDefault() {
        return dataDefault;
    }

    public String toString() {
        return "DATATYPE:" + dataType + " DATALENGTH:" + dataLength + " ISNULLABLE:" + nullable + " COLUMNID:" + columnID + " DEFAULTDATA:" + dataDefault;
    }

    public int getDataScale() {
        return dataScale;
    }

    private DataType dataType;
    private String dataTypeName;
    private int dataLength;
    private int dataScale;
    private boolean nullable;
    private int columnID;
    private String dataDefault;
}
