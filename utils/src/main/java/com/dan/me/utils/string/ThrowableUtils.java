/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.string;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import androidx.annotation.NonNull;

public class ThrowableUtils {
    /**
     * 解析Throwable
     *
     * @param e Throwable
     * @return string
     */
    public static String parseStackTrace(@NonNull Throwable e) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(bos);
        e.printStackTrace(ps);
        ps.close();
        return bos.toString();
    }
}
