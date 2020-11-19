package com.bixuebihui.tablegen.dbinfo;
/**
 * @author xwx
 */
public class IntegerIdentifierFactory
{
	private int _next;

	public IntegerIdentifierFactory()
	{
		this(0);
	}

	public IntegerIdentifierFactory(int initialValue)
	{
		super();
		_next = initialValue;
	}

	public synchronized IntegerIdentifier createIdentifier()
	{
		return new IntegerIdentifier(_next++);
	}
}
