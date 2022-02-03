/*
 * Copyright (c) 2022 afunx. All Rights Reserved
 */

package com.dan.me.utils.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import static com.dan.me.utils.content.UtilsContentConstants.BOOLEAN_DB_TABLE_NAME;
import static com.dan.me.utils.content.UtilsContentConstants.DATABASE_NAME;
import static com.dan.me.utils.content.UtilsContentConstants.DATABASE_VERSION;
import static com.dan.me.utils.content.UtilsContentConstants.INTEGER_DB_TABLE_NAME;
import static com.dan.me.utils.content.UtilsContentConstants.KEY_COLUMN_NAME;
import static com.dan.me.utils.content.UtilsContentConstants.LONG_DB_TABLE_NAME;
import static com.dan.me.utils.content.UtilsContentConstants.STRING_DB_TABLE_NAME;
import static com.dan.me.utils.content.UtilsContentConstants.VALUE_COLUMN_NAME;

class UtilsContentDBHelper extends SQLiteOpenHelper {

    UtilsContentDBHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BOOLEAN_DB_TABLE_NAME + "("
                + KEY_COLUMN_NAME + " TEXT PRIMARY KEY NOT NULL,"
                + VALUE_COLUMN_NAME + " INT NOT NULL"
                + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + INTEGER_DB_TABLE_NAME + "("
                + KEY_COLUMN_NAME +" TEXT PRIMARY KEY NOT NULL,"
                + VALUE_COLUMN_NAME +" INT NOT NULL"
                + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + LONG_DB_TABLE_NAME + "("
                + KEY_COLUMN_NAME + " TEXT PRIMARY KEY NOT NULL,"
                + VALUE_COLUMN_NAME + " LONG NOT NULL"
                + ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + STRING_DB_TABLE_NAME + "("
                + KEY_COLUMN_NAME + " TEXT PRIMARY KEY NOT NULL,"
                + VALUE_COLUMN_NAME + " TEXT NOT NULL"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 暂不考虑升级问题。每种类型数据都是单独的KV映射表。
    }
}
