/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.io;

import com.dan.me.utils.bytes.BytesUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FileUtils {

    private static final int READ_BUFFER_SIZE = 1024;

    private static final int COPY_FILE_BUFFER_SIZE = 1024;

    private static final int MD5SUM_BUFFER_SIZE = 1024;

    /**
     * 读本地文件内容（仅适合读取文件大小不大的文件｝
     *
     * @param filePath 文件路径
     * @return 被读的文件内容或null（若文件不存在）
     */
    @Nullable
    public static String fileRead(@NonNull String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[READ_BUFFER_SIZE];
        FileReader fileReader = null;
        try {
            int len;
            fileReader = new FileReader(filePath);
            while ((len = fileReader.read(buffer)) != -1) {
                sb.append(buffer, 0, len);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static void mkdirs4parent(@NonNull String filePath) {
        File parentFile = new File(filePath).getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            boolean suc = parentFile.mkdirs();
            if (!suc) {
                throw new IllegalArgumentException("fail to mkdirs for parentFile: " + parentFile.getAbsolutePath());
            }
        }
    }

    /**
     * 写内容到本地文件
     *
     * @param filePath 文件路径
     * @param content  待写内容
     * @return 是否成功
     */
    public static boolean fileWrite(@NonNull String filePath, @NonNull String content) {
        mkdirs4parent(filePath);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, false);
            fileWriter.append(content);
            fileWriter.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 文件拷贝，仅支持文件拷贝，不支持文件夹拷贝
     *
     * @param srcFilePath      源文件路径
     * @param destTempFilePath 目标文件临时路径
     * @param destFilePath     目标文件路径
     * @return 是否拷贝成功
     */
    public static boolean fileCopy(@NonNull String srcFilePath, @NonNull String destTempFilePath, @NonNull String destFilePath) {
        File srcFile = new File(srcFilePath);
        File destTempFile = new File(destTempFilePath);
        File destFile = new File(destFilePath);
        if (!srcFile.exists() || srcFile.isDirectory() || destTempFile.isDirectory() || destFile.isDirectory()) {
            return false;
        }

        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            // 确保destTempFile旧文件被删除，文件路径可达
            if (destTempFile.exists()) {
                destTempFile.delete();
            } else {
                destTempFile.getParentFile().mkdirs();
            }
            // 确保destFile旧文件被删除，文件路径可达
            if (destFile.exists()) {
                destFile.delete();
            } else {
                destFile.getParentFile().mkdirs();
            }

            // 先把srcFile拷贝到destTempFile
            fileInputStream = new FileInputStream(srcFile);
            fileOutputStream = new FileOutputStream(destTempFile);
            byte[] buffer = new byte[COPY_FILE_BUFFER_SIZE];

            int len;
            while ((len = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            // 再把destTempFile重命名为destFile并删除destTempFile
            return destTempFile.renameTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 最终删除中间文件
            destTempFile.delete();
        }
        return false;
    }

    /**
     * 文件移动
     *
     * @param srcFilePath  源文件路径
     * @param destFilePath 目标文件路径
     * @return 是否移动成功
     */
    public static boolean fileMove(@NonNull String srcFilePath, @NonNull String destFilePath) {
        File srcFile = new File(srcFilePath);
        if (!srcFile.exists()) {
            return false;
        }
        File destFile = new File(destFilePath);
        File destParentFile = destFile.getParentFile();
        if (destParentFile != null && !destParentFile.exists()) {
            boolean suc = destParentFile.mkdirs();
            if (!suc) {
                throw new IllegalArgumentException("fail to mkdirs for destParentFile: " + destParentFile.getAbsolutePath());
            }
        }
        return srcFile.renameTo(destFile);
    }

    /**
     * 删除文件夹子文件（也可删除其自身）
     *
     * @param dirPath   文件夹路径
     * @param deleteDir 是否删除文件夹本身
     * @return  是否删除成功（若文件夹不存在，也算是删除成功）
     * @throws IllegalArgumentException 参数异常
     */
    public static boolean dirDelete(@NonNull String dirPath, boolean deleteDir) {
        return dirDelete(dirPath, deleteDir, null);
    }

    /**
     * 删除文件夹子文件（也可删除其自身）
     *
     * @param dirPath    文件夹路径
     * @param deleteDir  是否删除文件夹本身
     * @param fileFilter 文件过滤器（若是空或filePath为文件路径，则认为文件均符合要求）
     * @return 是否删除成功（若文件夹不存在，也算是删除成功）
     * @throws IllegalArgumentException 参数异常
     */
    public static boolean dirDelete(@NonNull String dirPath, boolean deleteDir, @Nullable FileFilter fileFilter) {
        File dirFile = new File(dirPath);
        // 文件夹不存在即认为删除成功
        if (!dirFile.exists()) {
            return true;
        }
        if (!dirFile.isDirectory()) {
            throw new IllegalArgumentException("dirPath: " + dirPath + " isn't directory");
        }
        return _deleteFilesAtDir(new File(dirPath), deleteDir, fileFilter);
    }

    private static boolean _deleteFilesAtDir(@NonNull File dirFile, boolean deleteDir, @Nullable FileFilter fileFilter) {
        if (dirFile.exists()) {
            // 只要中间环节存在一个失败，则认为其失败
            boolean fail = false;
            File[] files = dirFile.listFiles(fileFilter);
            if (files != null && files.length > 0) {
                for (File file : files) {
                    final boolean suc;
                    if (file.isDirectory()) {
                        suc = _deleteFilesAtDir(file, true, fileFilter);
                        if (!fail && !suc) {
                            fail = true;
                        }
                    } else {
                        suc = file.delete();
                        if (!fail && !suc) {
                            fail = true;
                        }
                    }
                }
            }

            if (deleteDir && Objects.requireNonNull(dirFile.listFiles()).length == 0/*只有文件夹下无文件时，才删除文件夹本身*/) {
                boolean suc = dirFile.delete();
                if (!fail && !suc) {
                    fail = true;
                }
            }
            return !fail;
        } else {
            throw new IllegalArgumentException("dirFile: " + dirFile.getAbsolutePath() + " isn't exist");
        }
    }

    /**
     * 删除文件及其子文件
     *
     * @param filePath 文件路径（即可以是文件，也可以是文件夹）
     * @return 是否删除成功（若文件不存在，也算是删除成功）
     * @throws IllegalArgumentException 参数异常
     */
    public static boolean fileDelete(@NonNull String filePath) {
        return fileDelete(filePath, null);
    }

    /**
     * 删除符合要求的文件及其子文件
     *
     * @param filePath   文件路径（即可以是文件，也可以是文件夹）
     * @param fileFilter 文件过滤器（若是空或filePath为文件路径，则认为文件均符合要求）
     * @return 是否删除成功（若文件不存在，也算是删除成功）
     * @throws IllegalArgumentException 参数异常
     */
    public static boolean fileDelete(@NonNull String filePath, @Nullable FileFilter fileFilter) {
        File file = new File(filePath);
        // 文件不存在即认为删除成功
        if (!file.exists()) {
            return true;
        }
        if (file.isDirectory()) {
            return dirDelete(filePath, true, fileFilter);
        } else if (file.isFile()) {
            if (fileFilter != null) {
                if (fileFilter.accept(file)) {
                    return file.delete();
                } else {
                    // 虽然不知道为何要删除一个文件（非文件夹），但其又不符合FileFilter，
                    // 但是，还是按照调用者到意图处理，按照定义也算是成功。
                    return true;
                }
            } else {
                return file.delete();
            }
        } else {
            // 既不是文件夹，也不是文件，就算其失败
            return false;
        }
    }

    private static List<File> _fileList(@NonNull File dirFile) {
        List<File> fileList = new ArrayList<>();
        for (File file : Objects.requireNonNull(dirFile.listFiles())) {
            if (file.isFile()) {
                fileList.add(file);
            } else if (file.isDirectory()) {
                fileList.addAll(_fileList(file));
            } else {
                throw new IllegalArgumentException("dirFile: " + dirFile.getAbsolutePath() + " is invalid");
            }
        }
        return fileList;
    }

    /**
     * 列出文件路径下的全部文件（不包含文件夹)
     *
     * @param filePath 文件路径
     * @return 文件路径下的全部文件（不包含文件夹)
     * @throws IllegalArgumentException 参数异常
     */
    @NonNull
    public static List<File> fileList(@NonNull String filePath) {
        List<File> fileList = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return fileList;
        } else {
            if (file.isFile()) {
                fileList.add(file);
                return fileList;
            } else if (file.isDirectory()) {
                return _fileList(file);
            } else {
                throw new IllegalArgumentException("filePath: " + filePath + " is invalid");
            }
        }
    }

    /**
     * 文件添加内容，若文件不存在，则创建之，若已存在，则在其后继续写
     *
     * @param filePath 文件路径
     * @param content  待写内容
     * @return 是否成功
     */
    public static boolean fileAppend(@NonNull String filePath, @NonNull String content) {
        mkdirs4parent(filePath);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filePath, true);
            fileWriter.append(content);
            fileWriter.flush();
            return true;
        } catch (IOException ignore) {
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 计算文件MD5
     *
     * @param filePath 文件路径
     * @return 文件MD5或null
     */
    @Nullable
    public static String fileMD5(@NonNull String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[MD5SUM_BUFFER_SIZE];
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            int len;
            while ((len = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, len);
            }
            return BytesUtils.bytes2hexString(md5.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long _dirDiskSpace(@NonNull File dirFile) {
        if (!dirFile.isDirectory()) {
            return 0;
        }
        long diskSpace = 0;
        File[] files = dirFile.listFiles();
        if (files == null) {
            return 0;
        }
        for (File file : files) {
            if (file.isFile()) {
                diskSpace += file.length();
            } else if (file.isDirectory()) {
                diskSpace += _dirDiskSpace(dirFile);
            }
        }
        return diskSpace;
    }

    /**
     * 获取手机文件夹及其子文件夹占用磁盘空间大小
     *
     * @param dirPath 文件夹路径
     * @return 占用磁盘空间大小，单位：字节
     */
    public static long dirDiskSpace(@NonNull String dirPath) {
        File dirFile = new File(dirPath);
        return _dirDiskSpace(dirFile);
    }

    private static void _dirEarliestModifyFiles(@NonNull File dirFile, @NonNull PriorityQueue<File> filePriorityQueue) {
        if (!dirFile.isDirectory()) {
            return;
        }
        File[] files = dirFile.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile()) {
                filePriorityQueue.offer(file);
            } else if (file.isDirectory()) {
                _dirEarliestModifyFiles(file, filePriorityQueue);
            }
        }
    }

    private static List<File> _dirEarliestModifyFiles(@NonNull File dirFile, long diskSpace) {
        Comparator<File> comparator = (o1, o2) -> Long.compare(o1.lastModified(), o2.lastModified());
        PriorityQueue<File> priorityQueue = new PriorityQueue<>(16, comparator);
        _dirEarliestModifyFiles(dirFile, priorityQueue);
        long curDiskSpace = 0;
        List<File> fileList = new ArrayList<>();
        while (curDiskSpace < diskSpace && !priorityQueue.isEmpty()) {
            File file = priorityQueue.poll();
            fileList.add(file);
            curDiskSpace += file.length();
        }
        return fileList;
    }

    /**
     * 获取手机文件夹及其子文件夹最早修改的文件列表
     *
     * @param dirPath   文件夹路径
     * @param diskSpace 占用磁盘空间大小
     * @return 最早修改的文件列表（首次大于等于diskSpace参数）
     */
    @NonNull
    public static List<File> dirEarliestModifyFiles(@NonNull String dirPath, long diskSpace) {
        return _dirEarliestModifyFiles(new File(dirPath), diskSpace);
    }
}
