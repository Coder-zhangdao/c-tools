package com.bixuebihui.tablegen.dbinfo;
/**
 * @author xwx
 */
public interface IIdentifier {

	boolean equals(Object rhs);

	String toString();

	int hashCode();
}
