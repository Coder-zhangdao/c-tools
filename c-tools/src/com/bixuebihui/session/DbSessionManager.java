package com.bixuebihui.session;

import com.bixuebihui.BeanFactory;
import com.bixuebihui.jdbc.IDbHelper;
import com.bixuebihui.jdbc.RowCallbackHandler;
import com.bixuebihui.util.other.CMyException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class DbSessionManager extends BaseSessionManager {
    /*
     *
     * drop table t_session; CREATE TABLE t_session ( s_id varchar2(64) NOT
     * NULL, user_id number(10) NOT NULL, s_start date default sysdate NOT NULL ,
     * s_expire number(11) NOT NULL , s_fp varchar2(64) NOT NULL, PRIMARY KEY
     * (s_id), unique (user_id) ) ;
     */
    public static String SESSIONS_TABLE = "t_session";
    private IDbHelper dbHelper;
    private int insert_counter = 0;

    private final static String INSERT_SQL = "INSERT INTO " + SESSIONS_TABLE
            + " (s_id, user_id, s_fp, s_expire) VALUES(?,?,?,?)";
    ;
    private final static String SELECT_SQL = "SELECT s_id, user_id, s_start, s_expire, s_fp FROM "
            + SESSIONS_TABLE + " WHERE s_id = ";
    private final static String UPDATE_SQL = "UPDATE " + SESSIONS_TABLE
            + " SET s_expire = ? WHERE s_id = ?";
    private final static String DELETE_SQL = "DELETE FROM " + SESSIONS_TABLE
            + " WHERE s_id = ? or s_expire < ?";
    private final static String GC_SQL = "DELETE FROM " + SESSIONS_TABLE
            + " WHERE s_expire < ?";

    private final static String COUNT_SQL = "select count(*) from " + SESSIONS_TABLE
            + " WHERE s_expire < ?";

    private final static String INSTALL = "CREATE TABLE IF NOT EXISTS t_session ( s_id varchar2(64) NOT " +
            "NULL, user_id number(10) NOT NULL, s_start date default sysdate NOT NULL ," +
            " s_expire number(15) NOT NULL , s_fp varchar2(64) NOT NULL, PRIMARY KEY" +
            " (s_id), unique (user_id) ) ";

    public DbSessionManager(IDbHelper helper) throws SQLException {
        dbHelper = helper;
        dbHelper.executeNoQuery(INSTALL);
    }

    public DbSessionManager() {
        dbHelper = (IDbHelper) BeanFactory.createObjectById("sessionDbHelper");
    }

    @Override
    public boolean destroy(String id) {
        try {
            return 1 == dbHelper.executeNoQuery(DELETE_SQL, new Object[]{id,
                    (new Date()).getTime()});
        } catch (SQLException e) {
            LOG.warn("",e);
        }
        return false;
    }

    @Override
    public int gc(long time) {
        try {
            return dbHelper.executeNoQuery(GC_SQL,
                    new Object[]{time});
        } catch (SQLException e) {
            LOG.warn("", e);
        }
        return 0;
    }

    @Override
    public SimpleSession read(String id) {
        try {
            final SimpleSession ss = new SimpleSession();

            String sql = SELECT_SQL + "? and s_expire>= ?";


            dbHelper.executeQuery(sql, new Object[]{id,
                            (new Date()).getTime()},
                    rs -> {
                        ss.setS_id(rs.getString(1));
                        ss.setUser_id(rs.getInt(2));
                        ss.setS_start(rs.getDate(3));
                        ss.setS_expire(rs.getLong(4));
                        ss.setS_fp(rs.getString(5));
                    });

            return ss;
        } catch (SQLException ex) {
            LOG.warn("", ex);
        }

        return null;

    }

    @Override
    public boolean insert(SimpleSession ss) throws CMyException {
        try {
            insert_counter++;
            if (isAutoGC() && insert_counter > SimpleSession.GC_LIMIT) {
                gc((new Date()).getTime());
                insert_counter = 0;
            }

            return 1 == dbHelper.executeNoQuery(INSERT_SQL, new Object[]{
                    ss.getS_id(),
                    ss.getUser_id(),
                    ss.getS_fp(),
                    (new Date()).getTime()
                            + SimpleSession.SESSION_LIFE});
        } catch (SQLException e) {
            throw new CMyException("更新数据库session出错" + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(String s_id) throws CMyException {
        try {
            return 1 == dbHelper.executeNoQuery(UPDATE_SQL, new Object[]{
                    (new Date()).getTime()
                            + SimpleSession.SESSION_LIFE, s_id});
        } catch (SQLException e) {
            throw new CMyException("更新数据库session出错" + e.getMessage(), e);
        }
    }

    @Override
    public int getCount() throws CMyException {
        try {
            return dbHelper.executeNoQuery(COUNT_SQL, new Object[]{(new Date()).getTime()});
        } catch (SQLException e) {
            throw new CMyException("查询数据库session出错" + e.getMessage(), e);
        }
    }

}
