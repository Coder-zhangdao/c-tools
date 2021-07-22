package com.bixuebihui;

import com.bixuebihui.dbcon.DatabaseConfig;
import com.bixuebihui.sql.ConnectionPool;
import com.bixuebihui.sql.ConnectionPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * <p>ConnectionManager class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class ConnectionManager {
    private static ConnectionPoolManager cpm;

    /**
     * 默认连接池的名称
     */
    private static String defaultAlias = "default";
    private static final Set<String> ALIAS_SET = Collections.synchronizedSet(new HashSet<>(5, 0.7f));
    private static final Logger LOG = LoggerFactory.getLogger(ConnectionManager.class);


    private static final String CONFIG_FILE = "/ConnectionPoolManager.xml";

    private static Document readProperties() {
        try (InputStream in = ConnectionPoolManager.class.getResourceAsStream(CONFIG_FILE)) {
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            return fact.newDocumentBuilder().parse(in);
        } catch (MissingResourceException e) {
            LOG.error("missing file:" + CONFIG_FILE);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            LOG.error("readProperties", e);
        }
        return null;
    }

    /**
     * <p>destroy.</p>
     */
    public static void destroy() {
        if (cpm != null) {
            cpm.destroy();
        }
        ALIAS_SET.clear();
    }


    /**
     * <p>getInstance.</p>
     *
     * @return a {@link ConnectionPoolManager} object.
     * @throws java.sql.SQLException if any.
     */
    public static synchronized ConnectionPoolManager getInstance() throws SQLException {
        if (cpm != null) {
            return cpm;
        }

        Document resources = readProperties();

        if (resources == null) {
            throw new SQLException("没有获得数据库连接池配置" + CONFIG_FILE + "文档! thread="
                    + Thread.currentThread().getName());
        } else {
            LOG.info("find xml^^^ "
                    + Thread.currentThread().getName());
        }

        try {
            if (cpm == null) {
                ConnectionPoolManager tmp;
                NodeList aliases = resources.getElementsByTagName("Alias");
                if (aliases != null && aliases.getLength() > 0) {
                    tmp = new ConnectionPoolManager(300);
                    defaultAlias = ((Element) aliases.item(0)).getAttribute("name");

                    for (int i = 0; i < aliases.getLength(); i++) {
                        Element alias = (Element) aliases.item(i);
                        ALIAS_SET.add(alias.getAttribute("name"));
                        Class.forName(alias.getAttribute("driver"));
                        tmp.addAlias(alias.getAttribute("name"), alias
                                        .getAttribute("driver"), alias
                                        .getAttribute("url"), alias
                                        .getAttribute("username"), alias
                                        .getAttribute("password"), Integer
                                        .parseInt(alias.getAttribute("maxConn")),
                                Integer.parseInt(alias
                                        .getAttribute("idleTimeout")),
                                Integer.parseInt(alias
                                        .getAttribute("checkoutTimeout")),
                                Integer.parseInt(alias
                                        .getAttribute("maxCheckout")));
                    }
                    cpm = tmp;
                } else {
                    LOG.error("bad format " + CONFIG_FILE + " file, expected Alias element but not found");
                }
            }
        } catch (IllegalAccessException | ClassNotFoundException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            LOG.error("", e);
        }
        return cpm;
    }

    /**
     * Used by servlets and JSPs to get a connection from the pool.
     *
     * @return 池化的数据库连接
     * @throws java.sql.SQLException 数据库出错
     */
    public Connection getConnection() throws SQLException {
        if (cpm == null) {
            getInstance();
        }
        return cpm.getPool(defaultAlias).getConnection();
    }

    /**
     * <p>getConnection.</p>
     *
     * @param alias a {@link java.lang.String} object.
     * @return a {@link java.sql.Connection} object.
     * @throws java.sql.SQLException if any.
     */
    public static synchronized Connection getConnection(String alias)
            throws SQLException {
        if (cpm == null) {
            getInstance();
        }
        ConnectionPool pool = cpm.getPool(alias);
        try {
            return pool.getConnection();
        } catch (SQLException e) {
            LOG.error("Can not get Connection from " + alias + " with " + pool.dumpInfo());
            throw e;
        }
    }

    /**
     * Returns the connection back to the pool.
     *
     * @param conn 待释放连接
     * @throws java.sql.SQLException 数据库出错
     */
    public static void freeConnection(Connection conn) throws SQLException {
        conn.close();
    }


    /**
     * <p>state.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String state() {
        StringBuilder sb = new StringBuilder();
        if (cpm != null) {
            try {
                for (String s : ALIAS_SET) {
                    ConnectionPool pool = cpm.getPool(s);
                    String lf = "<br>\n";
                    sb.append("Pool Statistics: ").append(s).append(lf);
                    sb.append("  Current size: ").append(pool.size()).append(lf);
                    sb.append("  MaxConn: ").append(pool.getMaxConn()).append(lf);
                    sb.append("  Connection requests: ").append(pool.getNumRequests()).append(lf);
                    sb.append("  Number of waits: ").append(pool.getNumWaits()).append(lf);
                    sb.append("  Number of timeouts:  ").append(pool.getNumCheckoutTimeouts()).append(lf);
                }
            } catch (Exception ex) {
                LOG.warn("", ex);
            }
        } else {
            sb.append("Alias==").append(Arrays.toString(ALIAS_SET.toArray(new String[0]))).append(" , cpm==").append(cpm);
        }
        return sb.toString();
    }

    /**
     * <p>reset.</p>
     */
    public synchronized static void reset() {
        if (cpm != null) {
            try {
                for (String alias : ALIAS_SET) {
                    cpm.removeAlias(alias);
                }
                ALIAS_SET.clear();
            } catch (SQLException e) {
                LOG.warn("", e);
            }
            cpm = null;
        }
    }

    /**
     * <p>addAlias.</p>
     *
     * @param cfg a {@link DatabaseConfig} object.
     * @throws ClassNotFoundException if any.
     * @throws InstantiationException if any.
     * @throws IllegalAccessException if any.
     * @throws SQLException if any.
     * @throws NoSuchMethodException if any.
     * @throws InvocationTargetException if any.
     */
    public static synchronized void addAlias(DatabaseConfig cfg)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException, NoSuchMethodException, InvocationTargetException {
        if (cpm == null) {
            cpm = new ConnectionPoolManager(cfg.getSleepIntervalInSeconds());
        }
        cpm.addAlias(cfg.getAlias(), cfg.getClassName(), cfg.getDburl(),
                cfg.getUsername(), cfg.getPassword(), cfg.getMaxActive(),
                (int) cfg.getMaxWaitTime(), (int) cfg.getMaxWaitTime(),
                /*cfg.getMaxIdle(),*/  //comment out 2016-02-16 by xwx
                cfg.getMaxCheckOutCount());

        addAliasString(cfg.getAlias());

    }

    /**
     * for test only
     *
     * @return an array of {@link java.lang.String} objects.
     */
    protected static String[] getAliases() {
        return ALIAS_SET.toArray(new String[0]);
    }

    /**
     * <p>addAliasString.</p>
     *
     * @param alias a {@link java.lang.String} object.
     */
    protected static void addAliasString(String alias) {
        boolean find = ALIAS_SET.contains(alias);
        if (!find) {
            ALIAS_SET.add(alias);
        }
    }
}
