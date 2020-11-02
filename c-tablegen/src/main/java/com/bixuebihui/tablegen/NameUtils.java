package com.bixuebihui.tablegen;

public class NameUtils {
	public static boolean isYes(String str){
		return "Y".equals(str)
		|| "YES".equals(str);
	}

    /**
     * Upper cases the first character in a String. Includes a compatibility
     * flag for earlier users. This will be removed at some stage.
     * @param keepCase TODO
     */
	public static String firstUp(String p, boolean keepCase) {
        return keepCase ? (p.substring(0, 1).toUpperCase() + p.substring(1, p.length())) :
                (p.substring(0, 1).toUpperCase() + p.substring(1, p.length()).toLowerCase());
    }

	public static String firstLow(String p, boolean keepCase) {
		return keepCase ? (p.substring(0, 1).toLowerCase() + p.substring(1, p.length())) :
		 (p.substring(0, 1).toLowerCase() + p.substring(1, p.length()).toLowerCase());
    }
}
