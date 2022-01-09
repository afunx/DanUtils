/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private static final String sBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test";

    @BeforeClass
    public static void start() {
        Log.i(TAG, "start() sBasePath: " + sBasePath);
        // 测试开始前，先删除全部文件
         FileUtils.dirDelete(sBasePath, true);
    }

    @AfterClass
    public static void stop() {
        // 测试结束后，再删除全部文件
        //FileUtils.dirDelete(sBasePath, true);
    }

    @Before
    public void before() {
        appContext = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void fileRead() {
        // 读写小文件
        String filePath = sBasePath + "/read/01.txt";
        String contentWrite = "hello dan!!!";
        boolean suc = FileUtils.fileWrite(filePath, contentWrite);
        String contentRead = FileUtils.fileRead(filePath);
        assertEquals(contentWrite, contentRead);
        assertTrue(suc);
        // 读写非1024倍数的较大文件
        filePath = sBasePath + "/read/02.txt";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5000; i++) {
            sb.append(i);
        }
        contentWrite = sb.toString();
        suc = FileUtils.fileWrite(filePath, contentWrite);
        contentRead = FileUtils.fileRead(filePath);
        assertEquals(contentWrite, contentRead);
        assertTrue(suc);
    }

    @Test
    public void fileWrite() {
        String filePath = sBasePath + "/write/01.txt";
        String contentWrite = "";
        boolean suc = FileUtils.fileWrite(filePath, contentWrite);
        assertTrue(suc);
        String contentRead = FileUtils.fileRead(filePath);
        assertEquals(contentWrite, contentRead);
    }

    @Test
    public void fileCopy() {
        String filePath = sBasePath + "/copy01/01.txt";
        String contentWrite = "hello dan dan!!!";
        boolean suc = FileUtils.fileWrite(filePath, contentWrite);
        assertTrue(suc);
        String copyPath = sBasePath + "/copy02/01.txt";
        String tempPath = sBasePath + "/copytemp/01.txt";
        suc = FileUtils.fileCopy(filePath, tempPath, copyPath);
        assertTrue(suc);
        String contentRead = FileUtils.fileRead(copyPath);
        assertEquals(contentWrite, contentRead);
        contentRead = FileUtils.fileRead(tempPath);
        assertNull(contentRead);
    }

    @Test
    public void fileMove() {
        String filePath = sBasePath + "/move01/01.txt";
        String contentWrite = "hello hello dan !!!";
        boolean suc = FileUtils.fileWrite(filePath, contentWrite);
        assertTrue(suc);
        String movePath = sBasePath + "/move02/01.txt";
        suc = FileUtils.fileMove(filePath, movePath);
        assertTrue(suc);
        String contentRead = FileUtils.fileRead(movePath);
        assertEquals(contentWrite, contentRead);
        contentRead = FileUtils.fileRead(filePath);
        assertNull(contentRead);
    }

    private void prepare4dirDelete(String filePathDir) {
        String contentWrite = "hello dan!!!";
        String filePath0101 = filePathDir + "/01/01.txt";
        boolean suc = FileUtils.fileWrite(filePath0101, contentWrite);
        assertTrue(suc);
        String filePath0102 = filePathDir + "/01/02.txt";
        suc = FileUtils.fileWrite(filePath0102, contentWrite);
        assertTrue(suc);
        String filePath0201 = filePathDir + "/02/01.txt";
        suc = FileUtils.fileWrite(filePath0201, contentWrite);
        assertTrue(suc);
        String filePath0301 = filePathDir + "/03/01.txt";
        suc = FileUtils.fileWrite(filePath0301, contentWrite);
        assertTrue(suc);
        suc = FileUtils.fileDelete(filePath0301);
        assertTrue(suc);
    }

    @Test
    public void dirDelete() {
        // 删除文件夹本身
        String filePathDir = sBasePath + "/delete01";
        prepare4dirDelete(filePathDir);
        boolean suc = FileUtils.dirDelete(filePathDir, true);
        assertTrue(suc);
        File file = new File(filePathDir);
        assertFalse(file.exists());
        // 不删除文件夹本身
        filePathDir = sBasePath + "/delete02";
        prepare4dirDelete(filePathDir);
        suc = FileUtils.dirDelete(filePathDir, false);
        assertTrue(suc);
        file = new File(filePathDir);
        assertTrue(file.exists());
        int fileCount = Objects.requireNonNull(file.listFiles()).length;
        assertEquals(0, fileCount);
        // 删除文件夹本身，删除01.txt文件
        filePathDir = sBasePath + "/delete03";
        prepare4dirDelete(filePathDir);
        // 被删除的文件
        String deleteFile = "01.txt";
        suc = FileUtils.dirDelete(filePathDir, true, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return true;
                } else if (pathname.isFile()) {
                    String name = pathname.getName();
                    return name.equals(deleteFile);
                } else {
                    return false;
                }
            }
        });
        assertTrue(suc);
        List<File> fileList = FileUtils.fileList(filePathDir);
        for (File fileInList : fileList) {
            assertNotEquals(deleteFile, fileInList.getName());
        }
    }

    @Test
    public void fileDelete() {
        // 删除某一文件
        String filePath = sBasePath + "/delete04/01.txt";
        String contentWrite = "hello dan dan!!!";
        boolean suc = FileUtils.fileWrite(filePath, contentWrite);
        assertTrue(suc);
        suc = FileUtils.fileDelete(filePath);
        assertTrue(suc);
        File file = new File(filePath);
        assertFalse(file.exists());
        // 删除某一类文件
        String fileParentPath = sBasePath + "/delete05";
        filePath = fileParentPath + "/01.txt";
        suc = FileUtils.fileWrite(filePath, contentWrite);
        assertTrue(suc);
        filePath = fileParentPath + "/02.txt";
        suc = FileUtils.fileWrite(filePath, contentWrite);
        assertTrue(suc);
        filePath = sBasePath + "/03.txt";
        suc = FileUtils.fileWrite(filePath, contentWrite);
        String deleteFile = "01.txt";
        assertTrue(suc);
        suc = FileUtils.fileDelete(fileParentPath, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) {
                    return true;
                } else if (pathname.isFile()) {
                    String name = pathname.getName();
                    return name.equals(deleteFile);
                } else {
                    return false;
                }
            }
        });
        assertTrue(suc);
        List<File> fileList = FileUtils.fileList(fileParentPath);
        for (File fileInList : fileList) {
            assertNotEquals(deleteFile, fileInList.getName());
        }
    }

    @Test
    public void fileList() {
        List<String> filePathList = new ArrayList<>();
        String fileParentPath = sBasePath + "/list";
        String filePath = fileParentPath + "/01.txt";
        filePathList.add(filePath);
        String contentWrite = "hello dan dan!!!";
        boolean suc = FileUtils.fileWrite(filePath, contentWrite);
        assertTrue(suc);
        filePath = fileParentPath + "/02/01.txt";
        filePathList.add(filePath);
        suc = FileUtils.fileWrite(filePath, contentWrite);
        assertTrue(suc);
        filePath = fileParentPath + "/02.txt";
        filePathList.add(filePath);
        suc = FileUtils.fileWrite(filePath, contentWrite);
        assertTrue(suc);
        List<File> fileList = FileUtils.fileList(fileParentPath);
        for (File file : fileList) {
            boolean remove = filePathList.remove(file.getAbsolutePath());
            assertTrue(remove);
        }
        assertTrue(filePathList.isEmpty());
    }

    @Test
    public void fileAppend() {
        String filePath = sBasePath + "/append/01.txt";
        String contentWrite = "hello dan!!!";
        StringBuilder contentWrites = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            boolean suc = FileUtils.fileAppend(filePath, contentWrite);
            assertTrue(suc);
            contentWrites.append(contentWrite);
        }
        String contentRead = FileUtils.fileRead(filePath);
        assertEquals(contentWrites.toString(), contentRead);
    }

    @Test
    public void fileMD5() {
        String filePath = sBasePath + "/md5/01.txt";
        String contentWrite = "hello dan!!!";
        boolean suc = FileUtils.fileWrite(filePath, contentWrite);
        assertTrue(suc);
        String MD5 = FileUtils.fileMD5(filePath);
        assertEquals("9e79549ea42fa7d8a457dd4ec97aabe3", MD5);
    }
}