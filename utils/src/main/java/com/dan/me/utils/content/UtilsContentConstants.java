/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

public interface UtilsContentConstants {

    // 是否开启content模块的打印
    boolean DEBUG = true;

    // ContentProvider的authority
    String AUTHORITY = "com.dan.me.utils.content.utilscontentprovider";

    // 数据库版本号
    int DATABASE_VERSION = 1;
    // 数据库库名
    String DATABASE_NAME = "dan_utils_content.db";
    // 数据库表名
    String BOOLEAN_TABLE_NAME = "boolean";
    String INTEGER_TABLE_NAME = "integer";
    String LONG_TABLE_NAME = "long";
    String STRING_TABLE_NAME = "string";
    // 数据库列名
    String KEY_COLUMN_NAME = "_KEY";
    String VALUE_COLUMN_NAME = "VALUE";

    // 各种类型的code值
    int BOOLEAN_CODE = 0;
    int INTEGER_CODE = 1;
    int LONG_CODE = 2;
    int STRING_CODE = 3;

    // 各种ACTION字符串
    String ACTION_UPDATE = "update";
    String ACTION_DELETE = "delete";


    // '#'为转义符
    // '/'为分割符
    // '#'转义为'##'
    // '/'转义为'#/'
    String ESCAPE = "#";
    char ESCAPE_CHAR = '#';
    char SPLIT_CHAR = '/';
    String POUND_ORIGIN = "#";
    String POUND_ESCAPE = "##";
    String SPLASH_ORIGIN = "/";
    String SPLASH_ESCAPE = "#/";
}
