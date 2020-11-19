package com.bixuebihui.tablegen.dbinfo;

/**
 * @author xwx
 */
public interface IProcedureInfo extends IDatabaseObjectInfo {
	String getRemarks();
	int getType();
	String getTypeDescription();
}
