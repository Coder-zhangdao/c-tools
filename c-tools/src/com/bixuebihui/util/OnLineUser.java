package com.bixuebihui.util;

/**
 * 这是管理user信息的类
 * 文件名为onLineUser.java
 */

import com.bixuebihui.task.ScheduleMonitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.Date;
import java.util.Vector;

public class OnLineUser
        implements HttpSessionBindingListener, HttpSessionAttributeListener, ServletContextListener {

    Log __log = LogFactory.getLog(OnLineUser.class);
    ScheduleMonitor cm = null;

    public OnLineUser() {
    }

    private static Vector users = new Vector();

    public int getCount() {
        users.trimToSize();
        return users.capacity();
    }


    public boolean existUser(String userName) {
        users.trimToSize();
        boolean existUser = false;
        for (int i = 0; i < users.capacity(); i++) {
            if (userName.equals((String) users.get(i))) {
                existUser = true;
                break;
            }
        }
        return existUser;
    }

    public boolean deleteUser(String userName) {
        users.trimToSize();
        if (existUser(userName)) {
            int currUserIndex = -1;
            for (int i = 0; i < users.capacity(); i++) {
                if (userName.equals((String) users.get(i))) {
                    currUserIndex = i;
                    break;
                }
            }
            if (currUserIndex != -1) {
                users.remove(currUserIndex);
                users.trimToSize();
                return true;
            }
        }
        return false;
    }

    public Vector getOnLineUser() {
        return users;
    }

    public void valueBound(HttpSessionBindingEvent e) {
        addUser(e.getName());
    }


    private void addUser(String name) {
        users.trimToSize();
        if (!existUser(name)) {
            users.add(name);
            __log.debug("users object=" + users);
            __log.info(name + "\t 登入到系统\t" + (new Date()) + " 在线用户数为：" + getCount());
        } else {
            __log.info(name + "已经存在");
        }
    }

    public void valueUnbound(HttpSessionBindingEvent e) {
        users.trimToSize();
        String userName = e.getName();
        deleteUser(userName);
        __log.debug("users object=" + users);
        __log.info("会话结束：" + userName + "\t 退出系统\t" + (new Date()) + " 在线用户数为：" + getCount());
    }

    public void attributeAdded(HttpSessionBindingEvent arg0) {

        //System.out.println("Add Source:"+arg0.getSource());
        //System.out.println("Name:"+arg0.getName());
        //System.out.println("Value:"+arg0.getValue());
        if ("user_id".equals(arg0.getName())) {
            addUser(arg0.getValue().toString());
        }
    }

    public void attributeRemoved(HttpSessionBindingEvent arg0) {

        //System.out.println("Remove Source:"+arg0.getSource());
        //System.out.println("Name:"+arg0.getName());
        //System.out.println("Value:"+arg0.getValue());
        if ("user_id".equals(arg0.getName())) {
            deleteUser(arg0.getValue().toString());
            __log.info("attributeRemoved: " + arg0.getValue().toString() + "\t 退出系统\t" + (new Date()));
            __log.info(" 在线用户数为：" + getCount() + ",用户id=" + arg0.getValue().toString() + "退出");
        }

    }

    public void attributeReplaced(HttpSessionBindingEvent arg0) {

        if ("user_id".equals(arg0.getName())) {
            deleteUser(arg0.getValue().toString());
        }

    }


    public void contextDestroyed(ServletContextEvent event) {
        try {
            ScheduleMonitor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        event.getServletContext().log("Destory AppStartListener!");

        __log.info("[CYC]系统关闭");
    }


    public void contextInitialized(ServletContextEvent event) {
        __log.info("[CYC]系统启动...");
        __log.info(event.getServletContext().getRealPath("/"));
        //System.out.println("[CYC] Application Listener has been actived!");
        cm = new ScheduleMonitor();
        try {
            __log.info("[CYC] 自动监控任务启动:" + cm.run());
        } catch (Exception e) {
            __log.error(e.getMessage());
        }
        event.getServletContext().log("AppStartListener Launch!");

    }

}
