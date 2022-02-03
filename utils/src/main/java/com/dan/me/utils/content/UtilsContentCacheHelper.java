package com.dan.me.utils.content;

/**
 * 该类使用了Facade Pattern，为content的唯一对外接口。
 * 主要用来全应用跨进程修改变量数值，以及监听数值的变化。
 * 目前，仅支持String, Integer, Long, Boolean类型。
 */
public class UtilsContentCacheHelper {

    private static final UtilsContentCacheNotifier sUtilsContentCacheNotifier = UtilsContentCacheNotifier.get();

    /**
     * 设置String变量
     *
     * @param key     键值
     * @param value   String值（非空）
     */
    public static void putString(String key, String value) {
        UtilsContentCacheResolver.putString(key, value);
    }

    /**
     * 查询String变量
     *
     * @param key     键值
     * @return String值或null（若不存在）
     */
    public static String getString(String key) {
        return UtilsContentCacheResolver.getString(key);
    }

    /**
     * 删除String变量
     *
     * @param key     键值
     */
    public static void deleteString(String key) {
        UtilsContentCacheResolver.deleteString(key);
    }

    /**
     * 注册String变量监听器
     *
     * @param key         键值
     * @param callback    回调
     */
    public static void registerStringCallback(String key, UtilsContentCallback<String> callback) {
        sUtilsContentCacheNotifier.registerStringCallback(key, callback);
    }

    /**
     * 注销String变量监听器
     *
     * @param key      键值
     * @param callback 回调
     */
    public static void unregisterStringCallback(String key, UtilsContentCallback<String> callback) {
        sUtilsContentCacheNotifier.unregisterStringCallback(key, callback);
    }

    /**
     * 设置Integer变量
     *

     * @param key     键值
     * @param value   int值
     */
    public static void putInteger(String key, int value) {
        UtilsContentCacheResolver.putInt(key, value);
    }

    /**
     * 查询Integer变量
     *

     * @param key     键值
     * @return Integer值或null（若不存在）
     */
    public static Integer getInteger(String key) {
        return UtilsContentCacheResolver.getInt(key);
    }

    /**
     * 删除Integer变量
     *
     * @param key     键值
     */
    public static void deleteInteger(String key) {
        UtilsContentCacheResolver.deleteInt(key);
    }

    /**
     * 注册Integer变量监听器
     *
     * @param key         键值
     * @param callback    回调
     */
    public static void registerIntegerCallback(String key, UtilsContentCallback<Integer> callback) {
        sUtilsContentCacheNotifier.registerIntegerCallback(key, callback);
    }

    /**
     * 注销Integer变量监听器
     *
     * @param key      键值
     * @param callback 回调
     */
    public static void unregisterIntegerCallback(String key, UtilsContentCallback<Integer> callback) {
        sUtilsContentCacheNotifier.unregisterIntegerCallback(key, callback);
    }

    /**
     * 设置Long变量
     *
     * @param key     键值
     * @param value   long值
     */
    public static void putLong(String key, long value) {
        UtilsContentCacheResolver.putLong(key, value);
    }

    /**
     * 查询Long变量
     *
     * @param key     键值
     * @return Long值或null（若不存在）
     */
    public static Long getLong(String key) {
        return UtilsContentCacheResolver.getLong(key);
    }

    /**
     * 删除Long变量
     *
     * @param key     键值
     */
    public static void deleteLong(String key) {
        UtilsContentCacheResolver.deleteLong(key);
    }

    /**
     * 注册Long变量监听器
     *
     * @param key         键值
     * @param callback    回调
     */
    public static void registerLongCallback(String key, UtilsContentCallback<Long> callback) {
        sUtilsContentCacheNotifier.registerLongCallback(key, callback);
    }

    /**
     * 注销Long变量监听器
     *
     * @param key      键值
     * @param callback 回调
     */
    public static void unregisterLongCallback(String key, UtilsContentCallback<Long> callback) {
        sUtilsContentCacheNotifier.unregisterLongCallback(key, callback);
    }

    /**
     * 设置Boolean变量
     *
     * @param key     键值
     * @param value   boolean值
     */
    public static void putBoolean(String key, boolean value) {
        UtilsContentCacheResolver.putBoolean(key, value);
    }

    /**
     * 查询Boolean变量
     *
     * @param key     键值
     * @return Boolean值或null（若不存在）
     */
    public static Boolean getBoolean(String key) {
        return UtilsContentCacheResolver.getBoolean(key);
    }

    /**
     * 删除Boolean变量
     *
     * @param key     键值
     */
    public static void deleteBoolean(String key) {
        UtilsContentCacheResolver.deleteBoolean(key);
    }

    /**
     * 注册Boolean变量监听器
     *
     * @param key         键值
     * @param callback    回调
     */
    public static void registerBooleanCallback(String key, UtilsContentCallback<Boolean> callback) {
        sUtilsContentCacheNotifier.registerBooleanCallback(key, callback);
    }

    /**
     * 注销Boolean变量监听器
     *
     * @param key      键值
     * @param callback 回调
     */
    public static void unregisterBooleanCallback(String key, UtilsContentCallback<Boolean> callback) {
        sUtilsContentCacheNotifier.unregisterBooleanCallback(key, callback);
    }
}
