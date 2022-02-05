/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class UtilsContentCacheNotifier extends ContentObserver {

    private static final char SPLIT_CHAR = UtilsContentConstants.SPLIT_CHAR;
    private static final int CONTENT_HEADER_LENGTH = "content://".length();

    private static final Uri URI_CACHE_BOOLEAN = UtilsContentConstants.URI_CACHE_BOOLEAN;
    private static final Uri URI_CACHE_INTEGER = UtilsContentConstants.URI_CACHE_INTEGER;
    private static final Uri URI_CACHE_LONG = UtilsContentConstants.URI_CACHE_LONG;
    private static final Uri URI_CACHE_STRING = UtilsContentConstants.URI_CACHE_STRING;
    private static final Uri URI_CACHE_OBJECT = UtilsContentConstants.URI_CACHE_OBJECT;

    private static final String BOOLEAN_CACHE_TABLE_NAME = UtilsContentConstants.BOOLEAN_CACHE_TABLE_NAME;
    private static final String INTEGER_CACHE_TABLE_NAME = UtilsContentConstants.INTEGER_CACHE_TABLE_NAME;
    private static final String LONG_CACHE_TABLE_NAME = UtilsContentConstants.LONG_CACHE_TABLE_NAME;
    private static final String STRING_CACHE_TABLE_NAME = UtilsContentConstants.STRING_CACHE_TABLE_NAME;
    private static final String OBJECT_CACHE_TABLE_NAME = UtilsContentConstants.OBJECT_CACHE_TABLE_NAME;

    private static final String ACTION_CACHE_INSERT = UtilsContentConstants.ACTION_CACHE_INSERT;
    private static final String ACTION_CACHE_UPDATE = UtilsContentConstants.ACTION_CACHE_UPDATE;
    private static final String ACTION_CACHE_DELETE = UtilsContentConstants.ACTION_CACHE_DELETE;

    private static final int BOOLEAN_TRUE = UtilsContentConstants.BOOLEAN_TRUE;

    private final Map<String, Boolean> mBooleanMap = new HashMap<>();
    private final Map<String, Integer> mIntegerMap = new HashMap<>();
    private final Map<String, Long> mLongMap = new HashMap<>();
    private final Map<String, String> mStringMap = new HashMap<>();
    private final Map<String, Object> mObjectMap = new HashMap<>();

    private final Map<String, List<UtilsContentCallback<Boolean>>> mBooleanCallbacksMap = new HashMap<>();
    private final Map<String, List<UtilsContentCallback<Integer>>> mIntegerCallbacksMap = new HashMap<>();
    private final Map<String, List<UtilsContentCallback<Long>>> mLongCallbacksMap = new HashMap<>();
    private final Map<String, List<UtilsContentCallback<String>>> mStringCallbacksMap = new HashMap<>();
    private final Map<String, List<UtilsContentCallback<Object>>> mObjectCallbacksMap = new HashMap<>();

    private static final HandlerThread sHandlerThread;

    static {
        sHandlerThread = new HandlerThread("UtilsContentCacheNotifierHandlerThread");
        sHandlerThread.start();
    }

    private UtilsContentCacheNotifier() {
        /*
         * 出于效率考虑，不使用Looper.getMainLooper()，
         * 若回调时，需要使用UI线程，则由回调接收者自行切换。
         */
        super(new Handler(sHandlerThread.getLooper()));
    }

    private static class Singleton {
        private static final UtilsContentCacheNotifier instance = new UtilsContentCacheNotifier();
    }

    static UtilsContentCacheNotifier get() {
        return Singleton.instance;
    }

    @Override
    public void onChange(boolean selfChange, @Nullable Uri uri) {
        if (uri == null) {
            throw new NullPointerException("uri is null");
        }
        String uriString = uri.toString();
        int start = uriString.indexOf(SPLIT_CHAR, CONTENT_HEADER_LENGTH);
        // /string/cache_insert/key/value/
        // /string/cache_delete/key/
        String[] splitList = UtilsContentEscape.split(uriString.substring(start));
        if (splitList.length != 3 && splitList.length != 4) {
            throw new IllegalArgumentException("splitList is invalid: " + Arrays.toString(splitList));
        }
        final String table = splitList[0];
        final String action = splitList[1];
        final String key = splitList[2];
        switch (action) {
            case ACTION_CACHE_DELETE:
                doCallbackOnDeleted(table, key);
                break;
            case ACTION_CACHE_INSERT:
            case ACTION_CACHE_UPDATE:
                final String value =  splitList[3];
                doCallbackOnChanged(table, key, value);
                break;
            default:
                throw new IllegalArgumentException("action : " + action + " is invalid");
        }
    }

    void registerContentObserver(@NonNull Context context) {
        context.getContentResolver().registerContentObserver(URI_CACHE_INTEGER, true, this);
        context.getContentResolver().registerContentObserver(URI_CACHE_LONG, true, this);
        context.getContentResolver().registerContentObserver(URI_CACHE_STRING, true, this);
        context.getContentResolver().registerContentObserver(URI_CACHE_BOOLEAN, true, this);
        context.getContentResolver().registerContentObserver(URI_CACHE_OBJECT, true, this);
    }

    void unregisterContentObserver(@NonNull Context context) {
        context.getContentResolver().unregisterContentObserver(this);
    }

    synchronized void registerBooleanCallback(@NonNull String key, @NonNull UtilsContentCallback<Boolean> callback) {
        registerCallback(key, callback, Boolean.class, mBooleanMap, mBooleanCallbacksMap);
    }

    synchronized void unregisterBooleanCallback(@NonNull String key, @NonNull UtilsContentCallback<Boolean> callback) {
        unregisterCallback(key, callback, mBooleanCallbacksMap);
    }

    synchronized void registerIntegerCallback(@NonNull String key, @NonNull UtilsContentCallback<Integer> callback) {
        registerCallback(key, callback, Integer.class, mIntegerMap, mIntegerCallbacksMap);
    }

    synchronized void unregisterIntegerCallback(@NonNull String key, @NonNull UtilsContentCallback<Integer> callback) {
        unregisterCallback(key, callback, mIntegerCallbacksMap);
    }

    synchronized void registerLongCallback(@NonNull String key, @NonNull UtilsContentCallback<Long> callback) {
        registerCallback(key, callback, Long.class, mLongMap, mLongCallbacksMap);
    }

    synchronized void unregisterLongCallback(@NonNull String key, @NonNull UtilsContentCallback<Long> callback) {
        unregisterCallback(key, callback, mLongCallbacksMap);
    }

    synchronized void registerStringCallback(@NonNull String key, @NonNull UtilsContentCallback<String> callback) {
        registerCallback(key, callback, String.class, mStringMap, mStringCallbacksMap);
    }

    synchronized void unregisterStringCallback(@NonNull String key, @NonNull UtilsContentCallback<String> callback) {
        unregisterCallback(key, callback, mStringCallbacksMap);
    }

    @SuppressWarnings("unchecked")
    private <T> void registerCallback(@NonNull String key, @NonNull UtilsContentCallback<T> callback,
                                      @NonNull Class<T> clazz, @NonNull Map<String, T> typeMap,
                                      @NonNull Map<String, List<UtilsContentCallback<T>>> typeCallbackMap) {
        List<UtilsContentCallback<T>> contentCallbacks = typeCallbackMap.get(key);
        if (contentCallbacks == null) {
            contentCallbacks = new ArrayList<>();
            contentCallbacks.add(callback);
            typeCallbackMap.put(key, contentCallbacks);
        } else {
            if (!contentCallbacks.contains(callback)) {
                // 不添加重复的callback
                contentCallbacks.add(callback);
            }
        }
        // 立即回调
        final T oldValue = typeMap.get(key);
        final T newValue;
        if (clazz.getName().equals(Boolean.class.getName())) {
            newValue = (T) UtilsContentCacheResolver.getBoolean(key);
        } else if (clazz.getName().equals(Integer.class.getName())) {
            newValue = (T) UtilsContentCacheResolver.getInt(key);
        } else if (clazz.getName().equals(Long.class.getName())) {
            newValue = (T) UtilsContentCacheResolver.getLong(key);
        } else if (clazz.getName().equals(String.class.getName())) {
            newValue = (T) UtilsContentCacheResolver.getString(key);
        } else {
            newValue = (T) UtilsContentCacheResolver.getObject(key);
        }
        // 若newValue为空，则忽略之
        if (newValue != null) {
            doRealCallbackOnChanged(callback, oldValue, newValue);
            typeMap.put(key, newValue);
        }
    }

    private <T> void unregisterCallback(@NonNull String key, @NonNull UtilsContentCallback<T> callback, @NonNull Map<String, List<UtilsContentCallback<T>>> typeCallbackMap) {
        List<UtilsContentCallback<T>> contentCallbacks = typeCallbackMap.get(key);
        if (contentCallbacks != null) {
            contentCallbacks.remove(callback);
            if (contentCallbacks.isEmpty()) {
                typeCallbackMap.remove(key);
            }
        }
    }

    private void doCallbackOnChanged(@NonNull String table, @NonNull String key, @NonNull String value) {
        switch (table) {
            case BOOLEAN_CACHE_TABLE_NAME: {
                Boolean oldValue = mBooleanMap.get(key);
                boolean newValue = Integer.parseInt(value) == BOOLEAN_TRUE;
                if (oldValue == null || oldValue != newValue) {
                    List<UtilsContentCallback<Boolean>> utilsContentCallbacks = mBooleanCallbacksMap.get(key);
                    if (utilsContentCallbacks != null) {
                        for (UtilsContentCallback<Boolean> utilsContentCallback : utilsContentCallbacks) {
                            doRealCallbackOnChanged(utilsContentCallback, oldValue, newValue);
                        }
                    }
                    mBooleanMap.put(key, newValue);
                }
            }
            break;
            case INTEGER_CACHE_TABLE_NAME: {
                Integer oldValue = mIntegerMap.get(key);
                int newValue = Integer.parseInt(value);
                if (oldValue == null || oldValue != newValue) {
                    List<UtilsContentCallback<Integer>> utilsContentCallbacks = mIntegerCallbacksMap.get(key);
                    if (utilsContentCallbacks != null) {
                        for (UtilsContentCallback<Integer> utilsContentCallback : utilsContentCallbacks) {
                            doRealCallbackOnChanged(utilsContentCallback, oldValue, newValue);
                        }
                    }
                    mIntegerMap.put(key, newValue);
                }
            }
            break;
            case LONG_CACHE_TABLE_NAME: {
                Long oldValue = mLongMap.get(key);
                long newValue = Long.parseLong(value);
                if (oldValue == null || oldValue != newValue) {
                    List<UtilsContentCallback<Long>> utilsContentCallbacks = mLongCallbacksMap.get(key);
                    if (utilsContentCallbacks != null) {
                        for (UtilsContentCallback<Long> utilsContentCallback : utilsContentCallbacks) {
                            doRealCallbackOnChanged(utilsContentCallback, oldValue, newValue);
                        }
                    }
                    mLongMap.put(key, newValue);
                }
            }
            break;
            case STRING_CACHE_TABLE_NAME: {
                String oldValue = mStringMap.get(key);
                @SuppressWarnings("UnnecessaryLocalVariable")/* 代码看上去和其他类似 */
                String newValue = value;
                if (oldValue == null || !oldValue.equals(newValue)) {
                    List<UtilsContentCallback<String>> utilsContentCallbacks = mStringCallbacksMap.get(key);
                    if (utilsContentCallbacks != null) {
                        for (UtilsContentCallback<String> utilsContentCallback : utilsContentCallbacks) {
                            doRealCallbackOnChanged(utilsContentCallback, oldValue, newValue);
                        }
                    }
                    mStringMap.put(key, newValue);
                }
            }
            break;
            case OBJECT_CACHE_TABLE_NAME: {
                Object oldValue = mObjectMap.get(key);
                Object newValue = UtilsContentSerializable.readObject(value);
                if (newValue == null) {
                    throw new NullPointerException("doCallbackOnChanged() OBJECT_CACHE_TABLE_NAME newValue is null");
                }
                if (oldValue == null || !oldValue.equals(newValue)) {
                    List<UtilsContentCallback<Object>> utilsContentCallbacks = mObjectCallbacksMap.get(key);
                    if (utilsContentCallbacks != null) {
                        for (UtilsContentCallback<Object> utilsContentCallback : utilsContentCallbacks) {
                            doRealCallbackOnChanged(utilsContentCallback, oldValue, newValue);
                        }
                    }
                }
            }
            break;
            default:
                throw new IllegalArgumentException("table: " + table + " is invalid");
        }
    }

    private void doCallbackOnDeleted(@NonNull String table, @NonNull String key) {
        switch (table) {
            case BOOLEAN_CACHE_TABLE_NAME: {
                mBooleanMap.remove(key);
                List<UtilsContentCallback<Boolean>> utilsContentCallbacks = mBooleanCallbacksMap.get(key);
                if (utilsContentCallbacks != null) {
                    for (UtilsContentCallback<Boolean> utilsContentCallback : utilsContentCallbacks) {
                        doRealCallbackOnDeleted(utilsContentCallback);
                    }
                }
            }
            break;
            case INTEGER_CACHE_TABLE_NAME: {
                mIntegerMap.remove(key);
                List<UtilsContentCallback<Integer>> utilsContentCallbacks = mIntegerCallbacksMap.get(key);
                if (utilsContentCallbacks != null) {
                    for (UtilsContentCallback<Integer> utilsContentCallback : utilsContentCallbacks) {
                        doRealCallbackOnDeleted(utilsContentCallback);
                    }
                }
            }
            break;
            case LONG_CACHE_TABLE_NAME: {
                mLongMap.remove(key);
                List<UtilsContentCallback<Long>> utilsContentCallbacks = mLongCallbacksMap.get(key);
                if (utilsContentCallbacks != null) {
                    for (UtilsContentCallback<Long> utilsContentCallback : utilsContentCallbacks) {
                        doRealCallbackOnDeleted(utilsContentCallback);
                    }
                }
            }
            break;
            case STRING_CACHE_TABLE_NAME: {
                mStringMap.remove(key);
                List<UtilsContentCallback<String>> utilsContentCallbacks = mStringCallbacksMap.get(key);
                if (utilsContentCallbacks != null) {
                    for (UtilsContentCallback<String> utilsContentCallback : utilsContentCallbacks) {
                        doRealCallbackOnDeleted(utilsContentCallback);
                    }
                }
            }
            break;
            case OBJECT_CACHE_TABLE_NAME: {
                mObjectMap.remove(key);
                List<UtilsContentCallback<Object>> utilsContentCallbacks = mObjectCallbacksMap.get(key);
                if (utilsContentCallbacks != null) {
                    for (UtilsContentCallback<Object> utilsContentCallback : utilsContentCallbacks) {
                        doRealCallbackOnDeleted(utilsContentCallback);
                    }
                }
            }
            break;
            default:
                throw new IllegalArgumentException("table: " + table + " is invalid");
        }
    }

    private <T> void doRealCallbackOnChanged(@NonNull UtilsContentCallback<T> callback, @Nullable T oldValue, @NonNull T newValue) {
        callback.onValueChanged(oldValue, newValue);
    }

    private <T> void doRealCallbackOnDeleted(UtilsContentCallback<T> callback) {
        callback.onValueDeleted();
    }
}
