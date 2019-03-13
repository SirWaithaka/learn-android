package com.example.tasktimer

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

private const val TAG = "AppDatabase"

private const val DATABASE_NAME = "TaskTimer.db"
private const val DATABASE_VERSION = 3

internal class AppDatabase constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
   init {
      Log.d(TAG, "App Database: initializing")
   }

   override fun onCreate(db: SQLiteDatabase?) {
      Log.d(TAG, "onCreate: starts")
      val sSQL = """
            CREATE TABLE ${TasksContract.TABLE_NAME} (
            ${TasksContract.Columns.ID} INTEGER PRIMARY KEY NOT NULL,
            ${TasksContract.Columns.TASK_NAME} TEXT NOT NULL,
            ${TasksContract.Columns.TASK_DESCRIPTION} TEXT,
            ${TasksContract.Columns.TASK_SORT_ORDER} INTEGER);
      """.replaceIndent(" ")
      Log.d(TAG, sSQL)
   }

   override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

   }

   companion object : SingletonHolder<AppDatabase, Context>(::AppDatabase)

//   companion object {
//
//      @Volatile
//      private var instance: AppDatabase? = null
//
//      fun getInstance(context: Context): AppDatabase =
//            instance ?: synchronized(this) {
//               instance ?: AppDatabase(context).also { instance = it }
//            }
//
//   }
}