package com.example.application.db

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.application.data.createTableConfiguration
import com.example.application.data.getTableConfig

/*
 * Generic Manager for SQLite database.
 * Functions documentation:
 * - getTables: Get the name of the tables and their columns in order to create them.
 * - onCreate: Creates the database.
 * - onUpgrade: Does nothing.
 * - insert: Performs an insert.
 * - update: Performs an update.
 * - delete: Performs an delete.
 * - select: Performs an select.
 * - create: Performs a table creation.
 * - drop: Performs a table drop.
 */
class ManagerDB(context: Context,
                databaseName : String,
                databaseVersion : Int) : SQLiteOpenHelper(context, databaseName, null, databaseVersion)  {
    companion object {
        var mdb : ManagerDB? = null

        fun initialize(app : Application) {
            if (mdb == null)
                mdb = ManagerDB(app,"DatabaseSA",1)
        }

        fun getInstance() : ManagerDB? {
            return mdb
        }

    }
    private fun getTables() : List<Pair<String,List<String>>> {
        val res : MutableList<Pair<String,List<String>>> = mutableListOf()
        res.add(Pair(getTableConfig(), createTableConfiguration()))
        return res
    }
    override fun onCreate(db: SQLiteDatabase?) {
        val tables = getTables()
        tables.forEach{ db?.execSQL("CREATE TABLE IF NOT EXISTS '${it.first}' (${it.second.joinToString(",")})") }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun insert(table : String, content : ContentValues) {
        val db = writableDatabase
        db.insert(table,null,content)
        db.close()
    }

    fun update(table : String, content : ContentValues, whereClause : String, whereClauses : List<String>) {
        val db = writableDatabase
        db.update(table, content,whereClause,whereClauses.toTypedArray())
        db.close()
    }

    fun delete(table : String, whereClause : String?, whereClauses : List<String>?) {
        val db = writableDatabase
        db.delete(table,whereClause,whereClauses?.toTypedArray())
        db.close()
    }

    fun select(table : String, whereClause : String?, content : List<String>?) : Cursor {
        val db = readableDatabase
        val query = if (whereClause == null) "" else "WHERE $whereClause"
        val cursor = db.rawQuery("SELECT * FROM $table $query",content?.toTypedArray())
        db.close()
        return cursor
    }


    fun create(table: String, columns : List<String>) {
        val db = writableDatabase
        db.execSQL("CREATE TABLE IF NOT EXISTS '${table}' (${columns.joinToString(",")})")
    }

    fun drop(table : String) {
        val db = writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $table")
        db.close()
    }
}