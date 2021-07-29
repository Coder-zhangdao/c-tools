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
	private final String catalog;

	/** Schema name. Can be <CODE>null</CODE> */
	private final String schema;

	/** Simple object name. */
	private final String simpleName;

	/** Object type. @see DatabaseObjectType.*/
	private DatabaseObjectType dboType = DatabaseObjectType.OTHER;

	public DatabaseObjectInfo(String catalog, String schema, String simpleName,
								DatabaseObjectType dboType)
	{
		super();
		if (dboType == null)
		{
			throw new IllegalArgumentException("Null DatabaseObjectType passed");
		}

		this.catalog = catalog;
		this.schema = schema;
		this.simpleName = simpleName;
		this.dboType = dboType;
	}

	@Override
    public String toString()
	{
		return getSimpleName();
	}

	@Override
    public String getCatalogName()
	{
		return catalog;
	}

	@Override
    public String getSchemaName()
	{
		return schema;
	}

	@Override
    public String getSimpleName()
	{
		return simpleName;
	}



	@Override
    public DatabaseObjectType getDatabaseObjectType()
	{
		return dboType;
	}



	@Override
    public boolean equals(Object obj)
	{
		if (obj instanceof DatabaseObjectInfo)
		{
			DatabaseObjectInfo info = (DatabaseObjectInfo) obj;
			if ((info.catalog == null && catalog == null)
				|| ((info.catalog != null && catalog != null)
					&& info.catalog.equals(catalog)))
			{

					if ((info.schema == null && schema == null)
						|| ((info.schema != null && schema != null)
							&& info.schema.equals(schema)))
					{
						return (
							(info.simpleName == null && simpleName == null)
								|| ((info.simpleName != null
									&& simpleName != null)
									&& info.simpleName.equals(simpleName)));
					}


			}
		}
		return false;
	}

	@Override
    public int compareTo(Object o)
	{
		DatabaseObjectInfo other = (DatabaseObjectInfo) o;
		return simpleName.compareTo(other.simpleName);
	}

	@Override
    public String getQualifiedName() {
		return simpleName;
	}
}
