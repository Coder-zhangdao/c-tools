package com.bixuebihui.session;

import com.bixuebihui.util.other.CMyException;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

public class MemoSessionManager extends BaseSessionManager {

    private Hashtable memoStore;
    private int insert_counter = 0;

    public static String SESSIONS_TABLE = "t_session";

    public MemoSessionManager(Hashtable store) {
        memoStore = store;
    }

    public MemoSessionManager() {
        memoStore = new Hashtable(1000);
    }

    public boolean destroy(String s_id) {
        memoStore.remove(s_id);
        return true;
    }

    public int gc(long time) {

        Enumeration en = memoStore.elements();
        int i = 0;
        while (en.hasMoreElements()) {
            SimpleSession ss = (SimpleSession) en.nextElement();
            if (ss.getS_expire() < (new Date()).getTime()
                    + SimpleSession.SESSION_LIFE) {
                memoStore.remove(ss.getS_id());
                i++;
            }
        }

        return i;

    }

    public void close() {
        memoStore.clear();
    }

    public SimpleSession read(String s_id) {
        //synchronized (memoStore) {
        // if(!memoStore.containsKey(s_id)) return null;
        return (SimpleSession) memoStore.get(s_id);
        //}
    }

    public boolean insert(SimpleSession ss) {
        //synchronized (memoStore) {
        insert_counter++;
        if (isAutoGC() && insert_counter > SimpleSession.GC_LIMIT) {
            gc((new Date()).getTime());
            insert_counter = 0;
        }

        if (memoStore.containsKey(ss.getS_id()))
            return false;
        memoStore.put(ss.getS_id(), ss);
        return true;
        //}
    }

    public boolean update(String s_id) {
        //synchronized (memoStore) {
        if (!memoStore.containsKey(s_id))
            return false;
        ((SimpleSession) memoStore.get(s_id)).setS_expire((new Date())
                .getTime()
                + SimpleSession.SESSION_LIFE);
        return true;
        //}
    }

    public int getCount() throws CMyException {
        return memoStore.size();
    }

}
