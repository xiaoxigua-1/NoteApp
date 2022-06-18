package com.example.xiaonote

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLite(context: Context) : SQLiteOpenHelper(context, "db.db", null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE NOTES(CONTEXT TEXT, _ID TEXT)")
        p0?.execSQL("CREATE TABLE FOLDER(NAME TEXT, _ID TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}