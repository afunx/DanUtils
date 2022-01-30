/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.dan.me.utils.io.FileUtils;
import com.dan.me.utils.log.LogUtils;
import com.dan.me.utils.log.LogUtilsOptions;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.*;

@SuppressWarnings({"unchecked", "ConstantConditions", "SameParameterValue"})
public class UtilsContentHelperTest {

    private static final String TAG = "UtilsContentHelperTest";

    private static final String sBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/content/";

    @BeforeClass
    public static void start() {
        Log.i(TAG, "start() sBasePath: " + sBasePath);
        // 测试开始前，先删除全部文件
        FileUtils.dirDelete(sBasePath, true);

        Context appContext = ApplicationProvider.getApplicationContext();
        LogUtilsOptions logUtilsOptions = new LogUtilsOptions.Builder()
                .setBasePath(sBasePath)
                .setGeneralFileMaxSize(24 * 1024/* 24K */)
                .setSpecializedFileMaxSize(24 * 1024/* 24K */)
                .create();
        LogUtils.setLogUtilsOptions(appContext, logUtilsOptions);
        UtilsContentInit.initialize(appContext);
    }

    @AfterClass
    public static void stop() {
        // 测试结束后，再删除全部文件
        FileUtils.dirDelete(sBasePath, true);
        LogUtils.clearLogUtilsOptions();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Boolean> getBooleanValuesMap() {
        Map<String, Boolean> valuesMap = new HashMap<>();
        valuesMap.put("key##", true);
        valuesMap.put("/#key", false);
        valuesMap.put("key/", false);
        valuesMap.put("k/ey", false);
        return valuesMap;
    }

    private Map<String, Boolean> getBooleanOthersMap() {
        Map<String, Boolean> othersMap = new HashMap<>();
        othersMap.put("key##", false);
        othersMap.put("/#key", true);
        othersMap.put("key/", true);
        othersMap.put("k/ey", true);
        return othersMap;
    }

    @Test
    public void putBoolean() {
        Map<String, Boolean> valuesMap = getBooleanValuesMap();
        Map<String, Boolean> othersMap = getBooleanOthersMap();
        for (String key : valuesMap.keySet()) {
            boolean value = valuesMap.get(key);
            boolean other = othersMap.get(key);
            testPut(Boolean.class, key, value, other);
        }
    }

    @Test
    public void getBoolean() {
        Map<String, Boolean> valuesMap = getBooleanValuesMap();
        for (String key : valuesMap.keySet()) {
            boolean value = valuesMap.get(key);
            testGet(Boolean.class, key, value);
        }
    }

    @Test
    public void deleteBoolean() {
        Map<String, Boolean> valuesMap = getBooleanValuesMap();
        for (String key : valuesMap.keySet()) {
            boolean value = valuesMap.get(key);
            testDelete(Boolean.class, key, value);
        }
    }

    @Test
    public void registerBooleanCallback() {
        // 已在其它地方测试过
    }

    @Test
    public void unregisterBooleanCallback() {
        Map<String, Boolean> valuesMap = getBooleanValuesMap();
        for (String key : valuesMap.keySet()) {
            boolean value = valuesMap.get(key);
            testUnregister(Boolean.class, key, value);
        }
    }

    private Map<String, Integer> getIntegerValuesMap() {
        Map<String, Integer> valuesMap = new HashMap<>();
        valuesMap.put("key##", -1);
        valuesMap.put("/#key", 0);
        valuesMap.put("key/", Integer.MIN_VALUE);
        valuesMap.put("k/ey", Integer.MAX_VALUE);
        return valuesMap;
    }

    private Map<String, Integer> getIntegerOthersMap() {
        Map<String, Integer> othersMap = new HashMap<>();
        othersMap.put("key##", 100);
        othersMap.put("/#key", Integer.MIN_VALUE);
        othersMap.put("key/", Integer.MAX_VALUE);
        othersMap.put("k/ey", -2);
        return othersMap;
    }

    @Test
    public void putInteger() {
        Map<String, Integer> valuesMap = getIntegerValuesMap();
        Map<String, Integer> othersMap = getIntegerOthersMap();
        for (String key : valuesMap.keySet()) {
            int value = valuesMap.get(key);
            int other = othersMap.get(key);
            testPut(Integer.class, key, value, other);
        }
    }

    @Test
    public void getInteger() {
        Map<String, Integer> valuesMap = getIntegerValuesMap();
        for (String key : valuesMap.keySet()) {
            int value = valuesMap.get(key);
            testGet(Integer.class, key, value);
        }
    }

    @Test
    public void deleteInteger() {
        Map<String, Integer> valuesMap = getIntegerValuesMap();
        for (String key : valuesMap.keySet()) {
            int value = valuesMap.get(key);
            testDelete(Integer.class, key, value);
        }
    }

    @Test
    public void registerIntegerCallback() {
        // 已在其它地方测试过
    }

    @Test
    public void unregisterIntegerCallback() {
        Map<String, Integer> valuesMap = getIntegerValuesMap();
        for (String key : valuesMap.keySet()) {
            int value = valuesMap.get(key);
            testUnregister(Integer.class, key, value);
        }
    }

    private Map<String, Long> getLongValuesMap() {
        Map<String, Long> valuesMap = new HashMap<>();
        valuesMap.put("key##", -1L);
        valuesMap.put("/#key", 0L);
        valuesMap.put("key/", Long.MIN_VALUE);
        valuesMap.put("k/ey", Long.MAX_VALUE);
        return valuesMap;
    }

    private Map<String, Long> getLongOthersMap() {
        Map<String, Long> othersMap = new HashMap<>();
        othersMap.put("key##", 100L);
        othersMap.put("/#key", Long.MIN_VALUE);
        othersMap.put("key/", Long.MAX_VALUE);
        othersMap.put("k/ey", -2L);
        return othersMap;
    }

    @Test
    public void putLong() {
        Map<String, Long> valuesMap = getLongValuesMap();
        Map<String, Long> othersMap = getLongOthersMap();
        for (String key : valuesMap.keySet()) {
            long value = valuesMap.get(key);
            long other = othersMap.get(key);
            testPut(Long.class, key, value, other);
        }
    }

    @Test
    public void getLong() {
        Map<String, Long> valuesMap = getLongValuesMap();
        for (String key : valuesMap.keySet()) {
            long value = valuesMap.get(key);
            testGet(Long.class, key, value);
        }
    }

    @Test
    public void deleteLong() {
        Map<String, Long> valuesMap = getLongValuesMap();
        for (String key : valuesMap.keySet()) {
            long value = valuesMap.get(key);
            testDelete(Long.class, key, value);
        }
    }

    @Test
    public void registerLongCallback() {
        // 已在其它地方测试过
    }

    @Test
    public void unregisterLongCallback() {
        Map<String, Long> valuesMap = getLongValuesMap();
        for (String key : valuesMap.keySet()) {
            long value = valuesMap.get(key);
            testUnregister(Long.class, key, value);
        }
    }

    private Map<String, String> getStringValuesMap() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("key##", "#hello dan");
        valuesMap.put("/#key", "/hello dan");
        valuesMap.put("key/", "Long.MIN_VALUE");
        valuesMap.put("k/ey", "Long.MAX_VALUE");
        valuesMap.put("", "");
        return valuesMap;
    }

