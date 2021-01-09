package com.bixuebihui.datasource;


import com.bixuebihui.dbcon.DatabaseConfig;
import org.apache.commons.dbcp2.BasicDataSource;


/**
 * <p>DbcpDataSource class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class DbcpDataSource extends BasicDataSource implements  INamingPool {


    private String alias;

    /**
     * <p>Getter for the field <code>alias</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @Override
    public String getAlias() {
        return alias;
    }

    /** {@inheritDoc} */
    @Override
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /** {@inheritDoc} */
    @Override
    public void setDatabaseConfig(DatabaseConfig cfg) {
        this.setAlias(cfg.getAlias());
        this.setDriverClassName(cfg.getClassName());
        this.setUrl(cfg.getDburl());
        this.setUsername(cfg.getUsername());
        this.setPassword(cfg.getPassword());
        this.setMaxTotal(cfg.getMaxActive());
        this.setMaxIdle(cfg.getMaxIdle());
        this.setMaxWaitMillis(cfg.getMaxWaitTime());
        this.setMaxOpenPreparedStatements(cfg.getMaxOpenPreparedStatements());
    }


}
