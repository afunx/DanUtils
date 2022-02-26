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

import java.io.File;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ZipUtilsTest {

    private static final String TAG = "ZipUtilsTest";

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);

    private static final String sBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/zip";

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

    // TODO 由于时间关系，所以需要人工通过adb shell查看一下文件压缩是否正确
    @Test
    public void zipAndUnZip() {
        Context appContext = ApplicationProvider.getApplicationContext();
        String srcFileName = "zip.zip";
        String srcFilePath = sBasePath + File.separator + srcFileName;
        boolean suc = AssetsUtils.fileCopy(appContext, srcFileName, srcFilePath);
        assertTrue(suc);
        // 解压
        suc = ZipUtils.unzip(srcFilePath);
        assertTrue(suc);
        // 压缩文件
        String content = "hello dandan";
        srcFileName = "haha.txt";
        String destFileName = "haha.txt.zip";
        srcFilePath = sBasePath + File.separator + srcFileName;
        FileUtils.fileWrite(srcFilePath, content);
        String destFilePath = sBasePath + File.separator + "dest" + File.separator + destFileName;
        suc = ZipUtils.zip(srcFilePath, destFilePath);
        assertTrue(suc);
        // 压缩文件夹
        srcFileName = "zip";
        destFileName = "zip.zip";
        srcFilePath = sBasePath + File.separator + srcFileName;
        destFilePath = sBasePath + File.separator + "dest" + File.separator + destFileName;
        suc = ZipUtils.zip(srcFilePath, destFilePath);
        assertTrue(suc);
    }
}