/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils;

public class BytesUtils {

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String bytes2hexString(byte[] bytes) {
        return bytes2hexString(bytes, 0, bytes.length);
    }

    public static String bytes2hexString(byte[] bytes, int offset, int length) {
        char[] buf = new char[length << 1];
        for (int i = 0; i < length; i++) {
            buf[i << 1] = HEX_CHAR[bytes[i + offset] >>> 4 & 0x0f];
            buf[(i << 1) + 1] = HEX_CHAR[bytes[i + offset] & 0x0f];
        }
        return new String(buf);
    }
}
