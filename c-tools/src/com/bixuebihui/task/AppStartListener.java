package com.bixuebihui.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;


public class AppStartListener extends HttpServlet implements ServletContextListener {
    static ScheduleMonitor cm = null;
    private final static Log log = LogFactory.getLog(AppStartListener.class);
    /**
     *
     */
    private static final long serialVersionUID = 3105513079977240099L;

    public void contextInitialized(ServletContextEvent event) {
        //System.out.println("[CYC] Application Listener has been actived!");
        cm = new ScheduleMonitor();
        try {
            log.info("[CYC] 自动监控任务启动:" + cm.run());
        } catch (Exception e) {
            log.warn(e);
        }
        event.getServletContext().log("AppStartListener Launch!");
        //event.getServletContext().log("已经添加任务调度表");
    }

    public void contextDestroyed(ServletContextEvent event) {
        try {
            ScheduleMonitor.shutdown();
        } catch (Exception e) {
            log.warn(e);
        }
        event.getServletContext().log("Destory AppStartListener!");
        log.info("[CYC] Application Listener has been destoryed!");
    }


}
