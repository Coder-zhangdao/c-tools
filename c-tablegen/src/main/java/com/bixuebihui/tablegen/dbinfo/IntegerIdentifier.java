package com.bixuebihui.tablegen.dbinfo;

public class IntegerIdentifier implements IIdentifier {
	public interface IPropertyNames {
		String STRING = "string";
	}

	private int _id;

	public IntegerIdentifier(int value) {
		super();
		_id = value;
	}

	public boolean equals(Object rhs) {
		boolean rc = false;
		if (rhs != null && rhs.getClass().equals(getClass())) {
			rc = ((IntegerIdentifier)rhs).toString().equals(toString());
		}
		return rc;
	}

	public  int hashCode() {
		return _id;
	}

	public String toString() {
		return "" + _id;
	}

	// Only for restoring from XML etc.
	public void setString(String value) {
		_id = Integer.parseInt(value);
	}
}
