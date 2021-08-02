package com.bixuebihui;

/**
 * used to suppress throw SQLException everywhere
 */
public class DbException extends RuntimeException {

    public DbException(Exception e) {
        super("SQLException", e);
    }

    public DbException(String s) {
        super(s);
    }
}
