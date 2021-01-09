package com.bixuebihui.tablegen.dbinfo;
/**
 * @author xwx
 */
public interface IIdentifier {

	@Override
    boolean equals(Object rhs);

	@Override
    String toString();

	@Override
    int hashCode();
}
