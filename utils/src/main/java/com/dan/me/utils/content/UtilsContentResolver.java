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

class UtilsContentResolver {

    private static final String TAG = "UtilsContentResolver";

    private static final int BOOLEAN_TRUE = UtilsContentConstants.BOOLEAN_TRUE;
    private static final int BOOLEAN_FALSE = UtilsContentConstants.BOOLEAN_FALSE;

    private static final Uri URI_BOOLEAN = UtilsContentConstants.URI_BOOLEAN;
    private static final Uri URI_INTEGER = UtilsContentConstants.URI_INTEGER;
    private static final Uri URI_LONG = UtilsContentConstants.URI_LONG;
    private static final Uri URI_STRING = UtilsContentConstants.URI_STRING;

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
    static void putBoolean(String key, boolean value) {
        LOGGER.i(TAG, "putBoolean() key: " + key + ", value: " + value);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN_NAME, key);
        values.put(VALUE_COLUMN_NAME, value ? BOOLEAN_TRUE : BOOLEAN_FALSE);
        sAppContext.getContentResolver().insert(URI_BOOLEAN, values);
    }

    static Boolean getBoolean(String key) {
        Boolean value = null;
        Cursor cursor = sAppContext.getContentResolver().query(URI_BOOLEAN, new String[]{VALUE_COLUMN_NAME}, KEY_COLUMN_NAME + "=?", new String[]{key}, null);
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getInt(FIRST_COLUMN_INDEX/*只选了一列，故直接使用*/) == BOOLEAN_TRUE;
            cursor.close();
        }
        return value;
    }

    static void deleteBoolean(String key) {
        LOGGER.i(TAG, "deleteBoolean() key: " + key);
        sAppContext.getContentResolver().delete(URI_BOOLEAN, KEY_COLUMN_NAME + "=?", new String[]{key});
    }


    // int
    static void putInt(String key, int value) {
        LOGGER.i(TAG, "putInt() key: " + key + ", value: " + value);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN_NAME, key);
        values.put(VALUE_COLUMN_NAME, value);
        sAppContext.getContentResolver().insert(URI_INTEGER, values);
    }

    static Integer getInt(String key) {
        Integer value = null;
        Cursor cursor = sAppContext.getContentResolver().query(URI_INTEGER, new String[]{VALUE_COLUMN_NAME}, KEY_COLUMN_NAME + "=?", new String[]{key}, null);
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getInt(FIRST_COLUMN_INDEX/*只选了一列，故直接使用*/);
            cursor.close();
        }
        return value;
    }

    static void deleteInt(String key) {
        LOGGER.i(TAG, "deleteInt() key: " + key);
        sAppContext.getContentResolver().delete(URI_INTEGER, KEY_COLUMN_NAME + "=?", new String[]{key});
    }


    // long
    static void putLong(String key, long value) {
        LOGGER.i(TAG, "putLong() key: " + key + ", value: " + value);
        ContentValues values = new ContentValues();
        values.put(KEY_COLUMN_NAME, key);
        values.put(VALUE_COLUMN_NAME, value);
        sAppContext.getContentResolver().insert(URI_LONG, values);
    }

    static Long getLong(String key) {
        Long value = null;
        Cursor cursor = sAppContext.getContentResolver().query(URI_LONG, new String[]{VALUE_COLUMN_NAME}, KEY_COLUMN_NAME + "=?", new String[]{key}, null);
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getLong(FIRST_COLUMN_INDEX/*只选了一列，故直接使用*/);
            cursor.close();
        }
        return value;
    }

    static void deleteLong(String key) {
        LOGGER.i(TAG, "deleteLong() key: " + key);
        sAppContext.getContentResolver().delete(URI_LONG, KEY_COLUMN_NAME + "=?", new String[]{key});
    }


    // String
    static void putString(String key, String value) {
        LOGGER.i(TAG, "putString() key: " + key + ", value: " + value);
        if (value == null) {
            // 如果value为null，就认为是要删除这个key
            deleteString(key);
        } else {
            ContentValues values = new ContentValues();
            values.put(KEY_COLUMN_NAME, key);
            values.put(VALUE_COLUMN_NAME, value);
            sAppContext.getContentResolver().insert(URI_STRING, values);
        }
    }

    static String getString(String key) {
        String value = null;
        Cursor cursor = sAppContext.getContentResolver().query(URI_STRING, new String[]{VALUE_COLUMN_NAME}, KEY_COLUMN_NAME + "=?", new String[]{key}, null);
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getString(FIRST_COLUMN_INDEX/*只选了一列，故直接使用*/);
            cursor.close();
        }
        return value;
    }

    static void deleteString(String key) {
        LOGGER.i(TAG, "deleteString() key: " + key);
        sAppContext.getContentResolver().delete(URI_STRING, KEY_COLUMN_NAME + "=?", new String[]{key});
    }

}
