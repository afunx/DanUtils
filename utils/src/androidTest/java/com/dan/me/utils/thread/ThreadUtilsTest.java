/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.thread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

public class ThreadUtilsTest {

    private static final String TAG = "ThreadUtilsTest";

    private Handler getHandler(boolean main) {
        if (main) {
            return new Handler(Looper.getMainLooper());
        } else {
            HandlerThread handlerThread = new HandlerThread("HandlerThread");
            handlerThread.start();
            return new Handler(handlerThread.getLooper());
        }
    }

    @Test
    public void assertMainThread() {
        Log.i(TAG, "assertMainThread()");
        Handler mainHandler = getHandler(true);
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "assertMainThread() E");
                ThreadUtils.assertMainThread();
                Log.i(TAG, "assertMainThread() X");
            }
        });
    }

    @Test
    public void assertWorkThread() {
        Log.i(TAG, "assertWorkThread()");
        Handler workHandler = getHandler(false);
        workHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "assertWorkThread() E");
                ThreadUtils.assertWorkThread();
                Log.i(TAG, "assertWorkThread() X");
            }
        });
    }
}