package com.bixuebihui.tablegen.dbinfo;


public interface IDatabaseObjectInfo extends Comparable
{
	String getCatalogName();
	String getSchemaName();
	String getSimpleName();
	String getQualifiedName();

	/**
	 * Return the type for this object. @see DatabaseObjectType.
	 */
	DatabaseObjectType getDatabaseObjectType();
}
