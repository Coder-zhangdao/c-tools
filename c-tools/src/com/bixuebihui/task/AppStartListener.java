package com.bixuebihui.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;


/**
 * @author xwx
 */
public class AppStartListener extends HttpServlet implements ServletContextListener {
    private final static Log log = LogFactory.getLog(AppStartListener.class);
    /**
     *
     */
    private static final long serialVersionUID = 3105513079977240099L;

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            log.info("[CYC] 自动监控任务启动:" + ScheduleMonitor.run());
        } catch (Exception e) {
            log.warn(e);
        }
        event.getServletContext().log("AppStartListener Launch!");
    }

    @Override
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
