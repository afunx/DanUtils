/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.dan.me.utils.log.LogUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class UtilsContentResolver {

    private static final String TAG = "UtilsContentResolver";

    private static final int BOOLEAN_TRUE = UtilsContentConstants.BOOLEAN_TRUE;
    private static final int BOOLEAN_FALSE = UtilsContentConstants.BOOLEAN_FALSE;

    private static final Uri URI_DB_BOOLEAN = UtilsContentConstants.URI_DB_BOOLEAN;
    private static final Uri URI_DB_INTEGER = UtilsContentConstants.URI_DB_INTEGER;
    private static final Uri URI_DB_LONG = UtilsContentConstants.URI_DB_LONG;
    private static final Uri URI_DB_STRING = UtilsContentConstants.URI_DB_STRING;
    private static final Uri URI_DB_OBJECT = UtilsContentConstants.URI_DB_OBJECT;

    private static final String KEY_COLUMN_NAME = UtilsContentConstants.KEY_COLUMN_NAME;
    private static final String VALUE_COLUMN_NAME = UtilsContentConstants.VALUE_COLUMN_NAME;

    private static final int FIRST_COLUMN_INDEX = 0;

    private static final LogUtils LOGGER = new LogUtils.Builder()
            .setSystemLogEnabled(true)
            .setGeneralSaveEnabled(true)
            .setSpecializedSaveEnabled(false)
            .create();

    private static Context sAppContext;

    static void setAppContent(@NonNull Context appContent) {
        sAppContext = appContent.getApplicationContext();
    }


    // boolean
    static void putBoolean(@NonNull String key, boolean value) {
        LOGGER.i(TAG, "putBoolean() key: " + key + ", value: " + value);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN_NAME, key);
        values.put(VALUE_COLUMN_NAME, value ? BOOLEAN_TRUE : BOOLEAN_FALSE);
        sAppContext.getContentResolver().insert(URI_DB_BOOLEAN, values);
    }

    @Nullable
    static Boolean getBoolean(@NonNull String key) {
        Boolean value = null;
        Cursor cursor = sAppContext.getContentResolver().query(URI_DB_BOOLEAN, new String[]{VALUE_COLUMN_NAME}, KEY_COLUMN_NAME + "=?", new String[]{key}, null);
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getInt(FIRST_COLUMN_INDEX/*只选了一列，故直接使用*/) == BOOLEAN_TRUE;
            cursor.close();
        }
        return value;
    }

    static void deleteBoolean(@NonNull String key) {
        LOGGER.i(TAG, "deleteBoolean() key: " + key);
        sAppContext.getContentResolver().delete(URI_DB_BOOLEAN, KEY_COLUMN_NAME + "=?", new String[]{key});
    }


    // int
    static void putInt(@NonNull String key, int value) {
        LOGGER.i(TAG, "putInt() key: " + key + ", value: " + value);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN_NAME, key);
        values.put(VALUE_COLUMN_NAME, value);
        sAppContext.getContentResolver().insert(URI_DB_INTEGER, values);
    }

    @Nullable
    static Integer getInt(@NonNull String key) {
        Integer value = null;
        Cursor cursor = sAppContext.getContentResolver().query(URI_DB_INTEGER, new String[]{VALUE_COLUMN_NAME}, KEY_COLUMN_NAME + "=?", new String[]{key}, null);
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getInt(FIRST_COLUMN_INDEX/*只选了一列，故直接使用*/);
            cursor.close();
        }
        return value;
    }

    static void deleteInt(@NonNull String key) {
        LOGGER.i(TAG, "deleteInt() key: " + key);
        sAppContext.getContentResolver().delete(URI_DB_INTEGER, KEY_COLUMN_NAME + "=?", new String[]{key});
    }


    // long
    static void putLong(@NonNull String key, long value) {
        LOGGER.i(TAG, "putLong() key: " + key + ", value: " + value);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN_NAME, key);
        values.put(VALUE_COLUMN_NAME, value);
        sAppContext.getContentResolver().insert(URI_DB_LONG, values);
    }

    @Nullable
    static Long getLong(@NonNull String key) {
        Long value = null;
        Cursor cursor = sAppContext.getContentResolver().query(URI_DB_LONG, new String[]{VALUE_COLUMN_NAME}, KEY_COLUMN_NAME + "=?", new String[]{key}, null);
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getLong(FIRST_COLUMN_INDEX/*只选了一列，故直接使用*/);
            cursor.close();
        }
        return value;
    }

    static void deleteLong(@NonNull String key) {
        LOGGER.i(TAG, "deleteLong() key: " + key);
        sAppContext.getContentResolver().delete(URI_DB_LONG, KEY_COLUMN_NAME + "=?", new String[]{key});
    }


    // String
    static void putString(@NonNull String key, @NonNull String value) {
        LOGGER.i(TAG, "putString() key: " + key + ", value: " + value);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN_NAME, key);
        values.put(VALUE_COLUMN_NAME, value);
        sAppContext.getContentResolver().insert(URI_DB_STRING, values);
    }

    @Nullable
    static String getString(@NonNull String key) {
        String value = null;
        Cursor cursor = sAppContext.getContentResolver().query(URI_DB_STRING, new String[]{VALUE_COLUMN_NAME}, KEY_COLUMN_NAME + "=?", new String[]{key}, null);
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getString(FIRST_COLUMN_INDEX/*只选了一列，故直接使用*/);
            cursor.close();
        }
        return value;
    }

    static void deleteString(@NonNull String key) {
        LOGGER.i(TAG, "deleteString() key: " + key);
        sAppContext.getContentResolver().delete(URI_DB_STRING, KEY_COLUMN_NAME + "=?", new String[]{key});
    }


    // Object
    static void putObject(@NonNull String key, @NonNull Object value) {
        LOGGER.i(TAG, "putObject() key: " + key + ", value: " + value);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN_NAME, key);
        String objectString = UtilsContentSerializable.writeObject(value);
        if (objectString == null) {
            throw new NullPointerException("putObject() objectString is null");
        }
        values.put(VALUE_COLUMN_NAME, objectString);
        sAppContext.getContentResolver().insert(URI_DB_OBJECT, values);
    }

    @Nullable
    static Object getObject(@NonNull String key) {
        String value = null;
        Cursor cursor = sAppContext.getContentResolver().query(URI_DB_OBJECT, new String[]{VALUE_COLUMN_NAME}, KEY_COLUMN_NAME + "=?", new String[]{key}, null);
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getString(FIRST_COLUMN_INDEX/*只选了一列，故直接使用*/);
            cursor.close();
        }
        return value == null ? null : UtilsContentSerializable.readObject(value);
    }

    static void deleteObject(@NonNull String key) {
        LOGGER.i(TAG, "deleteObject() key: " + key);
        sAppContext.getContentResolver().delete(URI_DB_OBJECT, KEY_COLUMN_NAME + "=?", new String[]{key});
    }
}
