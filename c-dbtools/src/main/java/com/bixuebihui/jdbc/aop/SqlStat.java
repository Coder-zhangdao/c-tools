package com.bixuebihui.jdbc.aop;

/**
 * <p>SqlStat class.</p>
 *
 * @author xingwx
 * @version $Id: $Id
 */
public class SqlStat {
    private long count;
    private double totalTime;
    static final double SLOW_QUERY = 0.1;//0.1s

    /**
     * <p>Setter for the field <code>count</code>.</p>
     *
     * @param count a long.
     */
    public void setCount(long count) {
        this.count = count;
    }

    /**
     * <p>Getter for the field <code>count</code>.</p>
     *
     * @return a long.
     */
    public long getCount() {
        return count;
    }

    /**
     * <p>Getter for the field <code>totalTime</code>.</p>
     *
     * @return a double.
     */
    public double getTotalTime() {
        return totalTime;
    }

    /**
     * <p>Setter for the field <code>totalTime</code>.</p>
     *
     * @param totalTime a double.
     */
    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }
}
