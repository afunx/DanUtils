/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.log;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.dan.me.utils.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.dan.me.utils.log.LogUtils.LEVEL_D;
import static com.dan.me.utils.log.LogUtils.LEVEL_D_STR;
import static com.dan.me.utils.log.LogUtils.LEVEL_E;
import static com.dan.me.utils.log.LogUtils.LEVEL_E_STR;
import static com.dan.me.utils.log.LogUtils.LEVEL_I;
import static com.dan.me.utils.log.LogUtils.LEVEL_I_STR;
import static com.dan.me.utils.log.LogUtils.LEVEL_V;
import static com.dan.me.utils.log.LogUtils.LEVEL_V_STR;
import static com.dan.me.utils.log.LogUtils.LEVEL_W;
import static com.dan.me.utils.log.LogUtils.LEVEL_WTF;
import static com.dan.me.utils.log.LogUtils.LEVEL_WTF_STR;
import static com.dan.me.utils.log.LogUtils.LEVEL_W_STR;

abstract class BaseLogger implements Handler.Callback {

    protected static final boolean DEBUG = false;

    private static final String NEW_LINE = System.getProperty("line.separator");
    protected static final String LOG_FILE_EXTENSION = ".txt";
    private static final String KEY_TIME = "time";
    private static final String KEY_TIME_STR = "timeStr";
    private static final String KEY_TAG = "tag";

    private final String mTag;

    private final Handler mHandler;

    private final Date mDate = new Date();
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    protected long mMaxFileSize = 4 * 1024 * 1024;

    protected long mMaxTotalSize = 24 * 1024 * 1024;

    private String mPackageName = "";

    BaseLogger() {
        final HandlerThread loggerHandlerThread = new HandlerThread(getHandelThreadName());
        loggerHandlerThread.start();
        mTag = getTag();
        mHandler = new Handler(loggerHandlerThread.getLooper(), this);
    }

    void setPackageName(@NonNull String packageName) {
        mPackageName = packageName;
    }

    void setMaxFileSize(long maxFileSize) {
        mMaxFileSize = maxFileSize;
    }

    void setMaxTotalSize(long maxTotalSize) {
        mMaxTotalSize = maxTotalSize;
    }

    @NonNull
    protected abstract String getTag();

    @NonNull
    protected abstract String getHandelThreadName();

    protected abstract void setFolder(@NonNull String basePath);

    @Nullable
    protected abstract String getLogPath(long time);

    @Nullable
    protected abstract String getLogPath(long time, String tag);

    @NonNull
    protected abstract String getFolderPath();

    void log(int level, final String tag, final String msg) {
        // 多线程访问时，即使存在同步问题，也不会超过1ms的误差
        // 所以，不加任何同步机制，以免不必要的降低性能
        long time = System.currentTimeMillis();
        mDate.setTime(time);
        final String timeStr = mDateFormat.format(mDate);
        final int pid = android.os.Process.myPid();
        final int tid = android.os.Process.myTid();
        final Message message = mHandler.obtainMessage(level, pid, tid, msg);
        // time
        message.getData().putLong(KEY_TIME, time);
        message.getData().putString(KEY_TIME_STR, timeStr);
        // tag
        message.getData().putString(KEY_TAG, tag);
        mHandler.sendMessage(message);
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        final int level = msg.what;
        final int pid = msg.arg1;
        final int tid = msg.arg2;
        final long time = msg.getData().getLong(KEY_TIME);
        final String timeStr = msg.getData().getString(KEY_TIME_STR);
        final String tag = msg.getData().getString(KEY_TAG);
        final String message = (String) msg.obj;
        final String levelStr;
        switch (level) {
            case LEVEL_V:
                levelStr = LEVEL_V_STR;
                break;
            case LEVEL_D:
                levelStr = LEVEL_D_STR;
                break;
            case LEVEL_I:
                levelStr = LEVEL_I_STR;
                break;
            case LEVEL_W:
                levelStr = LEVEL_W_STR;
                break;
            case LEVEL_E:
                levelStr = LEVEL_E_STR;
                break;
            case LEVEL_WTF:
                levelStr = LEVEL_WTF_STR;
                break;
            default:
                throw new IllegalStateException();
        }
        String content = compose(levelStr, pid, tid, timeStr, tag, message);
        String logPath = getLogPath(time);
        if (logPath == null) {
            logPath = getLogPath(time, tag);
        }
        if (logPath == null) {
            throw new NullPointerException("logPath is null");
        }
        if (DEBUG) {
            Log.i(mTag, "logPath: " + logPath);
            Log.i(mTag, "content: " + content);
        }
        boolean suc = FileUtils.fileAppend(logPath, content);
        if (!suc) {
            Log.e(mTag, "handleMessage() fail to write logPath: " + logPath);
        }
        return true;
    }

    private String compose(final String levelStr, final int pid, final int tid, final String time, final String tag, final String msg) {
        return String.format(Locale.getDefault(),"%s %d-%d/%s %s/%s: %s%s",
                time, pid, tid, mPackageName, levelStr, tag, msg, NEW_LINE);
    }

    protected void deleteOvertimeLogs() {
        long start = SystemClock.elapsedRealtime();
        String folderPath = getFolderPath();
        long diskSpace = FileUtils.dirDiskSpace(folderPath);
        long overDiskSpace = diskSpace - mMaxTotalSize;
        int deleteFileCount = 0;
        long freeDiskSpace = 0;
        if (overDiskSpace > 0) {
            List<File> fileList = FileUtils.dirEarliestModifyFiles(folderPath, overDiskSpace);
            for (File file : fileList) {
                long fileLength = file.length();
                boolean suc = file.delete();
                if (suc) {
                    ++deleteFileCount;
                    freeDiskSpace += fileLength;
                } else {
                    Log.e(mTag, "deleteOvertimeLogs() file: " + file.getAbsolutePath() + " fail");
                }
            }
        }
        long consume = SystemClock.elapsedRealtime() - start;
        Log.e(mTag, "deleteOvertimeLogs() diskSpace: " + diskSpace + ", overDiskSpace: " + overDiskSpace
                + ", delete: " + deleteFileCount + " files, free " + freeDiskSpace  + " bytes, consume "
                + consume + " ms");
    }
}
