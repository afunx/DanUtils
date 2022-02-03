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

    private UtilsContentCacheNoDBHelper mCacheNoDBHelper;

    private static final String AUTHORITY = UtilsContentConstants.AUTHORITY;

    private static final String BOOLEAN_TABLE_NAME = UtilsContentConstants.BOOLEAN_DB_TABLE_NAME;
    private static final String INTEGER_TABLE_NAME = UtilsContentConstants.INTEGER_DB_TABLE_NAME;
    private static final String LONG_TABLE_NAME = UtilsContentConstants.LONG_DB_TABLE_NAME;
    private static final String STRING_TABLE_NAME = UtilsContentConstants.STRING_DB_TABLE_NAME;

    private static final int BOOLEAN_CODE = UtilsContentConstants.BOOLEAN_CODE;
    private static final int INTEGER_CODE = UtilsContentConstants.INTEGER_CODE;
    private static final int LONG_CODE = UtilsContentConstants.LONG_CODE;
    private static final int STRING_CODE = UtilsContentConstants.STRING_CODE;

    private static final String KEY_COLUMN_NAME = UtilsContentConstants.KEY_COLUMN_NAME;
    private static final String VALUE_COLUMN_NAME = UtilsContentConstants.VALUE_COLUMN_NAME;

    private static final String KEY_CACHE_COLUMN_NAME = UtilsContentConstants.KEY_CACHE_COLUMN_NAME;
    private static final String VALUE_CACHE_COLUMN_NAME = UtilsContentConstants.VALUE_CACHE_COLUMN_NAME;

    private static final char SPLIT_CHAR = UtilsContentConstants.SPLIT_CHAR;

    private static final String ACTION_DB_INSERT = UtilsContentConstants.ACTION_DB_INSERT;
    private static final String ACTION_DB_DELETE = UtilsContentConstants.ACTION_DB_DELETE;
    private static final String ACTION_DB_UPDATE = UtilsContentConstants.ACTION_DB_UPDATE;
    private static final String ACTION_CACHE_INSERT = UtilsContentConstants.ACTION_CACHE_INSERT;
    private static final String ACTION_CACHE_DELETE = UtilsContentConstants.ACTION_CACHE_DELETE;
    private static final String ACTION_CACHE_UPDATE = UtilsContentConstants.ACTION_CACHE_UPDATE;

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
        mCacheNoDBHelper = new UtilsContentCacheNoDBHelper();
        final long consume = SystemClock.elapsedRealtime() - start;
        LOGGER.i(TAG, "onCreate() consume: " + consume + " ms");
        return true;
    }

    @NonNull
    private Cursor queryCache(@NonNull UtilsContentTypeEnum contentTypeEnum, @NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        assert selectionArgs != null;
        String key = selectionArgs[0];
        assert key != null;
        return mCacheNoDBHelper.query(contentTypeEnum, key);
    }

    @Nullable
    private Cursor queryDB(@NonNull UtilsContentTypeEnum contentTypeEnum, @NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return mSQLiteDatabase.query(contentTypeEnum.getTableName(), projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Nullable
    @Override
    public synchronized Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        UtilsContentTypeEnum contentTypeEnum = getContentTypeEnum(uri);
        if (contentTypeEnum.isCache()) {
            return queryCache(contentTypeEnum, uri, projection, selection, selectionArgs, sortOrder);
        } else {
            return queryDB(contentTypeEnum, uri, projection, selection, selectionArgs, sortOrder);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    private boolean isKeyExistInCache(@NonNull UtilsContentTypeEnum contentTypeEnum, @NonNull String key) {
        Cursor cursor = mCacheNoDBHelper.query(contentTypeEnum, key);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private String insertCache(@NonNull UtilsContentTypeEnum contentTypeEnum, @NonNull ContentValues values) {
        String key = values.getAsString(KEY_CACHE_COLUMN_NAME);
        Object value = values.get(VALUE_CACHE_COLUMN_NAME);
        final String action;
        if (isKeyExistInCache(contentTypeEnum, key)) {
            action = ACTION_CACHE_UPDATE;
        } else {
            action = ACTION_CACHE_INSERT;
        }
        mCacheNoDBHelper.put(contentTypeEnum, key, value);
        return action;
    }

    private boolean isKeyExistInDB(@NonNull String tableName, @NonNull String key) {
        Cursor cursor = mSQLiteDatabase.query(tableName, new String[]{KEY_COLUMN_NAME}, KEY_COLUMN_NAME + "=?", new String[]{key}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }

    private String insertDB(@NonNull UtilsContentTypeEnum contentTypeEnum, @NonNull ContentValues values) {
        String key = values.getAsString(KEY_COLUMN_NAME);
        String tableName = contentTypeEnum.getTableName();
        final String action;
        if (isKeyExistInDB(tableName, key)) {
            mSQLiteDatabase.update(tableName, values, KEY_COLUMN_NAME + "=?", new String[]{key});
            action = ACTION_DB_UPDATE;
        } else {
            mSQLiteDatabase.insert(tableName, null, values);
            action = ACTION_DB_INSERT;
        }
        return action;
    }

    @Nullable
    @Override
    public synchronized Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        assert values != null;
        final boolean isCache;
        String key = values.getAsString(KEY_COLUMN_NAME);
        if (key == null) {
            isCache = true;
            key = values.getAsString(KEY_CACHE_COLUMN_NAME);
        } else {
            isCache = false;
        }
        assert key != null;
        UtilsContentTypeEnum contentTypeEnum = getContentTypeEnum(uri);
        final String action;
        if (contentTypeEnum.isCache()) {
            action = insertCache(contentTypeEnum, values);
        } else {
            action = insertDB(contentTypeEnum, values);
        }
        // 通知插入新数据
        Context context = getContext();
        if (context != null) {
            String value = UtilsContentEscape.escape("" + (isCache ? values.get(VALUE_CACHE_COLUMN_NAME) : values.get(VALUE_COLUMN_NAME)));
            key = UtilsContentEscape.escape(key);
            Uri insertUri = Uri.parse(uri.toString() + SPLIT_CHAR + action + SPLIT_CHAR + key + SPLIT_CHAR + value + SPLIT_CHAR);
            context.getContentResolver().notifyChange(insertUri, null);
        }
        return uri;
    }

    private int deleteCache(@NonNull UtilsContentTypeEnum contentTypeEnum, @NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        assert selectionArgs != null;
        String key = selectionArgs[0];
        assert key != null;
        return mCacheNoDBHelper.delete(contentTypeEnum, key);
    }

    private int deleteDB(@NonNull UtilsContentTypeEnum contentTypeEnum, @NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        assert selectionArgs != null && selectionArgs[0] != null;
        String tableName = contentTypeEnum.getTableName();
        return mSQLiteDatabase.delete(tableName, selection, selectionArgs);
    }

    @Override
    public synchronized int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        assert selectionArgs != null && selectionArgs[0] != null;
        String key = selectionArgs[0];
        UtilsContentTypeEnum contentTypeEnum = getContentTypeEnum(uri);
        final int delete;
        final String action;
        if (contentTypeEnum.isCache()) {
            delete = deleteCache(contentTypeEnum, uri, selection, selectionArgs);
            action = ACTION_CACHE_DELETE;
        } else {
            delete = deleteDB(contentTypeEnum, uri, selection, selectionArgs);
            action = ACTION_DB_DELETE;
        }
        if (delete > 0) {
            // 通知删除数据
            Context context = getContext();
            if (context != null) {
                key = UtilsContentEscape.escape(key);
                Uri deleteUri = Uri.parse(uri.toString() + SPLIT_CHAR + action + SPLIT_CHAR + key + SPLIT_CHAR);
                context.getContentResolver().notifyChange(deleteUri, null);
            }
        }
        return delete;
    }

    @Override
    public synchronized int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    private UtilsContentTypeEnum getContentTypeEnum(@NonNull Uri uri) {
        // path: /long_db
        String tableName = uri.getPath().substring(1);
        return UtilsContentTypeEnum.parseTableName(tableName);
    }
}
