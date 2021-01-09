package com.bixuebihui.tablegen.dbinfo;

/**
 * @author xwx
 */
public interface IProcedureInfo extends IDatabaseObjectInfo {
	/**
	 * @return return comments
	 */
	String getRemarks();

	/**
	 * return procedure type
	 * @return  "void","ResultSet","int"
	 */
	int getProcedureType();

	/**
	 * return procedure type description
	 * @return type description
	 */
	String getProcedureTypeDescription();
}
