package com.bixuebihui.task;

import com.bixuebihui.util.Config;
import com.bixuebihui.util.html.publish.MyFacesResourceFilter;
import com.bixuebihui.util.html.publish.Url2html;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.Date;

public class PagePublisher implements Job {
    private Log mLog = LogFactory.getLog(PagePublisher.class);
    private long LastWriteDate = 0;
    private String mainpageUrl;
    private String mainpagePath;
    private String syscode = null;//

    public long getLastexecuteDate() {
        return LastWriteDate;
    }

    public void execute(JobExecutionContext jxContext)
            throws JobExecutionException {
        try {
            if (syscode == null) {
                syscode = Config.getProperty("system.code");
                mainpageUrl = Config.getProperty("url");
                mainpageUrl += (mainpageUrl.indexOf("?") >= 0 ? "&" : "?") + "syscode=" + syscode;
                mainpagePath = Config.getProperty("path") + "/cache/shebeitree_cardlist.htm";
            }
            //LastWriteDate = LogManager.writeChatMessage();

            Url2html uh = new Url2html();
            uh.addFilter(new MyFacesResourceFilter());
            boolean res = uh.writeFile(mainpageUrl, mainpagePath, "{D8CFA102-62D2-4ab9-BA17-63CA41D2AE7C}");

            if (res) {
                LastWriteDate = (new Date()).getTime();
                mLog.debug("[CYC] Quartz task write " + mainpageUrl + ".... to " + mainpagePath + (new Date()));
            }
            //throw new JobExecutionContext(exc.getMessage());

        } catch (IOException e) {
            mLog.warn(e);
        }
    }
}
