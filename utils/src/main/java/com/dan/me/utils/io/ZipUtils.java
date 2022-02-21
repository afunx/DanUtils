/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import androidx.annotation.NonNull;

public class ZipUtils {

    private static final int READ_BUFFER_SIZE = 1024;

    private static final String ZIP_FILE_EXTENSION = ".zip";

    /**
     * 用zip压缩文件夹或文件
     *
     * @param srcFilePath  原文件夹或原文件
     * @param destFilePath 目标路径
     * @return 是否压缩成功
     */
    public static boolean zip(@NonNull String srcFilePath, @NonNull String destFilePath) {
        File srcFile = new File(srcFilePath);
        // 检查源文件是否存在
        if (!srcFile.exists()) {
            return false;
        }
        // 检查destFilePath的文件扩展名
        if (!destFilePath.endsWith(ZIP_FILE_EXTENSION)) {
            return false;
        }
        File destFile = new File(destFilePath);
        // 确保目标路径上的所有父文件路径存在
        File parentFile = destFile.getParentFile();
        if (parentFile == null) {
            throw new NullPointerException("parentFile is null for destFilePath: " + destFilePath);
        }
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        // 创建压缩文件对象
        ZipOutputStream zos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destFilePath);
            zos = new ZipOutputStream(fos);
            if (srcFile.isDirectory()) {
                int endIndex = destFile.getName().length() - ZIP_FILE_EXTENSION.length();
                zipInternal(zos, srcFile, destFile.getName().substring(0, endIndex));
            } else {
                zipInternal(zos, srcFile, srcFile.getName());
            }
            zos.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private static void zipInternal(@NonNull ZipOutputStream zos, @NonNull File srcFile, @NonNull String relativePath) throws IOException {
        if (srcFile.isDirectory()) {
            File[] files = srcFile.listFiles();
            if (files == null) {
                throw new IOException("zipInternal() files is null");
            }
            srcFile.mkdirs();
            ZipEntry zipEntry = new ZipEntry(relativePath + File.separator);
            zos.putNextEntry(zipEntry);
            for (File file : files) {
                zipInternal(zos, file, relativePath + File.separator + file.getName());
            }
            zos.closeEntry();
        } else {
            ZipEntry zipEntry = new ZipEntry(relativePath);
            zos.putNextEntry(zipEntry);
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(srcFile);
                int len;
                byte[] buffer = new byte[READ_BUFFER_SIZE];
                while ((len = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, len);
                }
                fis.close();
                zos.closeEntry();
                zos.flush();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 用zip解压文件
     *
     * @param srcFilePath    zip源文件路径
     * @param destParentPath 目标父路径
     * @return 是否解压成功
     */
    public static boolean unzip(@NonNull String srcFilePath, @NonNull String destParentPath) {
        File srcFile = new File(srcFilePath);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return false;
        }
        // 创建压缩文件对象
        ZipFile zipFile = null;
        InputStream in = null;
        FileOutputStream fos = null;
        try {
            zipFile = new ZipFile(srcFile);
            // 解压
            Enumeration<?> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                String filePath = destParentPath + File.separator + zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    // 如果zipEntry是文件夹
                    new File(filePath).mkdirs();
                } else {
                    // 如果zipEntry是文件
                    File destEntryFile = new File(filePath);
                    // 保证文件的父文件夹路径必须存在
                    if (destEntryFile.getParentFile() == null) {
                        throw new NullPointerException("parentFile is null for destFilePath: " + destParentPath);
                    }
                    if (!destEntryFile.getParentFile().exists()) {
                        destEntryFile.getParentFile().mkdirs();
                    }
                    destEntryFile.createNewFile();
                    // 将压缩文件内容写入到这个文件中
                    in = zipFile.getInputStream(zipEntry);
                    fos = new FileOutputStream(destEntryFile);
                    int len;
                    byte[] buffer = new byte[READ_BUFFER_SIZE];
                    while ((len = in.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 文件解压，解压在zip文件相同路径下的同名文件夹内。
     * e.g. /sdcard/dandan/zip.zip -> /sdcard/dandan/zip/
     *
     * @param zipFilePath zip文件路径
     * @return 是否解压成功
     */
    public static boolean unzip(@NonNull String zipFilePath) {
        // 检查文件后缀
        if (!zipFilePath.endsWith(ZIP_FILE_EXTENSION)) {
            return false;
        }
        // 检查文件是否存在
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            return false;
        }
        // 检查文件父路径是否存在
        File parentFile = zipFile.getParentFile();
        if (parentFile == null || !parentFile.exists()) {
            return false;
        }
        // 删除旧的文件夹和文件
        String destDirPath = zipFilePath.replace(ZIP_FILE_EXTENSION, "");
        File destFile = new File(destDirPath);
        if (destFile.exists()) {
            FileUtils.dirDelete(destDirPath, true);
        }
        // 解压
        return unzip(zipFilePath, parentFile.getAbsolutePath());
    }
}
