package com.bixuebihui.tablegen.dbinfo;

/**
 * @author xwx
 */
public class IntegerIdentifier implements IIdentifier {
	public interface IPropertyNames {
		String STRING = "string";
	}

	private int _id;

	public IntegerIdentifier(int value) {
		super();
		_id = value;
	}

	@Override
	public boolean equals(Object rhs) {
		boolean rc = false;
		if (rhs != null && rhs.getClass().equals(getClass())) {
			rc = ((IntegerIdentifier)rhs).toString().equals(toString());
		}
		return rc;
	}

	@Override
	public  int hashCode() {
		return _id;
	}

	@Override
    public String toString() {
		return "" + _id;
	}

	// Only for restoring from XML etc.
	public void setString(String value) {
		_id = Integer.parseInt(value);
	}
}
