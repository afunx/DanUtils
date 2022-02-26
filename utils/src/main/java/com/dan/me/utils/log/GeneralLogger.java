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
import androidx.annotation.Nullable;

class GeneralLogger extends BaseLogger {

    private static final String TAG = "GeneralLogger";

    static final String GENERAL_SUB_PATH = "general" + File.separator;
    private static final String LOG_FILE_PREFIX = "log_";

    private String mFolder;
    private String mLogPath;

    @NonNull
    @Override
    protected String getTag() {
        return TAG;
    }

    @NonNull
    @Override
    protected String getHandelThreadName() {
        return "GeneralLoggerHandlerThread";
    }

    @Override
    protected void setFolder(@NonNull String basePath) {
        // 别忘记清除mLogPath
        mLogPath = null;
        mFolder = basePath.endsWith(File.separator) ? basePath + GENERAL_SUB_PATH : basePath + File.separator + GENERAL_SUB_PATH;
        if (DEBUG) {
            Log.i(TAG, "setFolder() basePath: " + basePath + ", folder: " + mFolder);
        }
    }

    @Nullable
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
        // log_20220123_100319.txt
        return mLogPath;
    }

    @Nullable
    @Override
    protected String getLogPath(long time, String tag) {
        return null;
    }
}
