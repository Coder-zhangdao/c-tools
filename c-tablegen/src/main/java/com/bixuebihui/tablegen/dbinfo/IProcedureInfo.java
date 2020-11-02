package com.bixuebihui.tablegen.dbinfo;

public interface IProcedureInfo extends IDatabaseObjectInfo {
	String getRemarks();
	int getType();
	String getTypeDescription();
}