    private Map<String, String> getStringOthersMap() {
        Map<String, String> othersMap = new HashMap<>();
        othersMap.put("key##", "100L");
        othersMap.put("/#key", "Long.MIN_VALUE");
        othersMap.put("key/", "hello//dan");
        othersMap.put("k/ey", "hel#lo/dan##");
        othersMap.put("", "#//#");
        return othersMap;
    }

    @Test
    public void putString() {
        Map<String, String> valuesMap = getStringValuesMap();
        Map<String, String> othersMap = getStringOthersMap();
        for (String key : valuesMap.keySet()) {
            String value = valuesMap.get(key);
            String other = othersMap.get(key);
            testPut(String.class, key, value, other);
        }
    }

    @Test
    public void getString() {
        Map<String, String> valuesMap = getStringValuesMap();
        for (String key : valuesMap.keySet()) {
            String value = valuesMap.get(key);
            testGet(String.class, key, value);
        }
    }

    @Test
    public void deleteString() {
        Map<String, String> valuesMap = getStringValuesMap();
        for (String key : valuesMap.keySet()) {
            String value = valuesMap.get(key);
            testDelete(String.class, key, value);
        }
    }

    @Test
    public void registerStringCallback() {
        // 已在其它地方测试过
    }

    @Test
    public void unregisterStringCallback() {
        Map<String, String> valuesMap = getStringValuesMap();
        for (String key : valuesMap.keySet()) {
            String value = valuesMap.get(key);
            testUnregister(String.class, key, value);
        }
    }


