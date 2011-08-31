package com.windowsazure.samples.internal.util;

//import android.util.Log;


public final class Logger {
	
	public static void debug(String tag, String message) {
		//Log.d(tag, message);
	}
	
	public static void error(String tag, String message) {
		//Log.e(tag, message);
	}
	
	public static void info(String tag, String message) {
		//Log.i(tag, message);
	}
	
	public static void verbose(String tag, String message) {
		//Log.v(tag, message);
	}
	
	public static void warn(String tag, String message) {
		//Log.w(tag, message);
	}
	
	private Logger() {}
}
