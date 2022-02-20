/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.io;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AssetsUtils {

    private static final int READ_BUFFER_SIZE = 1024;

    private static final int COPY_FILE_BUFFER_SIZE = 1024;

    /**
     * 读取assets下的文件
     *
     * @param appContext Application Context
     * @param fileName   assets下的文件名
     * @return 被读的文件内容或null（若文件不存在）
     */
    @Nullable
    public static String fileRead(@NonNull Context appContext, @NonNull String fileName) {
        InputStream inputStream = null;
        try {
            inputStream = appContext.getAssets().open(fileName);
            StringBuilder sb = new StringBuilder();
            byte[] buffer = new byte[READ_BUFFER_SIZE];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, len));
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 拷贝assets下的文件
     *
     * @param appContext   Application Context
     * @param srcFileName  assets下的文件名
     * @param destFilePath 文件存储路径
     * @return 是否拷贝成功
     */
    public static boolean fileCopy(@NonNull Context appContext, @NonNull String srcFileName, @NonNull String destFilePath) {
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            File destFile = new File(destFilePath);
            // 确保路径上的文件夹存在
            destFile.getParentFile().mkdirs();
            inputStream = appContext.getAssets().open(srcFileName);
            fos = new FileOutputStream(destFile);
            byte[] buffer = new byte[READ_BUFFER_SIZE];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
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
}
