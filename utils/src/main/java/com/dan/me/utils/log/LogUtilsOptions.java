/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.log;

public class LogUtilsOptions {

    private final String mBasePath;

    private final long mGeneralFileMaxSize;

    private final long mSpecializedFileMaxSize;

    private LogUtilsOptions(String basePath, long generalFileMaxSize, long specializedFileMaxSize) {
        mBasePath = basePath;
        mGeneralFileMaxSize = generalFileMaxSize;
        mSpecializedFileMaxSize = specializedFileMaxSize;
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

    public static class Builder {

        private String basePath;

        private long generalFileMaxSize = 4 * 1024 * 1024;

        private long specializedFileMaxSize = 4 * 1024 * 1024;

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

        public LogUtilsOptions create() {
            return new LogUtilsOptions(this.basePath, this.generalFileMaxSize, this.specializedFileMaxSize);
        }
    }
}
