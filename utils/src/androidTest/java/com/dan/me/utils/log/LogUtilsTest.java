/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.log;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.dan.me.utils.io.FileUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.util.List;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;

import static com.dan.me.utils.log.GeneralLogger.GENERAL_SUB_PATH;
import static com.dan.me.utils.log.SpecializedLogger.SPECIALIZED_SUB_PATH;
import static org.junit.Assert.*;

public class LogUtilsTest {

    private static final String TAG = "LogUtilsTest";

    private static final String NEW_LINE = System.getProperty("line.separator");

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);

    private static final String sBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/log/";

    private static final long SLEEP_SHORT = 50;

    private static final long SLEEP_LONG = 1000;

    @BeforeClass
    public static void start() {
        Log.i(TAG, "start() sBasePath: " + sBasePath);
        // 测试开始前，先删除全部文件
        FileUtils.dirDelete(sBasePath, true);

        Context appContext = ApplicationProvider.getApplicationContext();
        LogUtilsOptions logUtilsOptions = new LogUtilsOptions.Builder()
                .setBasePath(sBasePath)
                .setGeneralFileMaxSize(24 * 1024/* 24K */)
                .setSpecializedFileMaxSize(24 * 1024/* 24K */)
                .create();
        LogUtils.setLogUtilsOptions(appContext, logUtilsOptions);
    }


    @AfterClass
    public static void stop() {
        // 临时调试一下LogVersions
        // LogUtils.logVersion();
        // 测试结束后，再删除全部文件
        FileUtils.dirDelete(sBasePath, true);
        LogUtils.clearLogUtilsOptions();
    }

    @Test
    public void v() {
        LogUtils logUtils = new LogUtils.Builder()
                .setSystemLogEnabled(true)
                .setGeneralSaveEnabled(false)
                .setSpecializedSaveEnabled(false)
                .create();
        logUtils.v(TAG, "VERBOSE: hello dan!");
    }

    @Test
    public void d() {
        LogUtils logUtils = new LogUtils.Builder()
                .setSystemLogEnabled(true)
                .setGeneralSaveEnabled(false)
                .setSpecializedSaveEnabled(false)
                .create();
        logUtils.d(TAG, "DEBUG: hello dan!");
    }

    // 考虑到写文件非同步的，所以休眠一下
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void i() {
        LogUtils logUtils = new LogUtils.Builder()
                .setSystemLogEnabled(true)
                .setGeneralSaveEnabled(true)
                .setSpecializedSaveEnabled(false)
                .create();
        StringBuilder contentLog = new StringBuilder("INFO: hello dan!");
        logUtils.i(TAG, contentLog.toString());
        // 考虑到写文件非同步的，所以休眠
        sleep(SLEEP_SHORT);
        final String logDir = sBasePath + GENERAL_SUB_PATH;
        List<File> fileList = FileUtils.fileList(logDir);
        assertEquals(1, fileList.size());
        File file = fileList.get(0);
        String contentRead = FileUtils.fileRead(file.getAbsolutePath());
        String contentExpect = contentLog + NEW_LINE;
        assertNotNull(contentRead);
        assertTrue(contentRead.endsWith(contentExpect));

        String basicLog = "hello dan!";
        contentLog = new StringBuilder();
        int count = 2048;
        for (int i = 0; i < count; i++) {
            contentLog.append(basicLog);
        }
        // !!! 打印需要adb shell去查看
        logUtils.i(TAG, contentLog.toString());
        // 考虑到写文件非同步的，所以休眠
        sleep(SLEEP_LONG);
        logUtils.i(TAG, contentLog.toString());
        // 考虑到写文件非同步的，所以休眠
        sleep(SLEEP_SHORT);
        fileList = FileUtils.fileList(logDir);
        assertEquals(2, fileList.size());
    }

    @Test
    public void w() {
        LogUtils logUtils = new LogUtils.Builder()
                .setSystemLogEnabled(true)
                .setGeneralSaveEnabled(false)
                .setSpecializedSaveEnabled(false)
                .create();
        logUtils.w(TAG, "WARNING: hello dan!");
    }

    @Test
    public void e() {
        LogUtils logUtils = new LogUtils.Builder()
                .setSystemLogEnabled(true)
                .setGeneralSaveEnabled(false)
                .setSpecializedSaveEnabled(true)
                .create();
        StringBuilder contentLog = new StringBuilder("ERROR: hello dan!");
        logUtils.e(TAG, contentLog.toString());
        // 考虑到写文件非同步的，所以休眠
        sleep(SLEEP_SHORT);
        final String logDir = sBasePath + SPECIALIZED_SUB_PATH;
        List<File> fileList = FileUtils.fileList(logDir);
        assertEquals(1, fileList.size());
        File file = fileList.get(0);
        String contentRead = FileUtils.fileRead(file.getAbsolutePath());
        String contentExpect = contentLog + NEW_LINE;
        assertNotNull(contentRead);
        assertTrue(contentRead.endsWith(contentExpect));

        String basicLog = "hello dan!";
        contentLog = new StringBuilder();
        int count = 2048;
        for (int i = 0; i < count; i++) {
            contentLog.append(basicLog);
        }
        // !!! 打印需要adb shell去查看
        logUtils.e(TAG, contentLog.toString());
        // 考虑到写文件非同步的，所以休眠
        sleep(SLEEP_LONG);
        logUtils.e(TAG, contentLog.toString());
        // 考虑到写文件非同步的，所以休眠
        sleep(SLEEP_SHORT);
        fileList = FileUtils.fileList(logDir);
        assertEquals(2, fileList.size());
    }

    @Test
    public void wtf() {
        LogUtils logUtils = new LogUtils.Builder()
                .setSystemLogEnabled(true)
                .setGeneralSaveEnabled(false)
                .setSpecializedSaveEnabled(false)
                .create();
        logUtils.wtf(TAG, "WTF: hello dan!");
    }
}