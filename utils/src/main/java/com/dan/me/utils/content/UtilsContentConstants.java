/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import android.net.Uri;

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
    String BOOLEAN_DB_TABLE_NAME = "boolean_db";
    String INTEGER_DB_TABLE_NAME = "integer_db";
    String LONG_DB_TABLE_NAME = "long_db";
    String STRING_DB_TABLE_NAME = "string_db";
    String OBJECT_DB_TABLE_NAME = "object_db";
    // 数据库列名
    String KEY_COLUMN_NAME = "_KEY";
    String VALUE_COLUMN_NAME = "VALUE";

    // cache数据库表名（虚拟数据库表，实际并不存在，仅仅是使用Map实现）
    String BOOLEAN_CACHE_TABLE_NAME = "boolean_cache";
    String INTEGER_CACHE_TABLE_NAME = "integer_cache";
    String LONG_CACHE_TABLE_NAME = "long_cache";
    String STRING_CACHE_TABLE_NAME = "string_cache";
    String OBJECT_CACHE_TABLE_NAME = "object_cache";
    // cache数据库列名
    String KEY_CACHE_COLUMN_NAME = "_KEY_CACHE";
    String VALUE_CACHE_COLUMN_NAME = "VALUE_CACHE";

    // boolean类型True
    int BOOLEAN_TRUE = 1;
    // boolean类型false
    int BOOLEAN_FALSE = 0;

    // 各种类型的code值
    int BOOLEAN_CODE = 0;
    int INTEGER_CODE = 1;
    int LONG_CODE = 2;
    int STRING_CODE = 3;
    int OBJECT_CODE = 4;

    // 各种ACTION字符串
    String ACTION_DB_INSERT = "db_insert";
    String ACTION_DB_UPDATE = "db_update";
    String ACTION_DB_DELETE = "db_delete";
    String ACTION_CACHE_INSERT = "cache_insert";
    String ACTION_CACHE_UPDATE = "cache_update";
    String ACTION_CACHE_DELETE = "cache_delete";

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

    Uri URI_DB_BOOLEAN = Uri.parse("content://" + AUTHORITY + SPLIT_CHAR + BOOLEAN_DB_TABLE_NAME);
    Uri URI_DB_INTEGER = Uri.parse("content://" + AUTHORITY + SPLIT_CHAR + INTEGER_DB_TABLE_NAME);
    Uri URI_DB_LONG = Uri.parse("content://" + AUTHORITY + SPLIT_CHAR + LONG_DB_TABLE_NAME);
    Uri URI_DB_STRING = Uri.parse("content://" + AUTHORITY + SPLIT_CHAR + STRING_DB_TABLE_NAME);
    Uri URI_DB_OBJECT = Uri.parse("content://" + AUTHORITY + SPLIT_CHAR + OBJECT_DB_TABLE_NAME);

    Uri URI_CACHE_BOOLEAN = Uri.parse("content://" + AUTHORITY + SPLIT_CHAR + BOOLEAN_CACHE_TABLE_NAME);
    Uri URI_CACHE_INTEGER = Uri.parse("content://" + AUTHORITY + SPLIT_CHAR + INTEGER_CACHE_TABLE_NAME);
    Uri URI_CACHE_LONG = Uri.parse("content://" + AUTHORITY + SPLIT_CHAR + LONG_CACHE_TABLE_NAME);
    Uri URI_CACHE_STRING = Uri.parse("content://" + AUTHORITY + SPLIT_CHAR + STRING_CACHE_TABLE_NAME);
    Uri URI_CACHE_OBJECT = Uri.parse("content://" + AUTHORITY + SPLIT_CHAR + OBJECT_CACHE_TABLE_NAME);
}
