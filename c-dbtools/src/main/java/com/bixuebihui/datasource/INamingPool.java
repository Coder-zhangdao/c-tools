package com.bixuebihui.datasource;

import com.bixuebihui.dbcon.DatabaseConfig;

/**
 * <p>INamingPool interface.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public interface INamingPool {
    /**
     * <p>getAlias.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    String getAlias();

    /**
     * <p>setAlias.</p>
     *
     * @param alias a {@link java.lang.String} object.
     */
    void setAlias(String alias);

    /**
     * <p>setDatabaseConfig.</p>
     *
     * @param cfg a {@link DatabaseConfig} object.
     */
    void setDatabaseConfig(DatabaseConfig cfg);
}
