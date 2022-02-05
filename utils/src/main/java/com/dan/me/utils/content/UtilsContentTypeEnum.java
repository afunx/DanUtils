/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

public enum UtilsContentTypeEnum {

    DB_BOOLEAN(UtilsContentConstants.BOOLEAN_CODE, UtilsContentConstants.BOOLEAN_DB_TABLE_NAME),
    DB_INTEGER(UtilsContentConstants.INTEGER_CODE, UtilsContentConstants.INTEGER_DB_TABLE_NAME),
    DB_LONG(UtilsContentConstants.LONG_CODE, UtilsContentConstants.LONG_DB_TABLE_NAME),
    DB_STRING(UtilsContentConstants.STRING_CODE, UtilsContentConstants.STRING_DB_TABLE_NAME),
    DB_OBJECT(UtilsContentConstants.OBJECT_CODE, UtilsContentConstants.OBJECT_DB_TABLE_NAME),
    CACHE_BOOLEAN(UtilsContentConstants.BOOLEAN_CODE, UtilsContentConstants.BOOLEAN_CACHE_TABLE_NAME),
    CACHE_INTEGER(UtilsContentConstants.INTEGER_CODE, UtilsContentConstants.INTEGER_CACHE_TABLE_NAME),
    CACHE_LONG(UtilsContentConstants.LONG_CODE, UtilsContentConstants.LONG_CACHE_TABLE_NAME),
    CACHE_STRING(UtilsContentConstants.STRING_CODE, UtilsContentConstants.STRING_CACHE_TABLE_NAME),
    CACHE_OBJECT(UtilsContentConstants.OBJECT_CODE, UtilsContentConstants.OBJECT_CACHE_TABLE_NAME);

    private final int tableCode;

    private final String tableName;

    UtilsContentTypeEnum(int code, String tableName) {
        this.tableCode = code;
        this.tableName = tableName;
    }

    public int getTableCode() {
        return tableCode;
    }

    public String getTableName() {
        return tableName;
    }

    /**
     * 检查是否为cache存储于内存
     *
     * @return 是否为cache存储于内存
     */
    public boolean isCache() {
        return this != DB_BOOLEAN && this != DB_INTEGER
                && this != DB_LONG && this != DB_STRING
                && this != DB_OBJECT;
    }

    public static UtilsContentTypeEnum parseTableName(String tableName) {
        switch (tableName) {
            case UtilsContentConstants.BOOLEAN_DB_TABLE_NAME:
                return DB_BOOLEAN;
            case UtilsContentConstants.INTEGER_DB_TABLE_NAME:
                return DB_INTEGER;
            case UtilsContentConstants.LONG_DB_TABLE_NAME:
                return DB_LONG;
            case UtilsContentConstants.STRING_DB_TABLE_NAME:
                return DB_STRING;
            case UtilsContentConstants.OBJECT_DB_TABLE_NAME:
                return DB_OBJECT;
            case UtilsContentConstants.BOOLEAN_CACHE_TABLE_NAME:
                return CACHE_BOOLEAN;
            case UtilsContentConstants.INTEGER_CACHE_TABLE_NAME:
                return CACHE_INTEGER;
            case UtilsContentConstants.LONG_CACHE_TABLE_NAME:
                return CACHE_LONG;
            case UtilsContentConstants.STRING_CACHE_TABLE_NAME:
                return CACHE_STRING;
            case UtilsContentConstants.OBJECT_CACHE_TABLE_NAME:
                return CACHE_OBJECT;
            default:
                throw new IllegalArgumentException("tableName: " + tableName + " is invalid");
        }
    }
}
