package com.rnv.media.urbanairship;


import android.util.Log;

public class MCLog {

	private static final int LOG_LEVEL = Log.VERBOSE;
	
	@SuppressWarnings("unused")
	public static void d(String tag, String msg) {
		
		if(LOG_LEVEL <= Log.DEBUG) {
			Log.d(tag, msg);
		}
	}
	
	@SuppressWarnings("unused")
	public static void i(String tag, String msg) {
		
		if(LOG_LEVEL <= Log.INFO) {
			Log.i(tag, msg);
		}
	}
	
	@SuppressWarnings("unused")
	public static void w(String tag, String msg) {
		
		if(LOG_LEVEL <= Log.WARN) {
			Log.w(tag, msg);
		}
	}
	
	@SuppressWarnings("unused")
	public static void e(String tag, String msg) {
		
		if(LOG_LEVEL <= Log.ERROR) {
			Log.e(tag, msg);
		}
	}
	
	@SuppressWarnings("unused")
	public static void d(String tag, String msg, Throwable e) {
		
		if(LOG_LEVEL <= Log.DEBUG) {
			Log.e(tag, msg, e);
		}
	}
	
	@SuppressWarnings("unused")
	public static void i(String tag, String msg, Throwable e) {
		
		if(LOG_LEVEL <= Log.INFO) {
			Log.e(tag, msg, e);
		}
	}
	
	@SuppressWarnings("unused")
	public static void w(String tag, String msg, Throwable e) {
		
		if(LOG_LEVEL <= Log.WARN) {
			Log.e(tag, msg, e);
		}
	}
	
	@SuppressWarnings("unused")
	public static void e(String tag, String msg, Throwable e) {
		
		if(LOG_LEVEL <= Log.ERROR) {
			Log.e(tag, msg, e);
		}
	}
}