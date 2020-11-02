// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-1-4 11:29:55
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   TimeoutException.java

package com.bixuebihui.util;


/**
 * <p>TimeoutException class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class TimeoutException extends Exception
{

    /**
     *
     */
    private static final long serialVersionUID = -461278587077013750L;

    /**
     * <p>Constructor for TimeoutException.</p>
     *
     * @param s a {@link java.lang.String} object.
     */
    public TimeoutException(String s) {
        super(s);
    }
}
