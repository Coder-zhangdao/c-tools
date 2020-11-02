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

    private final static String insertSql = "INSERT INTO " + SESSIONS_TABLE
            + " (s_id, user_id, s_fp, s_expire) VALUES(?,?,?,?)";
    ;
    private final static String selectSql = "SELECT s_id, user_id, s_start, s_expire, s_fp FROM "
            + SESSIONS_TABLE + " WHERE s_id = ";
    private final static String updateSql = "UPDATE " + SESSIONS_TABLE
            + " SET s_expire = ? WHERE s_id = ?";
    private final static String deleteSql = "DELETE FROM " + SESSIONS_TABLE
            + " WHERE s_id = ? or s_expire < ?";
    private final static String gcSql = "DELETE FROM " + SESSIONS_TABLE
            + " WHERE s_expire < ?";

    private final static String countSql = "select count(*) from " + SESSIONS_TABLE
            + " WHERE s_expire < ?";

    private final static String install = "CREATE TABLE IF NOT EXISTS t_session ( s_id varchar2(64) NOT " +
            "NULL, user_id number(10) NOT NULL, s_start date default sysdate NOT NULL ," +
            " s_expire number(15) NOT NULL , s_fp varchar2(64) NOT NULL, PRIMARY KEY" +
            " (s_id), unique (user_id) ) ";

    public DbSessionManager(IDbHelper dbhelper) throws SQLException {
        dbHelper = dbhelper;
        dbHelper.executeNoQuery(install);
    }

    public DbSessionManager() {
        dbHelper = (IDbHelper) BeanFactory.createObjectById("sessionDbHelper");
    }

    public boolean destroy(String s_id) {
        try {
            return 1 == dbHelper.executeNoQuery(deleteSql, new Object[]{s_id,
                    (new Date()).getTime()});
        } catch (SQLException e) {
            mLog.warn(e);
        }
        return false;
    }

    public int gc(long time) {
        try {
            return dbHelper.executeNoQuery(gcSql,
                    new Object[]{time});
        } catch (SQLException e) {
            mLog.warn(e);
        }
        return 0;
    }

    public SimpleSession read(String s_id) {
        try {
            final SimpleSession ss = new SimpleSession();

            String sql = selectSql + "? and s_expire>= ?";


            dbHelper.executeQuery(sql, new Object[]{s_id,
                            (new Date()).getTime()},
                    new RowCallbackHandler() {
                        public void processRow(ResultSet rs)
                                throws SQLException {
                            ss.setS_id(rs.getString(1));
                            ss.setUser_id(rs.getInt(2));
                            ss.setS_start(rs.getDate(3));
                            ss.setS_expire(rs.getLong(4));
                            ss.setS_fp(rs.getString(5));
                        }
                    });

            return ss;
        } catch (SQLException ex) {
            mLog.warn(ex);
        }

        return null;

    }

    public boolean insert(SimpleSession ss) throws CMyException {
        try {
            insert_counter++;
            if (isAutoGC() && insert_counter > SimpleSession.GC_LIMIT) {
                gc((new Date()).getTime());
                insert_counter = 0;
            }

            return 1 == dbHelper.executeNoQuery(insertSql, new Object[]{
                    ss.getS_id(),
                    ss.getUser_id(),
                    ss.getS_fp(),
                    (new Date()).getTime()
                            + SimpleSession.SESSION_LIFE});
        } catch (SQLException e) {
            throw new CMyException("更新数据库session出错" + e.getMessage(), e);
        }
    }

    public boolean update(String s_id) throws CMyException {
        try {
            return 1 == dbHelper.executeNoQuery(updateSql, new Object[]{
                    (new Date()).getTime()
                            + SimpleSession.SESSION_LIFE, s_id});
        } catch (SQLException e) {
            throw new CMyException("更新数据库session出错" + e.getMessage(), e);
        }
    }

    public int getCount() throws CMyException {
        try {
            return dbHelper.executeNoQuery(countSql, new Object[]{(new Date()).getTime()});
        } catch (SQLException e) {
            throw new CMyException("查询数据库session出错" + e.getMessage(), e);
        }
    }

}
