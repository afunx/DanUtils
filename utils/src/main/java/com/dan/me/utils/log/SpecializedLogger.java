/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.log;

import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class SpecializedLogger extends BaseLogger {

    private static final String TAG = "SpecializedLogger";

    static final String SPECIALIZED_SUB_PATH = "specialized" + File.separator;
    private static final String LOG_FILE_PREFIX = "log_";

    private String mFolder;
    // tag -> logPath
    private final Map<String, String> mLogPathMap = new HashMap<>();

    @NonNull
    @Override
    protected String getTag() {
        return TAG;
    }

    @NonNull
    @Override
    protected String getHandelThreadName() {
        return "SpecializedLoggerHandlerThread";
    }

    @Override
    protected void setFolder(@NonNull String basePath) {
        mFolder = basePath.endsWith(File.separator) ? basePath + SPECIALIZED_SUB_PATH : basePath + File.separator + SPECIALIZED_SUB_PATH;
        if (DEBUG) {
            Log.i(TAG, "setFolder() basePath: " + basePath + ", folder: " + mFolder);
        }
    }

    @Nullable
    @Override
    protected String getLogPath(long time) {
        return null;
    }

    @Nullable
    @Override
    protected String getLogPath(long time, String tag) {
        String logPath = mLogPathMap.get(tag);
        if (DEBUG) {
            if (logPath != null) {
                Log.i(TAG, "getLogPath() logPath: " + logPath + ", file.length: " + new File(logPath).length());
            }
        }
        if (logPath == null || new File(logPath).length() >= mMaxFileSize) {

            // 仅在写新的日志文件时，才去删除过期日志文件
            deleteOvertimeLogs();

            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String logName = LOG_FILE_PREFIX + fmt.format(new Date(time)) + LOG_FILE_EXTENSION;
            logPath = mFolder + tag + File.separator + logName;
            mLogPathMap.put(tag, logPath);
        }
        // tag/log_20220123_205508.txt
        return logPath;
    }

    @NonNull
    @Override
    protected String getFolderPath() {
        return mFolder;
    }
}
