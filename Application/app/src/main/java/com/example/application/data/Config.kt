package com.example.application.data

import android.content.ContentValues
import android.database.Cursor
import com.example.application.db.InsertBuilder


data class Config(val version : Int, val csstatsID : String, val current_version : Int) {
    fun isCurrentVersion() : Boolean {
        return current_version == getNumberLast()
    }
}


/* Functions related with database operations:
 * - createTableConfig: Supports the creation of Activities table.
 * - updateTableConfig: Supports the INSERT / UPDATE operations.
 * - convertEntryConfig: Converts the data from the database into an Config.
 * - whereClauseAll: return null in order to return them all.
 * - whereClauseLast: Makes the where clause to get last config.
 */
fun createTableConfig() : List<String> {
    val attributes = InsertBuilder()
    attributes.addAtribute("version","INTEGER",isNull = false,isPrimaryKey = true,autoIncrement = true)
    attributes.addAtribute("csstatsID","TEXT",isNull = false,isPrimaryKey = false,autoIncrement = false)
    attributes.addAtribute("selected","INTEGER",isNull = false,isPrimaryKey = false,autoIncrement = false)
    return attributes.getList()
}

fun selectTableConfigs() : List<String>? {
    return null
}


fun updateTableConfig(cnf : Config) : ContentValues {
    val values = ContentValues().apply {
        put("csstatsID",cnf.csstatsID)
        put("selected",cnf.current_version)
    }
    return values
}

fun convertEntryConfig(cursor : Cursor) : Config {
    val version = cursor.getInt(cursor.getColumnIndexOrThrow("version"))
    val csstats_id = cursor.getString(cursor.getColumnIndexOrThrow("csstatsID"))
    val selected = cursor.getInt(cursor.getColumnIndexOrThrow("selected"))
    return Config(version,csstats_id,selected)
}

fun convertEntriesConfigs(cursor : Cursor) : List<Config> {
    val res : MutableList<Config> = mutableListOf()
    cursor.apply {
        if (moveToFirst()) {
            do {
                val config = convertEntryConfig(cursor)
                res.add(config)
            } while (moveToNext())
        }
        close()
    }
    return res
}

fun getNumberLast() : Int {
    return 1
}
fun whereClauseLast() : String {
    val num = getNumberLast()
    return "selected = ?"
}

fun whereClauseLastList() : List<String> {
    val res : MutableList<String> = mutableListOf()
    res.add(getNumberLast().toString())
    return res
}

fun getTableConfig() : String {
    return "configs"
}

