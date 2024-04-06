package com.example.application.data.location

import android.content.ContentValues
import android.database.Cursor
import android.location.Location
import com.example.application.data.config.ConfigTableFuns
import com.example.application.db.InsertBuilder
import com.example.application.db.ManagerDB

class LocationTableFuns {

    companion object {

        private fun updateTableLocation(location: Location) : ContentValues {
            val values = ContentValues().apply {
                put("latitude",location.latitude)
                put("longitude",location.longitude)
                put("altitude",location.altitude)
            }
            return values
        }

        private fun convertEntryLocation(cursor : Cursor) : Location {
            val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"))
            val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"))
            val altitude = cursor.getDouble(cursor.getColumnIndexOrThrow("altitude"))
            val location = Location("Database")
            location.latitude = latitude
            location.longitude = longitude
            location.altitude = altitude
            return location
        }

        private fun convertEntriesLocations(cursor : Cursor) : List<Location> {
            val res : MutableList<Location> = mutableListOf()
            cursor.apply {
                if (moveToFirst()) {
                    do {
                        val location = convertEntryLocation(cursor)
                        res.add(location)
                    } while (moveToNext())
                }
                close()
            }
            return res
        }


        fun getTableLocation() : String {
            return "locations"
        }

        fun createTableLocation() : List<String> {
            val attributes = InsertBuilder()
            attributes.addAtribute("id","INTEGER",isNull = false,isPrimaryKey = true,autoIncrement = true)
            attributes.addAtribute("latitude","DOUBLE",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("longitude","DOUBLE",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("altitude","DOUBLE",isNull = false,isPrimaryKey = false,autoIncrement = false)
            return attributes.getList()
        }

        fun newLocation(location: Location) {
            val tableName : String = getTableLocation()
            ManagerDB.getInstance()?.insert(tableName, updateTableLocation(location))
        }

        fun getLocations() : List<Location> {
            val tableName : String = LocationTableFuns.getTableLocation()
            val list = ManagerDB.getInstance()?.select(tableName, null, null,
               Companion::convertEntriesLocations
            )
            return if (list.isNullOrEmpty()) mutableListOf() else list
        }
    }
}