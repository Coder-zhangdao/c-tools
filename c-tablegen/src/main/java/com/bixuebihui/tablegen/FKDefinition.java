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

import java.util.*;

public class FKDefinition
{
	public String getFKname() {
		return FKname;
	}

	//Unread public/protected field NOT USED
	protected String FKname;

	protected String FKtable;
	protected String PKtable;
	protected ArrayList<String> FKfields;
	protected ArrayList<String> PKfields;
	/**
	  * Constructor
	  */
	public FKDefinition( String pPKTable, String pFKTable, String pFKName )
	{
		FKtable = pFKTable;
		PKtable = pPKTable;
		if ( pFKName != null )
			FKname = pFKName;
		else
			FKname = FKtable + PKtable;
		FKfields = new ArrayList<>();
		PKfields = new ArrayList<>();
	}
	public  String getPKTableName( ) { return PKtable;}
	public  String getFKTableName( ) { return FKtable;}
	/**
	 * add the corresponding Foreign Key Name and Primary Key name
	 */
	public void addField( String FKFieldName, String PKFieldName )
		{
			FKfields.add( FKFieldName);
			PKfields.add( PKFieldName);
		}
	public String getFKColList() {
		return StringUtils.join(FKfields.toArray(),",").toLowerCase();
	}
	public String getPKColList() {
		return StringUtils.join(PKfields.toArray(), ",").toLowerCase();
	}


	public String getPKColumnName()
	{
		return PKfields.get(0);
	}
	public  String getFKColumnName( )
	{
		return FKfields.get(0);
	}
	public List<String> getFKFields()
	{
		return FKfields;
	}
	public List<String> getPKFields()
	{
		return PKfields;
	}

	@Override
	public String toString() {
		return "FKDefinition{" +
				"FKname='" + FKname + '\'' +
				", FKtable='" + FKtable + '\'' +
				", PKtable='" + PKtable + '\'' +
				", FKfields=" + FKfields +
				", PKfields=" + PKfields +
				'}';
	}
}
