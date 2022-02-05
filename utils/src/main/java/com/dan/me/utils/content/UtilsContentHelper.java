/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import androidx.annotation.NonNull;

/**
 * 使用外观模式，为content的db对外接口。主要用来全应用跨进程修改变量数值，以及监听数值的变化。
 */
public class UtilsContentHelper {

    private static final UtilsContentNotifier sUtilsContentNotifier = UtilsContentNotifier.get();

    /**
     * 设置Boolean变量
     *
     * @param key     键值
     * @param value   boolean值
     */
    public static void putBoolean(@NonNull String key, boolean value) {
        UtilsContentResolver.putBoolean(key, value);
    }

    /**
     * 查询Boolean变量
     *
     * @param key     键值
     * @return Boolean值或null（若不存在）
     */
    public static Boolean getBoolean(@NonNull String key) {
        return UtilsContentResolver.getBoolean(key);
    }

    /**
     * 删除Boolean变量
     *
     * @param key     键值
     */
    public static void deleteBoolean(@NonNull String key) {
        UtilsContentResolver.deleteBoolean(key);
    }

    /**
     * 注册Boolean变量监听器
     *
     * @param key         键值
     * @param callback    回调
     */
    public static void registerBooleanCallback(@NonNull String key, @NonNull UtilsContentCallback<Boolean> callback) {
        sUtilsContentNotifier.registerBooleanCallback(key, callback);
    }

    /**
     * 注销Boolean变量监听器
     *
     * @param key      键值
     * @param callback 回调
     */
    public static void unregisterBooleanCallback(@NonNull String key, @NonNull UtilsContentCallback<Boolean> callback) {
        sUtilsContentNotifier.unregisterBooleanCallback(key, callback);
    }

    /**
     * 设置Integer变量
     *
     * @param key     键值
     * @param value   int值
     */
    public static void putInteger(@NonNull String key, int value) {
        UtilsContentResolver.putInt(key, value);
    }

    /**
     * 查询Integer变量
     *
     * @param key     键值
     * @return Integer值或null（若不存在）
     */
    public static Integer getInteger(@NonNull String key) {
        return UtilsContentResolver.getInt(key);
    }

    /**
     * 删除Integer变量
     *
     * @param key     键值
     */
    public static void deleteInteger(@NonNull String key) {
        UtilsContentResolver.deleteInt(key);
    }

    /**
     * 注册Integer变量监听器
     *
     * @param key         键值
     * @param callback    回调
     */
    public static void registerIntegerCallback(@NonNull String key, @NonNull UtilsContentCallback<Integer> callback) {
        sUtilsContentNotifier.registerIntegerCallback(key, callback);
    }

    /**
     * 注销Integer变量监听器
     *
     * @param key      键值
     * @param callback 回调
     */
    public static void unregisterIntegerCallback(@NonNull String key, @NonNull UtilsContentCallback<Integer> callback) {
        sUtilsContentNotifier.unregisterIntegerCallback(key, callback);
    }

    /**
     * 设置Long变量
     *
     * @param key     键值
     * @param value   long值
     */
    public static void putLong(@NonNull String key, long value) {
        UtilsContentResolver.putLong(key, value);
    }

    /**
     * 查询Long变量
     *
     * @param key     键值
     * @return Long值或null（若不存在）
     */
    public static Long getLong(@NonNull String key) {
        return UtilsContentResolver.getLong(key);
    }

    /**
     * 删除Long变量
     *
     * @param key     键值
     */
    public static void deleteLong(@NonNull String key) {
        UtilsContentResolver.deleteLong(key);
    }

    /**
     * 注册Long变量监听器
     *
     * @param key         键值
     * @param callback    回调
     */
    public static void registerLongCallback(@NonNull String key, @NonNull UtilsContentCallback<Long> callback) {
        sUtilsContentNotifier.registerLongCallback(key, callback);
    }

    /**
     * 注销Long变量监听器
     *
     * @param key      键值
     * @param callback 回调
     */
    public static void unregisterLongCallback(@NonNull String key, @NonNull UtilsContentCallback<Long> callback) {
        sUtilsContentNotifier.unregisterLongCallback(key, callback);
    }


    /**
     * 设置String变量
     *
     * @param key     键值
     * @param value   String值（非空）
     */
    public static void putString(@NonNull String key, @NonNull String value) {
        UtilsContentResolver.putString(key, value);
    }

    /**
     * 查询String变量
     *
     * @param key     键值
     * @return String值或null（若不存在）
     */
    public static String getString(@NonNull String key) {
        return UtilsContentResolver.getString(key);
    }

    /**
     * 删除String变量
     *
     * @param key     键值
     */
    public static void deleteString(@NonNull String key) {
        UtilsContentResolver.deleteString(key);
    }

    /**
     * 注册String变量监听器
     *
     * @param key         键值
     * @param callback    回调
     */
    public static void registerStringCallback(@NonNull String key, @NonNull UtilsContentCallback<String> callback) {
        sUtilsContentNotifier.registerStringCallback(key, callback);
    }

    /**
     * 注销String变量监听器
     *
     * @param key      键值
     * @param callback 回调
     */
    public static void unregisterStringCallback(@NonNull String key, @NonNull UtilsContentCallback<String> callback) {
        sUtilsContentNotifier.unregisterStringCallback(key, callback);
    }

    /**
     * 设置Object变量
     *
     * @param key     键值
     * @param value   Object值（非空）
     */
    public static void putObject(@NonNull String key, @NonNull Object value) {
        UtilsContentResolver.putObject(key, value);
    }

    /**
     * 查询Object变量
     *
     * @param key     键值
     * @return Object值或null（若不存在）
     */
    public static Object getObject(@NonNull String key) {
        return UtilsContentResolver.getObject(key);
    }

    /**
     * 删除Object变量
     *
     * @param key     键值
     */
    public static void deleteObject(@NonNull String key) {
        UtilsContentResolver.deleteObject(key);
    }

    /**
     * 注册Object变量监听器
     *
     * @param key         键值
     * @param callback    回调
     */
    public static void registerObjectCallback(@NonNull String key, @NonNull UtilsContentCallback<Object> callback) {
        sUtilsContentNotifier.registerObjectCallback(key, callback);
    }

    /**
     * 注销Object变量监听器
     *
     * @param key      键值
     * @param callback 回调
     */
    public static void unregisterObjectCallback(@NonNull String key, @NonNull UtilsContentCallback<Object> callback) {
        sUtilsContentNotifier.unregisterObjectCallback(key, callback);
    }

}