    private <T> void putValue(@NonNull Class<T> clazz, @NonNull String key, @NonNull T value) {
        if (clazz.getName().equals(Boolean.class.getName())) {
            UtilsContentHelper.putBoolean(key, (Boolean) value);
        } else if (clazz.getName().equals(Integer.class.getName())) {
            UtilsContentHelper.putInteger(key, (Integer) value);
        } else if (clazz.getName().equals(Long.class.getName())) {
            UtilsContentHelper.putLong(key, (Long) value);
        } else if (clazz.getName().equals(String.class.getName())) {
            UtilsContentHelper.putString(key, (String) value);
        } else {
            throw new IllegalArgumentException("clazz: " + clazz + " is invalid");
        }
    }

    private <T> T getValue(@NonNull Class<T> clazz, @NonNull String key) {
        if (clazz.getName().equals(Boolean.class.getName())) {
            return (T) UtilsContentHelper.getBoolean(key);
        } else if (clazz.getName().equals(Integer.class.getName())) {
            return (T) UtilsContentHelper.getInteger(key);
        } else if (clazz.getName().equals(Long.class.getName())) {
            return (T) UtilsContentHelper.getLong(key);
        } else if (clazz.getName().equals(String.class.getName())) {
            return (T) UtilsContentHelper.getString(key);
        } else {
            throw new IllegalArgumentException("clazz: " + clazz + " is invalid");
        }
    }

    private <T> void deleteValue(@NonNull Class<T> clazz, @NonNull String key) {
        if (clazz.getName().equals(Boolean.class.getName())) {
            UtilsContentHelper.deleteBoolean(key);
        } else if (clazz.getName().equals(Integer.class.getName())) {
            UtilsContentHelper.deleteInteger(key);
        } else if (clazz.getName().equals(Long.class.getName())) {
            UtilsContentHelper.deleteLong(key);
        } else if (clazz.getName().equals(String.class.getName())) {
            UtilsContentHelper.deleteString(key);
        } else {
            throw new IllegalArgumentException("clazz: " + clazz + " is invalid");
        }
    }

    private <T> void registerCallback(@NonNull Class<T> clazz, @NonNull String key, UtilsContentCallback<T> callback) {
        if (clazz.getName().equals(Boolean.class.getName())) {
            UtilsContentHelper.registerBooleanCallback(key, (UtilsContentCallback<Boolean>) callback);
        } else if (clazz.getName().equals(Integer.class.getName())) {
            UtilsContentHelper.registerIntegerCallback(key, (UtilsContentCallback<Integer>) callback);
        } else if (clazz.getName().equals(Long.class.getName())) {
            UtilsContentHelper.registerLongCallback(key, (UtilsContentCallback<Long>) callback);
        } else if (clazz.getName().equals(String.class.getName())) {
            UtilsContentHelper.registerStringCallback(key, (UtilsContentCallback<String>) callback);
        } else {
            throw new IllegalArgumentException("clazz: " + clazz + " is invalid");
        }
    }

