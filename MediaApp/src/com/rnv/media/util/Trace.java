package com.rnv.media.util;

import android.util.Log;

/**
 * This class is responsible for printing the debug logs in DDMS
 * 
 *
 *
 */ 
public class Trace {
    public static final int    NONE                         = 0;
    public static final int    ERRORS_ONLY                  = 1;
    public static final int    ERRORS_WARNINGS              = 2;
    public static final int    ERRORS_WARNINGS_INFO         = 3;
    public static final int    ERRORS_WARNINGS_INFO_DEBUG   = 4;

    public static final int   LOGGING_LEVEL   = ERRORS_WARNINGS_INFO_DEBUG;        // Errors + warnings + info + debug (default)

    public static void e(String tag, String msg) {
        if (LOGGING_LEVEL >=1) 
        	Log.e(tag,msg);
    }

    public static void e(String tag, String msg, Exception e) {
        if (LOGGING_LEVEL >=1) 
        	Log.e(tag,msg,e);
    }

    public static void w(String tag, String msg) {
        if (LOGGING_LEVEL >=2) 
        	Log.w(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (LOGGING_LEVEL >=3) 
        	Log.i(tag,msg);
    }

    public static void d(String tag, String msg) {
        if (LOGGING_LEVEL >=4) 
        	Log.d(tag, msg);
    }
}