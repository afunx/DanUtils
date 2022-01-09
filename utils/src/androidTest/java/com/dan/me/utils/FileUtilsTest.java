/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class FileUtilsTest {

    private static final String TAG = "FileUtilsTest";

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);

    private Context appContext;

    private String basePath;

    @Before
    public void before() {
        appContext = ApplicationProvider.getApplicationContext();
        basePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    @Test
    public void read() {
        // 读写小文件
        String filePath = basePath + "/test/read/01.txt";
        Log.i(TAG, "read() filePath: " + filePath);
        String contentWrite = "hello dan!!!";
        boolean suc = FileUtils.write(filePath, contentWrite);
        String contentRead = FileUtils.read(filePath);
        assertEquals(contentWrite, contentRead);
        assertTrue(suc);
        // 读写非1024倍数的较大文件
        filePath = basePath + "/test/read/02.txt";
        Log.i(TAG, "read() filePath: " + filePath);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5000; i++) {
            sb.append(i);
        }
        contentWrite = sb.toString();
        suc = FileUtils.write(filePath, contentWrite);
        contentRead = FileUtils.read(filePath);
        assertEquals(contentWrite, contentRead);
        assertTrue(suc);
    }

    @Test
    public void write() {
        // 已在read()中测试过
    }

    @Test
    public void fileCopy() {
    }

    @Test
    public void fileMove() {
    }

    @Test
    public void dirDelete() {
    }

    @Test
    public void testDirDelete() {
    }

    @Test
    public void fileDelete() {
    }

    @Test
    public void testFileDelete() {
    }

    @Test
    public void fileAppend() {
    }

    @Test
    public void md5sum() {
    }
}