package com.bixuebihui.tablegen.dbinfo;


/**
 * @author xwx
 */
public class DatabaseObjectType implements IHasIdentifier
{
	/** Factory to generate unique IDs for these objects. */
	private final static IntegerIdentifierFactory s_idFactory = new IntegerIdentifierFactory();

	/** Other - general purpose. */
	public final static DatabaseObjectType OTHER = createNewDatabaseObjectType();

	/** Catalog. */
	public final static DatabaseObjectType CATALOG = createNewDatabaseObjectType();

	/** Column. */
	public final static DatabaseObjectType COLUMN = createNewDatabaseObjectType();

	/** Database. */
	public final static DatabaseObjectType SESSION = createNewDatabaseObjectType();

	/** Standard datatype. */
	public final static DatabaseObjectType DATATYPE = createNewDatabaseObjectType();

	/** Foreign Key relationship. */
	public final static DatabaseObjectType FOREIGN_KEY = createNewDatabaseObjectType();

	/** Function. */
	public final static DatabaseObjectType FUNCTION = createNewDatabaseObjectType();

	/** Index. */
	public final static DatabaseObjectType INDEX = createNewDatabaseObjectType();

	/** Stored procedure. */
	public final static DatabaseObjectType PROCEDURE = createNewDatabaseObjectType();

	/** Schema. */
	public final static DatabaseObjectType SCHEMA = createNewDatabaseObjectType();

	/**
	 * An object that generates uniques IDs for primary keys. E.G. an Oracle
	 * sequence.
	 */
	public final static DatabaseObjectType SEQUENCE = createNewDatabaseObjectType();

	/** TABLE. */
	public final static DatabaseObjectType TABLE = createNewDatabaseObjectType();

	/** Trigger. */
	public final static DatabaseObjectType TRIGGER = createNewDatabaseObjectType();

	/** User defined type. */
	public final static DatabaseObjectType UDT = createNewDatabaseObjectType();

	/** A database user. */
	public final static DatabaseObjectType USER = createNewDatabaseObjectType();

	/** Uniquely identifies this Object. */
	private final IIdentifier _id;

	/**
	 * Default ctor.
	 */
	private DatabaseObjectType()
	{
		super();
		_id = s_idFactory.createIdentifier();
	}

	/**
	 * Return the object that uniquely identifies this object.
	 *
	 * @return	Unique ID.
	 */
	@Override
	public IIdentifier getIdentifier()
	{
		return _id;
	}

	public static DatabaseObjectType createNewDatabaseObjectType()
	{
		return new DatabaseObjectType();
	}
}
