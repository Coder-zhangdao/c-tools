package com.bixuebihui.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.bixuebihui.dbcon.DatabaseConfig;


/**
 * <p>DruidDataSourceAdapter class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class DruidDataSourceAdapter extends DruidDataSource implements INamingPool {

    /**
     *
     */
    private static final long serialVersionUID = -3090433579721571673L;

    /**
     * <p>getAlias.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAlias() {
        return getName();
    }

    /** {@inheritDoc} */
    public void setAlias(String alias) {
        this.setName(alias);
    }

    /** {@inheritDoc} */
    public void setDatabaseConfig(DatabaseConfig cfg) {
        this.setAlias(cfg.getAlias());
        this.setDriverClassName(cfg.getClassName());
        this.setUrl(cfg.getDburl());
        this.setUsername(cfg.getUsername());
        this.setPassword(cfg.getPassword());
        this.setMaxActive(cfg.getMaxActive());
        this.setMaxWait(cfg.getMaxWaitTime());
        this.setMaxOpenPreparedStatements(cfg.getMaxOpenPreparedStatements());
        this.setRemoveAbandoned(removeAbandoned);
        this.setRemoveAbandonedTimeout(cfg.getMaxIdle());

        //防止过期
        this.setValidationQuery("SELECT 'x'");
        this.setTestWhileIdle(true);
        this.setTestOnBorrow(true);
    }


}
