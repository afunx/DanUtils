/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import android.content.Context;

import com.dan.me.utils.log.LogUtils;

import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;

public class UtilsContentInit {

    private static final String TAG = "UtilsContentInit";

    private static final boolean DEBUG = UtilsContentConstants.DEBUG;

    private static final AtomicBoolean sInitialized = new AtomicBoolean(false);

    private static final LogUtils LOGGER = new LogUtils.Builder()
            .setSystemLogEnabled(true)
            .setGeneralSaveEnabled(true)
            .setSpecializedSaveEnabled(false)
            .create();

    public static void initialize(@NonNull Context appContext) {
        if (sInitialized.getAndSet(true)) {
            return;
        }
        appContext = appContext.getApplicationContext();
        if (DEBUG) {
            LOGGER.i(TAG, "initialize() appContext: " + appContext);
        }
        UtilsContentResolver.setAppContent(appContext);
        UtilsContentNotifier.get().registerContentObserver(appContext);
        UtilsContentCacheResolver.setAppContent(appContext);
        UtilsContentCacheNotifier.get().registerContentObserver(appContext);
    }
}
