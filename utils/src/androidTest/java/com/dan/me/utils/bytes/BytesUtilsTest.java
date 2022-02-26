/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.bytes;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class BytesUtilsTest {

    @Test
    public void bytes2hexString() {
        String s = BytesUtils.bytes2hexString(new byte[]{(byte) 0xff, (byte) 0xfe, (byte) 0x00, (byte) 0x01});
        assertEquals(s, "fffe0001");
    }

    @Test
    public void testBytes2hexString() {
        String s = BytesUtils.bytes2hexString(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04}, 1,2);
        assertEquals(s, "0203");
    }
}