package com.bixuebihui.tablegen.dbinfo;

public class DatabaseObjectInfo implements IDatabaseObjectInfo
{
	/** Property names for this bean. */
	public interface IPropertyNames
	{
		/** Catalog name. */
		String CATALOG_NAME = "catalogName";

		/** Schema name. */
		String SCHEMA_NAME = "schemaName";

		/** Simple name. */
		String SIMPLE_NAME = "simpleName";

		/** Qualified name. */
		String QUALIFIED_NAME = "qualifiedName";
	}

	/** Catalog name. Can be <CODE>null</CODE> */
	private final String _catalog;

	/** Schema name. Can be <CODE>null</CODE> */
	private final String _schema;

	/** Simple object name. */
	private final String _simpleName;

	/** Object type. @see DatabaseObjectType.*/
	private DatabaseObjectType _dboType = DatabaseObjectType.OTHER;

	public DatabaseObjectInfo(String catalog, String schema, String simpleName,
								DatabaseObjectType dboType)
	{
		super();
		if (dboType == null)
		{
			throw new IllegalArgumentException("Null DatabaseObjectType passed");
		}

		_catalog = catalog;
		_schema = schema;
		_simpleName = simpleName;
		_dboType = dboType;
	}

	@Override
    public String toString()
	{
		return getSimpleName();
	}

	@Override
    public String getCatalogName()
	{
		return _catalog;
	}

	@Override
    public String getSchemaName()
	{
		return _schema;
	}

	@Override
    public String getSimpleName()
	{
		return _simpleName;
	}



	@Override
    public DatabaseObjectType getDatabaseObjectType()
	{
		return _dboType;
	}



	@Override
    public boolean equals(Object obj)
	{
		if (obj instanceof DatabaseObjectInfo)
		{
			DatabaseObjectInfo info = (DatabaseObjectInfo) obj;
			if ((info._catalog == null && _catalog == null)
				|| ((info._catalog != null && _catalog != null)
					&& info._catalog.equals(_catalog)))
			{

					if ((info._schema == null && _schema == null)
						|| ((info._schema != null && _schema != null)
							&& info._schema.equals(_schema)))
					{
						return (
							(info._simpleName == null && _simpleName == null)
								|| ((info._simpleName != null
									&& _simpleName != null)
									&& info._simpleName.equals(_simpleName)));
					}


			}
		}
		return false;
	}

	@Override
    public int compareTo(Object o)
	{
		DatabaseObjectInfo other = (DatabaseObjectInfo) o;
		return _simpleName.compareTo(other._simpleName);
	}

	@Override
    public String getQualifiedName() {
		return _simpleName;
	}
}
