/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.log;

public class LogUtilsOptions {

    private final String mBasePath;

    private final long mGeneralFileMaxSize;

    private LogUtilsOptions(String basePath, long generalFileMaxSize) {
        mBasePath = basePath;
        mGeneralFileMaxSize = generalFileMaxSize;
    }

    public String getBasePath() {
        return mBasePath;
    }

    public long getGeneralFileMaxSize() {
        return mGeneralFileMaxSize;
    }

    public static class Builder {

        private String basePath;

        private long generalFileMaxSize = 4 * 1024 * 1024;

        public Builder setBasePath(String basePath) {
            this.basePath = basePath;
            return this;
        }

        public Builder setGeneralFileMaxSize(long generalFileMaxSize) {
            this.generalFileMaxSize = generalFileMaxSize;
            return this;
        }

        public LogUtilsOptions create() {
            return new LogUtilsOptions(this.basePath, this.generalFileMaxSize);
        }
    }
}
