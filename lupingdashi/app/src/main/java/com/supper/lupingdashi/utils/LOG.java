package com.supper.lupingdashi.utils;

import android.util.Log;
import android.util.SparseArray;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author hxk
 * @version 1.0
 */
public class LOG {

    /**
     * false  no debug ; yes debug
     */
    private static final boolean ADB = true;


    private static final int LOG_DEGREE = Log.VERBOSE;

    public static final String LOGTAG = "lupingzhuanjia";
    private static String className;
    private static String methodName;
    private static int lineNumber;

    public static void v(String message, Object... args) {
        if (ADB || Log.isLoggable(LOGTAG, Log.WARN)) {
            Log.v(LOGTAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }


    public static void v(String... msg) {
        if (ADB && LOG_DEGREE <= Log.VERBOSE) {
            getMethodNames(new Throwable().getStackTrace());
            String msgStr = createLog(msg);

            Log.v(className, msgStr);

        }
    }

    public static void debug(String message, Object... args) {
        if (ADB || Log.isLoggable(LOGTAG, Log.WARN)) {
            Log.d(LOGTAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }


    public static void debug(String... msg) {
        if (ADB && LOG_DEGREE <= Log.DEBUG) {
            getMethodNames(new Throwable().getStackTrace());
            String msgStr = createLog(msg);

            Log.d(LOGTAG, msgStr);

        }
    }

    public static void info(String message, Object... args) {
        if (ADB || Log.isLoggable(LOGTAG, Log.WARN)) {
            Log.i(LOGTAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }


    public static void info(String... msg) {
        if (ADB && LOG_DEGREE <= Log.INFO) {
            getMethodNames(new Throwable().getStackTrace());
            String msgStr = createLog(msg);

            Log.i(LOGTAG, msgStr);

        }
    }

    public static void warn(String message, Object... args) {
        if (ADB || Log.isLoggable(LOGTAG, Log.WARN)) {
            Log.w(LOGTAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }

    public static void warn(String... msg) {
        if (ADB && LOG_DEGREE <= Log.WARN) {
            getMethodNames(new Throwable().getStackTrace());
            String msgStr = createLog(msg);

            Log.w(LOGTAG, msgStr);

        }
    }

    public static void error(String... msg) {
        if (ADB && LOG_DEGREE <= Log.ERROR) {
            getMethodNames(new Throwable().getStackTrace());
            String msgStr = createLog(msg);

            Log.e(LOGTAG, msgStr);

        }
    }

    public static void error(String message, Object... args) {
        if (ADB || Log.isLoggable(LOGTAG, Log.ERROR)) {
            Log.e(LOGTAG, args == null || args.length == 0 ? message
                    : String.format(message, args));
        }
    }


    public static void error(Throwable tr, String... msg) {
        if (ADB && LOG_DEGREE <= Log.ERROR) {
            getMethodNames(new Throwable().getStackTrace());
            String msgStr = createLog(msg);

            Log.e(LOGTAG, msgStr, tr);

        }
    }


    private static String combineLogMsg(String... msg) {
        if (null == msg)
            return null;

        StringBuilder sb = new StringBuilder();
        for (String s : msg) {
            sb.append(s);
        }
        return sb.toString();

    }

    private static String createLog(String... msg) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(methodName);
        buffer.append(":");
        buffer.append(lineNumber);
        buffer.append("]");
        buffer.append(combineLogMsg(msg));

        return buffer.toString();
    }

    private static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }


    private static Object lock = new Object();


    private static Queue<String> Logs = new ConcurrentLinkedQueue<String>();


    private static SparseArray<String> degreeLabel = new SparseArray<String>();


    static {
        degreeLabel.put(Log.VERBOSE, "V");
        degreeLabel.put(Log.DEBUG, "D");
        degreeLabel.put(Log.INFO, "I");
        degreeLabel.put(Log.WARN, "W");
        degreeLabel.put(Log.ERROR, "E");
    }


}