package com.bixuebihui.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class ScheduleMonitor {
    private static boolean scheduleon = false;
    private static Scheduler sched = null;                    //唯一的时间表对象
    private static long log_interval = 600000L;                //每1分钟写一次主页
    private static long tou_interval = 300000L;                //每5分钟查一次超时用户

    private static Log mLog = LogFactory.getLog(ScheduleMonitor.class);

    /**
     * 重启监控程序
     *
     * @return 成功重启返回TRUE
     * @throws Exception
     */
    public static boolean restart() throws Exception {
        if (sched != null) {
            mLog.info("Restart Quartz schedule job...");
            if (sched.isStarted()) {
                sched.shutdown();
                mLog.info("Schedule is running..->..Shutdown current schedule job!");
            }
            sched = StdSchedulerFactory.getDefaultScheduler();
            //JobDetail job = new JobDetail("WriteStaticPage","WPAGE", PagePublisher.class);
            //SimpleTrigger trigger = new SimpleTrigger("Trigger","WPAGE",SimpleTrigger.REPEAT_INDEFINITELY,log_interval);

            //sched.scheduleJob(job, trigger);

            sched.start();
            scheduleon = sched.isStarted();
            mLog.info("Restart schedule[" + scheduleon + "] finished.");
            return scheduleon;
        }

        return false;
    }

    /**
     * 关闭监控
     *
     * @return 成功关闭返回TRUE
     * @throws Exception
     */
    public static boolean shutdown() throws SchedulerException {
        if (sched != null) {
            mLog.info("Shutdown Quartz schedule job...");
            if (sched.isStarted()) {
                sched.shutdown();
                mLog.info("Schedule is running..->..Shutdown current schedule job!");
            }
            if (sched.isShutdown()) {
                scheduleon = false;
            } else {
                scheduleon = true;
            }
            mLog.info("Shutdown schedule finished.[" + !scheduleon + "]");
            return !scheduleon;
        }

        return true;
    }


    /**
     * 首次启动运行日至和超时用户检查
     *
     * @return 如正常启动返回TRUE
     * @throws Exception
     */
    public static boolean run() throws Exception {
        try {

            mLog.info("Quartz tasks start working(first)...");
            sched = StdSchedulerFactory.getDefaultScheduler();

            sched.start();

            scheduleon = sched.isStarted();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scheduleon;
    }

    public static boolean isScheduleon() {
        return scheduleon;
    }

    public static void setScheduleon(boolean scheduleon) {
        ScheduleMonitor.scheduleon = scheduleon;
    }

    public static long getLog_interval() {
        return log_interval;
    }

    public static void setLog_interval(long log_interval) {
        ScheduleMonitor.log_interval = log_interval;
    }

    public static long getTou_interval() {
        return tou_interval;
    }

    public static void setTou_interval(long tou_interval) {
        ScheduleMonitor.tou_interval = tou_interval;
    }

}
