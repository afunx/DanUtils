/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.thread;

import android.os.Looper;

public class ThreadUtils {

    /**
     * 断言主线程
     */
    public static void assertMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AssertionError("assertMainThread() fail");
        }
    }

    /**
     * 断言非主线程
     */
    public static void assertWorkThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new AssertionError("assertWorkThread() fail");
        }
    }

}
