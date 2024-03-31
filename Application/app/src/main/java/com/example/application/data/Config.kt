package com.example.application.data

import android.content.ContentValues
import android.database.Cursor
import com.example.application.db.InsertBuilder
import com.example.application.db.ManagerDB


data class Config(val version : Int, val csstatsID : String, val current_version : Int) {
    fun noMoreCurrentVersion() : Config {
        return this.copy(current_version = 0)
    }
}


/* Functions related with database operations:
 * - createTableConfig: Supports the creation of Activities table.
 * - updateTableConfig: Supports the INSERT / UPDATE operations.
 * - convertEntryConfig: Converts the data from the database into an Config.
 * - convertEntriesConfigs: Converts the data from the database into a List<Config>.
 * - getNumberLast: Get the current configuration
 * - whereClauseLast: Where clause to get last config.
 * - whereClauseLastList: Assign the values to get the last config.
 * - getTableConfig: Table of configurations name.
 */
class ConfigTableFuns {
    companion object {

        private fun updateTableConfig(cnf : Config) : ContentValues {
            val values = ContentValues().apply {
                put("csstatsID",cnf.csstatsID)
                put("selected",cnf.current_version)
            }
            return values
        }

        private fun convertEntryConfig(cursor : Cursor) : Config {
            val version = cursor.getInt(cursor.getColumnIndexOrThrow("version"))
            val csstats_id = cursor.getString(cursor.getColumnIndexOrThrow("csstatsID"))
            val selected = cursor.getInt(cursor.getColumnIndexOrThrow("selected"))
            return Config(version,csstats_id,selected)
        }

        private fun convertEntriesConfigs(cursor : Cursor) : List<Config> {
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

        private fun whereClauseLast() : String {
            return "selected = ?"
        }

        private fun whereClauseLastList() : List<String> {
            val res : MutableList<String> = mutableListOf()
            res.add(getNumberSelected().toString())
            return res
        }

        fun getTableConfig() : String {
            return "configs"
        }

        private fun getNumberSelected() : Int {
            return 1
        }
        fun createTableConfig() : List<String> {
            val attributes = InsertBuilder()
            attributes.addAtribute("version","INTEGER",isNull = false,isPrimaryKey = true,autoIncrement = true)
            attributes.addAtribute("csstatsID","TEXT",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("selected","INTEGER",isNull = false,isPrimaryKey = false,autoIncrement = false)
            return attributes.getList()
        }
        fun getLastVersion() : Config? {
            val tableName : String = getTableConfig()
            val whereClause : String = whereClauseLast()
            val whereClauseValues : List<String> = whereClauseLastList()
            val list = ManagerDB.getInstance()?.select(tableName, whereClause, whereClauseValues, ConfigTableFuns::convertEntriesConfigs )
            return if (list.isNullOrEmpty()) null else list.first()
        }

        fun newConfig(configOld : Config?, csstatsid : String) {
            val tableName : String = getTableConfig()
            val whereClause : String = whereClauseLast()
            val whereClauseValues : List<String> = whereClauseLastList()
            val selectedVersion : Int = getNumberSelected()
            if(configOld != null) {
                val updateClause = updateTableConfig(configOld.noMoreCurrentVersion())
                ManagerDB.getInstance()?.update(tableName, updateClause, whereClause, whereClauseValues)
            }
            ManagerDB.getInstance()?.insert(tableName, updateTableConfig(Config(0,csstatsid, selectedVersion)))
        }
    }
}