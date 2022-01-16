/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.log;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

public class LogUtils {

    private static final String TAG = "LogUtils";

    private static final int SINGLE_LINE_MAX_LENGTH = 2048;

    static final int LEVEL_V = 0;
    static final int LEVEL_D = 1;
    static final int LEVEL_I = 2;
    static final int LEVEL_W = 3;
    static final int LEVEL_E = 4;
    static final int LEVEL_WTF = 5;
    static final String LEVEL_V_STR = "V";
    static final String LEVEL_D_STR = "D";
    static final String LEVEL_I_STR = "I";
    static final String LEVEL_W_STR = "W";
    static final String LEVEL_E_STR = "E";
    static final String LEVEL_WTF_STR = "WTF";

    private static LogUtilsOptions sLogUtilsOptions = null;

    // 是否使能Android系统日志
    private final boolean mSystemLogEnabled;
    // 是否使能通用日志存储
    private final boolean mGeneralSaveEnabled;
    // 是否使能特殊日志存储
    private final boolean mSpecializedSaveEnabled;

    private static final GeneralLogger sLogGeneralHandler = new GeneralLogger();

    private LogUtils(boolean systemLogEnabled, boolean generalSaveEnabled, boolean specializedSaveEnabled) {
        mSystemLogEnabled = systemLogEnabled;
        mGeneralSaveEnabled = generalSaveEnabled;
        mSpecializedSaveEnabled = specializedSaveEnabled;
    }

    /**
     * 设置LogUtils的配置参数，但只能设置一次
     *
     * @param appContext Application Context
     * @param logUtilsOptions LogUtils的配置参数
     */
    public static void setLogUtilsOptions(@NonNull Context appContext, @NonNull LogUtilsOptions logUtilsOptions) {
        if (sLogUtilsOptions != null) {
            throw new IllegalStateException("sLogUtilsOptions could be set only once");
        }
        sLogUtilsOptions = logUtilsOptions;
        sLogGeneralHandler.setPackageName(appContext.getPackageName());
        sLogGeneralHandler.setFolder(logUtilsOptions.getBasePath());
        sLogGeneralHandler.setMaxFileSize(logUtilsOptions.getGeneralFileMaxSize());
    }

