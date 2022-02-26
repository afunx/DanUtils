/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.io;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AssetsUtilsTest {

    private static final String TAG = "AssetsUtilsTest";

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);

    private static final String sBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/assets";

    @BeforeClass
    public static void start() {
        Log.i(TAG, "start() sBasePath: " + sBasePath);
        // 测试开始前，先删除全部文件
        FileUtils.dirDelete(sBasePath, true);
    }

    @AfterClass
    public static void stop() {
        // 测试结束后，再删除全部文件
        FileUtils.dirDelete(sBasePath, true);
    }


    @Test
    public void fileRead() {
        Context appContext = ApplicationProvider.getApplicationContext();
        String fileName = "dandan.txt";
        String content = AssetsUtils.fileRead(appContext, fileName);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            sb.append("hello dan dan" + "\n");
        }
        assertEquals(sb.toString(), content);
    }

    @Test
    public void fileCopy() {
        String fileName = "dandan.txt";
        String filePath = sBasePath + "/dandan.txt";
        Context appContext = ApplicationProvider.getApplicationContext();
        boolean suc = AssetsUtils.fileCopy(appContext, fileName, filePath);
        assertTrue(suc);
        String content = FileUtils.fileRead(filePath);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 256; i++) {
            sb.append("hello dan dan" + "\n");
        }
        assertEquals(sb.toString(), content);
    }
}