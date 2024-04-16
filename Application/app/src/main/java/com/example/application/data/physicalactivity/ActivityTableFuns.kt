package com.example.application.data.physicalactivity

import android.content.ContentValues
import android.database.Cursor
import android.location.Location
import com.example.application.db.InsertBuilder
import com.example.application.db.ManagerDB
import java.text.SimpleDateFormat
import java.util.Date

class ActivityTableFuns {

    companion object {

        private val parser = SimpleDateFormat("yyyy-MM-dd")
        private fun updateTableActivity(activity: Activity) : ContentValues {
            val values = ContentValues().apply {
                put("latitude",activity.lan)
                put("longitude",activity.lon)
                put("altitude",activity.alt)
                put("activity", activity.act)
                put("steps",activity.steps)
                put("date", parser.format(activity.date))
            }
            return values
        }

        private fun convertEntryActivity(cursor : Cursor) : Activity {
            val latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"))
            val longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"))
            val altitude = cursor.getDouble(cursor.getColumnIndexOrThrow("altitude"))
            val activity = cursor.getString(cursor.getColumnIndexOrThrow("activity"))
            val steps = cursor.getInt(cursor.getColumnIndexOrThrow("steps"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            val parsedDate = parser.parse(date)!!
            return Activity(longitude,latitude,altitude,activity,steps,parsedDate)
        }

        private fun convertEntriesActivities(cursor : Cursor) : List<Activity> {
            val res : MutableList<Activity> = mutableListOf()
            cursor.apply {
                if (moveToFirst()) {
                    do {
                        res.add(convertEntryActivity(cursor))
                    } while (moveToNext())
                }
                close()
            }
            return res
        }


        fun getTableLocation() : String {
            return "activities"
        }

        fun createTableLocation() : List<String> {
            val attributes = InsertBuilder()
            attributes.addAtribute("id","INTEGER",isNull = false,isPrimaryKey = true,autoIncrement = true)
            attributes.addAtribute("latitude","DOUBLE",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("longitude","DOUBLE",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("altitude","DOUBLE",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("activity","STRING",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("steps","INT",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("date","String",isNull = false,isPrimaryKey = false,autoIncrement = false)
            return attributes.getList()
        }

        fun newActivity(activity: Activity) {
            val tableName : String = getTableLocation()
            ManagerDB.getInstance()?.insert(tableName, updateTableActivity(activity))
        }

        private fun getActivities() : List<Activity> {
            val tableName : String = getTableLocation()
            val list = ManagerDB.getInstance()?.select(tableName, null, null, Companion::convertEntriesActivities)
            return if (list.isNullOrEmpty()) mutableListOf() else list
        }

        fun getDailyActivity() : Map<Date,DailyActivity> {
            val aux = mutableMapOf<Date,MutableList<Activity>>()
            val activities = getActivities()
            activities.forEach {
                if(!aux.containsKey(it.date))
                    aux[it.date] = mutableListOf()
                aux[it.date]?.add(it)
            }
            return aux.mapValues { DailyActivity(it.value) }
        }
    }
}