package com.bixuebihui.tablegen.dbinfo;

/**
 * @author xwx
 */
public class IntegerIdentifier implements IIdentifier {
	public interface IPropertyNames {
		String STRING = "string";
	}

	private int id;

	public IntegerIdentifier(int value) {
		super();
		id = value;
	}

	@Override
	public boolean equals(Object rhs) {
		boolean rc = false;
		if (rhs != null && rhs.getClass().equals(getClass())) {
			rc = rhs.toString().equals(toString());
		}
		return rc;
	}

	@Override
	public  int hashCode() {
		return id;
	}

	@Override
    public String toString() {
		return "" + id;
	}

	// Only for restoring from XML etc.
	public void setString(String value) {
		id = Integer.parseInt(value);
	}
}
