package com.bixuebihui.cache;

public class CacheConfig {

	int capacity = 10000;//max number of items to cache



	/** 更新时间间隔,小时 */
	static private int s_iUpdateInterval = 12;

	/** 更新时间间隔,毫秒 */
	static private long s_lUpdateInterval = s_iUpdateInterval * 60 * 60 * 1000;

	static public int getUpdateIntervalHours() {
		return s_iUpdateInterval;
	}

	static public long getUpdateIntervalMilliseconds() {
		return s_lUpdateInterval;
	}

	static public void setUpdateIntervalHours(int iUpdateInterval) {
		s_iUpdateInterval = iUpdateInterval;
		s_lUpdateInterval = s_iUpdateInterval * 60 * 60 * 1000;
	}
}