    private <T> void unregisterCallback(@NonNull Class<T> clazz, @NonNull String key, UtilsContentCallback<T> callback) {
        if (clazz.getName().equals(Boolean.class.getName())) {
            UtilsContentHelper.unregisterBooleanCallback(key, (UtilsContentCallback<Boolean>) callback);
        } else if (clazz.getName().equals(Integer.class.getName())) {
            UtilsContentHelper.unregisterIntegerCallback(key, (UtilsContentCallback<Integer>) callback);
        } else if (clazz.getName().equals(Long.class.getName())) {
            UtilsContentHelper.unregisterLongCallback(key, (UtilsContentCallback<Long>) callback);
        } else if (clazz.getName().equals(String.class.getName())) {
            UtilsContentHelper.unregisterStringCallback(key, (UtilsContentCallback<String>) callback);
        } else {
            throw new IllegalArgumentException("clazz: " + clazz + " is invalid");
        }
    }

    private <T> void testPut(@NonNull Class<T> clazz, @NonNull String key, @NonNull T value, @NonNull T other) {
        T[] values = (T[]) Array.newInstance(clazz, 1);
        values[0] = other;
        // 注册回调
        UtilsContentCallback<T> callback = new UtilsContentCallback<T>() {
            @Override
            public void onValueChanged(@Nullable T oldValue, @NonNull T newValue) {
                values[0] = newValue;
            }

            @Override
            public void onValueDeleted() {

            }
        };
        registerCallback(clazz, key, callback);
        // 放置数据
        putValue(clazz, key, value);
        sleep(50);
        assertEquals(value, values[0]);
        // 注销回调
        unregisterCallback(clazz, key, callback);
    }

    private <T> void testGet(@NonNull Class<T> clazz, @NonNull String key, @NonNull T value) {
        // 删除旧数据
        deleteValue(clazz, key);
        // 初次获取新数据
        T gain = getValue(clazz, key);
        assertNull(gain);
        // 放置新数据
        putValue(clazz, key, value);
        // 再次获取新数据
        gain = getValue(clazz, key);
        assertEquals(value, gain);
    }

    private <T> void testDelete(@NonNull Class<T> clazz, @NonNull String key, @NonNull T value) {
        // 删除旧数据
        deleteValue(clazz, key);
        // 放置数据
        putValue(clazz, key, value);
        sleep(50);
        // 注册回调
        T[] oldValues = (T[]) Array.newInstance(clazz, 1);
        T[] newValues = (T[]) Array.newInstance(clazz, 1);
        final int[] deleteCount = new int[1];
        UtilsContentCallback<T> callback = new UtilsContentCallback<T>() {
            @Override
            public void onValueChanged(@Nullable T oldValue, @NonNull T newValue) {
                oldValues[0] = oldValue;
                newValues[0] = newValue;
            }

            @Override
            public void onValueDeleted() {
                ++deleteCount[0];
            }
        };
        registerCallback(clazz, key, callback);
        deleteValue(clazz, key);
        sleep(50);
        // 注册时，查询的数据已更新
        assertEquals(oldValues[0], value);
        assertEquals(newValues[0], value);
        // 删除回调被调用有且仅有一次
        assertEquals(1, deleteCount[0]);
        // 注销回调
        unregisterCallback(clazz, key, callback);
    }

    private <T> void testUnregister(@NonNull Class<T> clazz, @NonNull String key, @NonNull T value) {
        final int[] triggeredCount = new int[1];
        UtilsContentCallback<T> callback = new UtilsContentCallback<T>() {
            @Override
            public void onValueChanged(@Nullable T oldValue, @NonNull T newValue) {
                ++triggeredCount[0];
            }
            @Override
            public void onValueDeleted() {
                ++triggeredCount[0];
            }
        };
        // 放置数据
        putValue(clazz, key, value);
        sleep(50);
        // 注册回调
        registerCallback(clazz, key, callback);
        assertEquals(1, triggeredCount[0]);
        // 注销回调
        unregisterCallback(clazz, key, callback);
        // 删除数据
        deleteValue(clazz, key);
        // 放置数据
        putValue(clazz, key, value);
        sleep(50);
        assertEquals(1, triggeredCount[0]);
    }
}