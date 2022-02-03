/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import android.database.AbstractCursor;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class UtilsContentCacheNoDBHelper {

    private final Map<String, Integer> mBooleanMap = new HashMap<>();
    private final Map<String, Integer> mIntegerMap = new HashMap<>();
    private final Map<String, Long> mLongMap = new HashMap<>();
    private final Map<String, String> mStringMap = new HashMap<>();

    Cursor query(@NonNull UtilsContentTypeEnum contentTypeEnum, @NonNull String key) {
        switch (contentTypeEnum) {
            case CACHE_BOOLEAN:
                return getBooleanCursor(key);
            case CACHE_INTEGER:
                return getIntegerCursor(key);
            case CACHE_LONG:
                return getLongCursor(key);
            case CACHE_STRING:
                return getStringCursor(key);
            default:
                throw new IllegalArgumentException("contentTypeEnum: " + contentTypeEnum + " is invalid");
        }
    }

    void put(@NonNull UtilsContentTypeEnum contentTypeEnum, @NonNull String key, @NonNull Object value) {
        switch (contentTypeEnum) {
            case CACHE_BOOLEAN:
                putBoolean(key, (Integer) value);
                break;
            case CACHE_INTEGER:
                putInteger(key, (Integer) value);
                break;
            case CACHE_LONG:
                putLong(key, (Long) value);
                break;
            case CACHE_STRING:
                putString(key, (String) value);
                break;
            default:
                throw new IllegalArgumentException("contentTypeEnum: " + contentTypeEnum + " is invalid");
        }
    }

    int delete(@NonNull UtilsContentTypeEnum contentTypeEnum, @NonNull String key) {
        switch (contentTypeEnum) {
            case CACHE_BOOLEAN:
                return deleteBoolean(key) == null ? 0 : 1;
            case CACHE_INTEGER:
                return deleteInteger(key) == null ? 0 : 1;
            case CACHE_LONG:
                return deleteLong(key) == null ? 0 : 1;
            case CACHE_STRING:
                return deleteString(key) == null ? 0 : 1;
            default:
                throw new IllegalArgumentException("contentTypeEnum: " + contentTypeEnum + " is invalid");
        }
    }

    private void putBoolean(@NonNull String key, @NonNull Integer value) {
        mBooleanMap.put(key, value);
    }

    @Nullable
    private Integer deleteBoolean(@NonNull String key) {
        return mBooleanMap.remove(key);
    }

    @NonNull
    private Cursor getBooleanCursor(@NonNull String key) {
        return getCursor(key, Boolean.class);
    }

    private void putInteger(@NonNull String key, @NonNull Integer value) {
        mIntegerMap.put(key, value);
    }

    @Nullable
    private Integer deleteInteger(@NonNull String key) {
        return mIntegerMap.remove(key);
    }

    @NonNull
    private Cursor getIntegerCursor(@NonNull String key) {
        return getCursor(key, Integer.class);
    }

    private void putLong(@NonNull String key, @NonNull Long value) {
        mLongMap.put(key, value);
    }

    @Nullable
    private Long deleteLong(@NonNull String key) {
        return mLongMap.remove(key);
    }

    @NonNull
    private Cursor getLongCursor(@NonNull String key) {
        return getCursor(key, Long.class);
    }

    private void putString(@NonNull String key, @NonNull String value) {
        mStringMap.put(key, value);
    }

    @Nullable
    private String deleteString(@NonNull String key) {
        return mStringMap.remove(key);
    }

    @NonNull
    private Cursor getStringCursor(@NonNull String key) {
        return getCursor(key, String.class);
    }

    private Cursor getCursor(String key, Class<?> clazz) {
        final Object value;
        if (clazz.getName().equals(Integer.class.getName())) {
            value = mIntegerMap.get(key);
        } else if (clazz.getName().equals(Long.class.getName())) {
            value = mLongMap.get(key);
        } else if (clazz.getName().equals(String.class.getName())) {
            value = mStringMap.get(key);
        } else if (clazz.getName().equals(Boolean.class.getName())) {
            value = mBooleanMap.get(key);
        } else {
            throw new IllegalArgumentException("clazz: " + clazz + " is invalid");
        }
        final int count = value == null ? 0 : 1;

        return new AbstractCursor() {

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public String[] getColumnNames() {
                return new String[]{""};
            }

            @Override
            public String getString(int column) {
                assert value != null;
                return value.toString();
            }

            @Override
            public short getShort(int columnIndex) {
                throw new UnsupportedOperationException("getShort()");
            }

            @Override
            public int getInt(int columnIndex) {
                if (!(value instanceof Integer)) throw new AssertionError();
                return (Integer) value;
            }

            @Override
            public long getLong(int columnIndex) {
                if (!(value instanceof Long)) throw new AssertionError();
                return (Long) value;
            }

            @Override
            public float getFloat(int columnIndex) {
                throw new UnsupportedOperationException("getFloat()");
            }

            @Override
            public double getDouble(int columnIndex) {
                throw new UnsupportedOperationException("getDouble()");
            }

            @Override
            public boolean isNull(int column) {
                throw new UnsupportedOperationException("isNull(()");
            }
        };
    }
}
