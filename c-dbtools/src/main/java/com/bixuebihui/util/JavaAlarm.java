// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-1-4 11:29:44
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   JavaAlarm.java

package com.bixuebihui.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>JavaAlarm class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public final class JavaAlarm extends Thread
{
    private static final Log log = LogFactory.getLog(JavaAlarm.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        completed = false;
        r.run();
        synchronized(this) {
            completed = true;
            notifyAll();
        }
    }

    /**
     * <p>Constructor for JavaAlarm.</p>
     *
     * @param runnable a {@link java.lang.Runnable} object.
     * @param maxWait a long.
     * @throws TimeoutException if any.
     */
    public JavaAlarm(Runnable runnable, long maxWait)
            throws TimeoutException{
        r = runnable;
        completed = false;

        long step =maxWait/10+1;
        long count = step;

        synchronized(this) {
            start();
            while (!completed) {
                try {
                    wait(step);
                    if(!completed && count<maxWait) {
                        count +=step;
                        continue;
                    }
                    if(!completed) {
                        throw new TimeoutException("Runnable " + r + " did not complete within " + maxWait + "ms");
                    }
                } catch (InterruptedException e) {
                    log.warn(e);
                    this.interrupt();
                }

            }
        }

    }

    Runnable r;
    volatile boolean completed;
}
