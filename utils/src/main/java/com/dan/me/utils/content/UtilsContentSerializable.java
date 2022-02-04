/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import com.dan.me.utils.log.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UtilsContentSerializable {

    private static final String TAG = "UtilsContentSerializable";

    private static final LogUtils LOGGER = new LogUtils.Builder()
            .setSystemLogEnabled(true)
            .setGeneralSaveEnabled(true)
            .setSpecializedSaveEnabled(false)
            .create();

    @Nullable
    public static Object readObject(@NonNull String objectString) {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectString.getBytes(StandardCharsets.ISO_8859_1));
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.e(TAG, "readSerializable() e: " + e + ", serializableString: " + objectString);
            return null;
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException ignore) {
                }
            }
            try {
                byteArrayInputStream.close();
            } catch (IOException ignore) {
            }
        }
    }

    @Nullable
    public static String writeObject(@NonNull Object object) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            return bos.toString(StandardCharsets.ISO_8859_1.name());
        } catch (IOException e) {
            LOGGER.e(TAG, "writeSerializable() e: " + e + ", object: " + object);
            return null;
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException ignore) {
                }
            }
        }
    }

}
