package com.bixuebihui.tablegen.dbinfo;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

public class ProcedureInfo extends DatabaseObjectInfo implements IProcedureInfo
{
	/**
	 * This interface defines locale specific strings. This should be
	 * replaced with a property file.
	 */
	private interface i18n
	{
		String DATABASE = "Database";
		//String NO_CATALOG = "No Catalog"; // i18n or Replace with md.getCatalogueTerm.
		String MAY_RETURN = "May return a result";
		String DOESNT_RETURN = "Does not return a result";
		String DOES_RETURN = "Returns a result";
		String UNKNOWN = "Unknown";
	}

	/** Procedure Type. */
	private final int _procType;

	/** Procedure remarks. */
	private final String _remarks;

	ProcedureInfo(String catalog, String schema, String simpleName,
							String remarks, int procType) throws SQLException
	{
		super(catalog, schema, simpleName, DatabaseObjectType.PROCEDURE);
		_remarks = remarks;
		_procType = procType;
	}

//TODO: Rename to getProcedureType().
	public int getType()
	{
		return _procType;
	}

	public String getRemarks()
	{
		return _remarks;
	}

//TODO: Rename to getProcedureTypeDescription().
	public String getTypeDescription()
	{
		switch (_procType)
		{
			case DatabaseMetaData.procedureNoResult :
				return i18n.DOESNT_RETURN;
			case DatabaseMetaData.procedureReturnsResult :
				return i18n.DOES_RETURN;
			case DatabaseMetaData.procedureResultUnknown :
				return i18n.MAY_RETURN;
			default :
				return i18n.UNKNOWN;
		}
	}

	public boolean equals(Object obj)
	{
		if (super.equals(obj) && obj instanceof ProcedureInfo)
		{
			ProcedureInfo info = (ProcedureInfo) obj;
			if ((info._remarks == null && _remarks == null)
				|| ((info._remarks != null && _remarks != null)
					&& info._remarks.equals(_remarks)))
			{
				return info._procType == _procType;
			}
		}
		return false;
	}

}
