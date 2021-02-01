package com.bixuebihui.sql;

import org.hamcrest.core.Is;
import org.hamcrest.core.StringContains;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

import static org.junit.Assert.*;

public class ConnectionPoolManagerTest {

    String driverName="org.h2.Driver";
    String url="jdbc:h2:mem:test";
    String username="sa";
    String password="sa";


    @Test
    public void addAlias() throws Exception {
        ConnectionPoolManager pm = new ConnectionPoolManager();
        pm.addAlias("h2", driverName,url,username,password,
                100,1000,2000);
        pm = new ConnectionPoolManager();
        pm.addAlias("h20",driverName,url,username,password,
                100,1000,2000);
        assertTrue( pm.getPool("h20")!=null);
        assertTrue(pm.getPool("h20")!=null);
        pm.destroy();
    }

    @Test(expected = SQLException.class)
    public void removeAlias() throws Exception {
        ConnectionPoolManager pm = new ConnectionPoolManager();
        pm.addAlias("h2", driverName,url,username,password,
                100,1000,2000);
        pm.removeAlias("h2");
        pm.getPool("h2");
    }

    @Test
    public void getPools() throws Exception {
        ConnectionPoolManager pm = new ConnectionPoolManager();
        pm.addAlias("h2", driverName,url,username,password,
                100,1000,2000);
        Collection<ConnectionPool> pools = pm.getPools();

        assertTrue(!pools.isEmpty());
        pm.destroy();

    }

    @Test
    public void getPool() throws Exception {
        ConnectionPoolManager pm = new ConnectionPoolManager();
        pm.addAlias("h2", driverName,url,username,password,
                100,1000,2000);
        ConnectionPool pool = pm.getPool("h2");
        assertTrue(pool!=null);
        pm.destroy();
    }

    @Test
    public void run() throws Exception {
        DriverManager.registerDriver((Driver) Class.forName(driverName).getDeclaredConstructor().newInstance());
        ConnectionPoolManager pm = new ConnectionPoolManager();
        pm.setTracing(true);
        pm.addAlias("h2", driverName,url,username,password,
                100,1000,2000);
        new Thread(pm).start();
        Thread.sleep(200);
        Connection cn = pm.getPool("h2").getConnection();
        assertTrue(cn.isValid(100));
        pm.destroy();
    }

    @Test
    public void getMajorVersion() throws Exception {
        ConnectionPoolManager pm = new ConnectionPoolManager();
        org.hamcrest.MatcherAssert.assertThat(pm.getMajorVersion(), Is.isA(Integer.class));



    }

    @Test
    public void getMinorVersion() throws Exception {
        ConnectionPoolManager pm = new ConnectionPoolManager();
        org.hamcrest.MatcherAssert.assertThat(pm.getMinorVersion(), Is.isA(Integer.class));
    }

    @Test
    public void jdbcCompliant() throws Exception {
        ConnectionPoolManager pm = new ConnectionPoolManager();
        assertFalse(pm.jdbcCompliant());
    }

    @Test
    public void dumpInfo() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ConnectionPoolManager pm = new ConnectionPoolManager();
        pm.addAlias("h2", driverName,url,username,password,
                100,1000,2000);
        pm.getPool("h2").getConnection();
        String str = pm.dumpInfo();
//        System.out.println(str);
        org.hamcrest.MatcherAssert.assertThat(str, StringContains.containsString("h2"));

    }

    @Test
    public void tracing() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        ConnectionPoolManager pm = new ConnectionPoolManager();
        pm.addAlias("h2", driverName, url, username, password,
                100, 1000, 2000);
        pm.setTracing(true);
        assertTrue(pm.isTracing());

        pm.destroy();
    }

    @Test
    public void destroy() throws Exception {
        ConnectionPoolManager pm = new ConnectionPoolManager();
        pm.destroy();
    }

}
