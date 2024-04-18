package com.example.application.data.physicalactivity

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.application.db.InsertBuilder
import com.example.application.db.ManagerDB
import java.time.LocalDate

class DailyActivityTableFuns {

    companion object {

        private fun updateTableDailyActivity(activity: DailyActivity) : ContentValues {
            val values = ContentValues().apply {
                put("calories",activity.calories)
                put("distanceRun",activity.distanceRun)
                put("steps",activity.steps)
                put("date", activity.date.toString())
            }
            return values
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun convertEntryActivity(cursor : Cursor) : DailyActivity {
            val calories = cursor.getFloat(cursor.getColumnIndexOrThrow("calories"))
            val distance = cursor.getFloat(cursor.getColumnIndexOrThrow("distanceRun"))
            val steps = cursor.getInt(cursor.getColumnIndexOrThrow("steps"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            return DailyActivity(calories,distance,steps, LocalDate.parse(date))
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun convertEntriesActivities(cursor : Cursor) : List<DailyActivity> {
            val res : MutableList<DailyActivity> = mutableListOf()
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
            return "dailyactivities"
        }

        fun createTableDailyActivity() : List<String> {
            val attributes = InsertBuilder()
            attributes.addAtribute("id","INTEGER",isNull = false,isPrimaryKey = true,autoIncrement = true)
            attributes.addAtribute("calories","FLOAT",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("distanceRun","FLOAT",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("steps","INT",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("date","String",isNull = false,isPrimaryKey = false,autoIncrement = false)
            return attributes.getList()
        }

        fun newDailyActivity(dailyActivity: DailyActivity) {
            val tableName : String = getTableLocation()
            ManagerDB.getInstance()?.insert(tableName, updateTableDailyActivity(dailyActivity))
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun getDailyActivity() : List<DailyActivity> {
            val tableName : String = getTableLocation()
            val list = ManagerDB.getInstance()?.select(tableName, null, null, Companion::convertEntriesActivities)
            return if (list.isNullOrEmpty()) mutableListOf() else list
        }
    }
}