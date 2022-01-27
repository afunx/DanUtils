/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.log;

import android.os.Process;
import android.util.Log;

import com.dan.me.utils.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * CrashLogger采用同步记录方式，故不再继承BaseLogger
 *
 * AndroidTest无法测试应用崩溃。但已在应用中自测可用。
 */
class CrashLogger {

    private static final boolean DEBUG = false;

    private static final String TAG = "CrashLogger";

    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String LOG_FILE_EXTENSION = ".txt";

    static final String CRASH_SUB_PATH = "crash" + File.separator;
    private static final String LOG_FILE_PREFIX = "crash_";

    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    private String mFolder;

    private String mPackageName = "";

    void setPackageName(@NonNull String packageName) {
        mPackageName = packageName;
    }

    void setFolder(@NonNull String basePath) {
        mFolder = basePath.endsWith(File.separator) ? basePath + CRASH_SUB_PATH : basePath + File.separator + CRASH_SUB_PATH;
        if (DEBUG) {
            Log.i(TAG, "setFolder() basePath: " + basePath + ", folder: " + mFolder);
        }
    }

    private String getLogPath(long time) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String logName = LOG_FILE_PREFIX + fmt.format(new Date(time)) + LOG_FILE_EXTENSION;
        String logPath = mFolder + logName;
        if (DEBUG) {
            Log.i(TAG, "getLogPath() logPath: " + logPath);
        }
        // crash_20220127_205010.txt
        return logPath;
    }

    private String compose(final int pid, final int tid, final String time, final String msg) {
        return String.format(Locale.getDefault(), "%s %d-%d/%s %s/%s: %s%s",
                time, pid, tid, mPackageName, "E" /*levelStr*/, TAG, msg, NEW_LINE);
    }

    private void logCrash(@NonNull Throwable e) {
        if (DEBUG) {
            Log.e(TAG, "logCrash()");
        }
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(bos);
        e.printStackTrace(ps);
        ps.close();
        final long time = System.currentTimeMillis();
        final String msg = bos.toString();
        final int pid = android.os.Process.myPid();
        final int tid = android.os.Process.myTid();
        final Date date = new Date(time);
        final String timeStr = mDateFormat.format(date);
        String content = compose(pid, tid, timeStr, msg);
        if (DEBUG) {
            Log.e(TAG, content);
        }
        String logPath = getLogPath(System.currentTimeMillis());
        FileUtils.fileWrite(logPath, content);
    }

    static class CrashHandler implements Thread.UncaughtExceptionHandler {

        private final Thread.UncaughtExceptionHandler defaultCrashHandler;

        private final CrashLogger crashLogger;

        CrashHandler(CrashLogger crashLogger) {
            this.crashLogger = crashLogger;
            this.defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
            if (DEBUG) {
                Log.i(TAG, "CrashHandler() defaultCrashHandler: " + defaultCrashHandler);
            }
        }

        @Override
        public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
            if (DEBUG) {
                Log.e(TAG, "uncaughtException() e: " + e.getLocalizedMessage());
            }
            // 写日志
            this.crashLogger.logCrash(e);
            // 如果系统提供了默认的CrashHandler，则使用之
            if (defaultCrashHandler != null) {
                defaultCrashHandler.uncaughtException(t, e);
            } else {
                Process.killProcess(Process.myPid());
            }
        }
    }
}
