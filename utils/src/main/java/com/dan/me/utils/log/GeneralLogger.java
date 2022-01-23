/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */
package com.dan.me.utils.log;

import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;

class GeneralLogger extends BaseLogger {

    private static final String TAG = "GeneralLogger";

    private static final String GENERAL_SUB_PATH = "general" + File.separator;
    private static final String LOG_FILE_PREFIX = "log_";

    private long mMaxFileSize = 4 * 1024 * 1024;
    private String mFolder;
    private String mLogPath;

    void setMaxFileSize(long maxFileSize) {
        mMaxFileSize = maxFileSize;
    }

    void setFolder(@NonNull String basePath) {
        mFolder = basePath.endsWith(File.separator) ? basePath + GENERAL_SUB_PATH : basePath + File.separator + GENERAL_SUB_PATH;
        if (DEBUG) {
            Log.i(TAG, "setFolder() basePath: " + basePath + ", folder: " + mFolder);
        }
    }

    @Override
    protected String getTag() {
        return TAG;
    }

    @Override
    protected String getHandelThreadName() {
        return "GeneralLoggerHandlerThread";
    }

    protected String getLogPath(long time) {
        if (DEBUG) {
            if (mLogPath != null) {
                Log.i(TAG, "getLogPath() logPath: " + mLogPath + ", file.length: " + new File(mLogPath).length());
            }
        }
        if (mLogPath == null || new File(mLogPath).length() >= mMaxFileSize) {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String logName = LOG_FILE_PREFIX + fmt.format(new Date(time)) + LOG_FILE_EXTENSION;
            mLogPath = mFolder + logName;
        }
        return mLogPath;
    }
}
