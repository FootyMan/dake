package com.unicom.utils;

public class DBContextHolder {
	public static final String SITE_WRITE = "siteWrite";
	public static final String SITE_READ = "siteRead";
	private static final ThreadLocal<String> contextHolder = new ThreadLocal();

	public static void setDBType(String dbType) {
		contextHolder.set(dbType);
	}

	public static String getDBType() {
		return (String) contextHolder.get();
	}

	public static void clearDBType() {
		contextHolder.remove();
	}
}
