/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.log;

public class LogUtilsOptions {

    private final String mBasePath;

    private final long mGeneralFileMaxSize;

    private final long mSpecializedFileMaxSize;

    private final long mGeneralTotalMaxSize;

    private final long mSpecializedTotalMaxSize;

    private final long mCrashTotalMaxSize;

    private LogUtilsOptions(String basePath, long generalFileMaxSize, long specializedFileMaxSize,
                            long generalTotalMaxSize, long specializedTotalMaxSize, long crashTotalMaxSize) {
        mBasePath = basePath;
        mGeneralFileMaxSize = generalFileMaxSize;
        mSpecializedFileMaxSize = specializedFileMaxSize;
        mGeneralTotalMaxSize = generalTotalMaxSize;
        mSpecializedTotalMaxSize = specializedTotalMaxSize;
        mCrashTotalMaxSize = crashTotalMaxSize;
    }

    public String getBasePath() {
        return mBasePath;
    }

    public long getGeneralFileMaxSize() {
        return mGeneralFileMaxSize;
    }

    public long getSpecializedFileMaxSize() {
        return mSpecializedFileMaxSize;
    }

    public long getGeneralTotalMaxSize() {
        return mGeneralTotalMaxSize;
    }

    public long getSpecializedTotalMaxSize() {
        return mSpecializedTotalMaxSize;
    }

    public static class Builder {

        private String basePath;

        private long generalFileMaxSize = 4 * 1024 * 1024;

        private long specializedFileMaxSize = 4 * 1024 * 1024;

        private long generalTotalMaxSize = 24 * 1024 * 1024;

        private long specializedTotalMaxSize = 24 * 1024 * 1024;

        private long crashTotalMaxSize = 24 * 1024 * 1024;

        public Builder setBasePath(String basePath) {
            this.basePath = basePath;
            return this;
        }

        public Builder setGeneralFileMaxSize(long generalFileMaxSize) {
            this.generalFileMaxSize = generalFileMaxSize;
            return this;
        }

        public Builder setSpecializedFileMaxSize(long specializedFileMaxSize) {
            this.specializedFileMaxSize = specializedFileMaxSize;
            return this;
        }

        public Builder setGeneralTotalMaxSize(long generalTotalMaxSize) {
            this.generalTotalMaxSize = generalTotalMaxSize;
            return this;
        }

        public Builder setSpecializedTotalMaxSize(long specializedTotalMaxSize) {
            this.specializedTotalMaxSize = specializedTotalMaxSize;
            return this;
        }

        public Builder setCrashTotalMaxSize(long crashTotalMaxSize) {
            this.crashTotalMaxSize = crashTotalMaxSize;
            return this;
        }

        public LogUtilsOptions create() {
            return new LogUtilsOptions(this.basePath, this.generalFileMaxSize, this.specializedFileMaxSize,
                    this.generalTotalMaxSize, this.specializedTotalMaxSize, this.crashTotalMaxSize);
        }
    }
}
