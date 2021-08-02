package com.bixuebihui.tablegen;

/**
  * FKDefinition
  * <p>A Container for a Foreign Key definition for a table</p>
  * @author  I.Holsman
  * @version 1.6
  * (c) Ian Holsman 1998
  * Released under GPL. See LICENSE for full details.
  */

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ForeignKeyDefinition
{
	protected String foreignKeyName;
	protected String foreignKeyTableName;
	protected String primaryKeyTableName;
	protected ArrayList<String> foreignKeyFields;
	protected ArrayList<String> primaryKeyFields;
	/**
	  * Constructor
	  */
	public ForeignKeyDefinition(String pPKTable, String pFKTable, String pFKName )
	{
		foreignKeyTableName = pFKTable;
		primaryKeyTableName = pPKTable;
		if ( pFKName != null ) {
            foreignKeyName = pFKName;
        } else {
            foreignKeyName = foreignKeyTableName + primaryKeyTableName;
        }
		foreignKeyFields = new ArrayList<>();
		primaryKeyFields = new ArrayList<>();
	}

	public String getForeignKeyName() {
		return foreignKeyName;
	}

	public  String getPKTableName( ) { return primaryKeyTableName;}
	public  String getFKTableName( ) { return foreignKeyTableName;}
	/**
	 * add the corresponding Foreign Key Name and Primary Key name
	 */
	public void addField( String FKFieldName, String PKFieldName )
		{
			foreignKeyFields.add( FKFieldName);
			primaryKeyFields.add( PKFieldName);
		}
	public String getFKColList() {
		return StringUtils.join(foreignKeyFields.toArray(),",").toLowerCase();
	}
	public String getPKColList() {
		return StringUtils.join(primaryKeyFields.toArray(), ",").toLowerCase();
	}


	public String getPKColumnName()
	{
		return primaryKeyFields.get(0);
	}
	public  String getFKColumnName( )
	{
		return foreignKeyFields.get(0);
	}
	public List<String> getFKFields()
	{
		return foreignKeyFields;
	}
	public List<String> getPKFields()
	{
		return primaryKeyFields;
	}

	@Override
	public String toString() {
		return "FKDefinition{" +
				"foreignKeyName='" + foreignKeyName + '\'' +
				", foreignKeyTableName='" + foreignKeyTableName + '\'' +
				", primaryKeyTableName='" + primaryKeyTableName + '\'' +
				", foreignKeyFields=" + foreignKeyFields +
				", primaryKeyFields=" + primaryKeyFields +
				'}';
	}
}
