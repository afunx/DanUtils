/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.SystemClock;

import com.dan.me.utils.log.LogUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UtilsContentProvider extends ContentProvider {

    private static final String TAG = "UtilsContentProvider";

    private static final LogUtils LOGGER = new LogUtils.Builder()
            .setSystemLogEnabled(true)
            .setGeneralSaveEnabled(true)
            .setSpecializedSaveEnabled(false)
            .create();

    private SQLiteDatabase mSQLiteDatabase;

    private static final String AUTHORITY = UtilsContentConstants.AUTHORITY;

    private static final String BOOLEAN_TABLE_NAME = UtilsContentConstants.BOOLEAN_TABLE_NAME;
    private static final String INTEGER_TABLE_NAME = UtilsContentConstants.INTEGER_TABLE_NAME;
    private static final String LONG_TABLE_NAME = UtilsContentConstants.LONG_TABLE_NAME;
    private static final String STRING_TABLE_NAME = UtilsContentConstants.STRING_TABLE_NAME;

    private static final int BOOLEAN_CODE = UtilsContentConstants.BOOLEAN_CODE;
    private static final int INTEGER_CODE = UtilsContentConstants.INTEGER_CODE;
    private static final int LONG_CODE = UtilsContentConstants.LONG_CODE;
    private static final int STRING_CODE = UtilsContentConstants.STRING_CODE;

    private static final String KEY_COLUMN_NAME = UtilsContentConstants.KEY_COLUMN_NAME;
    private static final String VALUE_COLUMN_NAME = UtilsContentConstants.VALUE_COLUMN_NAME;

    private static final char SPLIT_CHAR = UtilsContentConstants.SPLIT_CHAR;

    private static final String ACTION_INSERT = UtilsContentConstants.ACTION_INSERT;
    private static final String ACTION_DELETE = UtilsContentConstants.ACTION_DELETE;
    private static final String ACTION_UPDATE = UtilsContentConstants.ACTION_UPDATE;


    private static final UriMatcher sUriTableMatcher;

    static {
        sUriTableMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriTableMatcher.addURI(AUTHORITY, BOOLEAN_TABLE_NAME, BOOLEAN_CODE);
        sUriTableMatcher.addURI(AUTHORITY, INTEGER_TABLE_NAME, INTEGER_CODE);
        sUriTableMatcher.addURI(AUTHORITY, LONG_TABLE_NAME, LONG_CODE);
        sUriTableMatcher.addURI(AUTHORITY, STRING_TABLE_NAME, STRING_CODE);
    }

    @Override
    public boolean onCreate() {
        final long start = SystemClock.elapsedRealtime();
        Context context = getContext();
        assert context != null;
        UtilsContentDBHelper utilsContentDBHelper = new UtilsContentDBHelper(context);
        mSQLiteDatabase = utilsContentDBHelper.getWritableDatabase();
        final long consume = SystemClock.elapsedRealtime() - start;
        LOGGER.i(TAG, "onCreate() consume: " + consume + " ms");
        return true;
    }

    @Nullable
    @Override
    public synchronized Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getTableName(uri);
        return mSQLiteDatabase.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }



    @Nullable
    @Override
    public synchronized Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        assert  values != null;
        String key = values.getAsString(KEY_COLUMN_NAME);
        assert key != null;
        String tableName = getTableName(uri);
        final String action;
        if (isKeyExist(tableName, key)) {
            mSQLiteDatabase.update(tableName, values, KEY_COLUMN_NAME + "=?", new String[]{key});
            action = ACTION_UPDATE;
        } else {
            mSQLiteDatabase.insert(tableName, null, values);
            action = ACTION_INSERT;
        }
        // 通知插入新数据
        Context context = getContext();
        if (context != null) {
            String value = UtilsContentEscape.escape("" + values.get(VALUE_COLUMN_NAME));
            key = UtilsContentEscape.escape(key);
            Uri insertUri = Uri.parse(uri.toString() + SPLIT_CHAR + action + SPLIT_CHAR + key + SPLIT_CHAR + value + SPLIT_CHAR);
            context.getContentResolver().notifyChange(insertUri, null);
        }
        return uri;
    }

    @Override
    public synchronized int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        assert selectionArgs != null && selectionArgs[0] != null;
        String key = selectionArgs[0];
        String tableName = getTableName(uri);
        int delete = mSQLiteDatabase.delete(tableName, selection, selectionArgs);
        if (delete > 0) {
            // 通知删除数据
            Context context = getContext();
            if (context != null) {
                key = UtilsContentEscape.escape(key);
                Uri deleteUri = Uri.parse(uri.toString() + SPLIT_CHAR + ACTION_DELETE + SPLIT_CHAR + key + SPLIT_CHAR);
                context.getContentResolver().notifyChange(deleteUri, null);
            }
        }
        return delete;
    }

    @Override
    public synchronized int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    private String getTableName(Uri uri) {
        final String tableName;
        switch (sUriTableMatcher.match(uri)) {
            case INTEGER_CODE:
                tableName = INTEGER_TABLE_NAME;
                break;
            case LONG_CODE:
                tableName = LONG_TABLE_NAME;
                break;
            case STRING_CODE:
                tableName = STRING_TABLE_NAME;
                break;
            case BOOLEAN_CODE:
                tableName = BOOLEAN_TABLE_NAME;
                break;
            default:
                throw new NullPointerException("tableName is null");
        }
        return tableName;
    }

    private boolean isKeyExist(String tableName, String key) {
        Cursor cursor = mSQLiteDatabase.query(tableName, new String[]{KEY_COLUMN_NAME}, KEY_COLUMN_NAME + "=?", new String[]{key}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }
}