    // 虽然，这个实现会在最前面多一些内容。但是，考虑到多线程情况，这个是目前想到最好的实现。
    private void logLong(final String tag, final String message, int level) {
        // 第一部分日志
        switch (level) {
            case LEVEL_V:
                v(tag, message.substring(0, SINGLE_LINE_MAX_LENGTH));
                break;
            case LEVEL_D:
                d(tag, message.substring(0, SINGLE_LINE_MAX_LENGTH));
                break;
            case LEVEL_I:
                i(tag, message.substring(0, SINGLE_LINE_MAX_LENGTH));
                break;
            case LEVEL_W:
                w(tag, message.substring(0, SINGLE_LINE_MAX_LENGTH));
                break;
            case LEVEL_E:
                e(tag, message.substring(0, SINGLE_LINE_MAX_LENGTH));
                break;
            case LEVEL_WTF:
                wtf(tag, message.substring(0, SINGLE_LINE_MAX_LENGTH));
                break;
            default:
                throw new IllegalStateException();
        }
        // 剩余日志
        final int length = message.length();
        int end;
        for (int start = SINGLE_LINE_MAX_LENGTH; start < length; start += SINGLE_LINE_MAX_LENGTH) {
            end = Math.min(start + SINGLE_LINE_MAX_LENGTH, message.length());
            switch (level) {
                case LEVEL_V:
                    v(tag, message.substring(start, end));
                    break;
                case LEVEL_D:
                    d(tag, message.substring(start, end));
                    break;
                case LEVEL_I:
                    i(tag, message.substring(start, end));
                    break;
                case LEVEL_W:
                    w(tag, message.substring(start, end));
                    break;
                case LEVEL_E:
                    e(tag, message.substring(start, end));
                    break;
                case LEVEL_WTF:
                    wtf(tag, message.substring(start, end));
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
    }

    public void v(final String tag, final String msg) {
        if (sLogUtilsOptions == null) {
            Log.v(TAG, "v() sLogUtilsOptions is null");
            return;
        }
        if (msg.length() > SINGLE_LINE_MAX_LENGTH) {
            logLong(tag, msg, LEVEL_V);
        } else {
            if (mSystemLogEnabled) {
                Log.v(tag, msg);
            }
            if (mGeneralSaveEnabled) {
                sLogGeneralHandler.log(LEVEL_V, tag, msg);
            }
        }
    }

    public void d(final String tag, final String msg) {
        if (sLogUtilsOptions == null) {
            Log.d(TAG, "d() sLogUtilsOptions is null");
            return;
        }
        if (msg.length() > SINGLE_LINE_MAX_LENGTH) {
            logLong(tag, msg, LEVEL_D);
        } else {
            if (mSystemLogEnabled) {
                Log.d(tag, msg);
            }
            if (mGeneralSaveEnabled) {
                sLogGeneralHandler.log(LEVEL_D, tag, msg);
            }
        }
    }

    public void i(final String tag, final String msg) {
        if (sLogUtilsOptions == null) {
            Log.i(TAG, "i() sLogUtilsOptions is null");
            return;
        }
        if (msg.length() > SINGLE_LINE_MAX_LENGTH) {
            logLong(tag, msg, LEVEL_I);
        } else {
            if (mSystemLogEnabled) {
                Log.i(tag, msg);
            }
            if (mGeneralSaveEnabled) {
                sLogGeneralHandler.log(LEVEL_I, tag, msg);
            }
        }
    }

    public void w(final String tag, final String msg) {
        if (sLogUtilsOptions == null) {
            Log.w(TAG, "w() sLogUtilsOptions is null");
            return;
        }
        if (msg.length() > SINGLE_LINE_MAX_LENGTH) {
            logLong(tag, msg, LEVEL_W);
        } else {
            if (mSystemLogEnabled) {
                Log.w(tag, msg);
            }
            if (mGeneralSaveEnabled) {
                sLogGeneralHandler.log(LEVEL_W, tag, msg);
            }
        }
    }

    public void e(final String tag, final String msg) {
        if (sLogUtilsOptions == null) {
            Log.e(TAG, "e() sLogUtilsOptions is null");
            return;
        }
        if (msg.length() > SINGLE_LINE_MAX_LENGTH) {
            logLong(tag, msg, LEVEL_E);
        } else {
            if (mSystemLogEnabled) {
                Log.e(tag, msg);
            }
            if (mGeneralSaveEnabled) {
                sLogGeneralHandler.log(LEVEL_E, tag, msg);
            }
        }
    }

    public void wtf(final String tag, final String msg) {
        if (sLogUtilsOptions == null) {
            Log.wtf(TAG, "wtf() sLogUtilsOptions is null");
            return;
        }
        if (msg.length() > SINGLE_LINE_MAX_LENGTH) {
            logLong(tag, msg, LEVEL_WTF);
        } else {
            if (mSystemLogEnabled) {
                Log.wtf(tag, msg);
            }
            if (mGeneralSaveEnabled) {
                sLogGeneralHandler.log(LEVEL_WTF, tag, msg);
            }
        }
    }

    public static class Builder {
        // 是否使能Android系统日志
        private Boolean systemLogEnabled = null;
        // 是否使能通用日志存储
        private Boolean generalSaveEnabled = null;
        // 是否使能特殊日志存储
        private Boolean specializedSaveEnabled = null;

        /**
         * 使能Android系统日志
         *
         * @param enabled 是否使能
         * @return Builder
         */
        public Builder setSystemLogEnabled(boolean enabled) {
            this.systemLogEnabled = enabled;
            return this;
        }

        /**
         * 使能通用日志存储
         *
         * @param enabled 是否使能
         * @return Builder
         */
        public Builder setGeneralSaveEnabled(boolean enabled) {
            this.generalSaveEnabled = enabled;
            return this;
        }

        /**
         * 使能特殊日志存储
         *
         * @param enabled 是否使能
         * @return Builder
         */
        public Builder setSpecializedSaveEnabled(boolean enabled) {
            this.specializedSaveEnabled = enabled;
            return this;
        }

        /**
         * 创建LogUtils
         *
         * @return LogUtils
         */
        public LogUtils create() {
            if (this.systemLogEnabled == null) {
                throw new NullPointerException("please set systemLogEnabled firstly");
            }
            if (this.generalSaveEnabled == null) {
                throw new NullPointerException("please set generalSaveEnabled firstly");
            }
            if (this.specializedSaveEnabled == null) {
                throw new NullPointerException("please set specializedSaveEnabled firstly");
            }
            return new LogUtils(this.systemLogEnabled, this.generalSaveEnabled, this.specializedSaveEnabled);
        }
    }
}
