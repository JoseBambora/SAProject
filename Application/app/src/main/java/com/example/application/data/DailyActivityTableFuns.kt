package com.example.application.data

import android.content.ContentValues
import android.database.Cursor
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.application.model.DailyActivity
import java.time.LocalDate
import java.time.LocalDateTime

class DailyActivityTableFuns {

    companion object {

        private fun updateTableDailyActivity(activity: DailyActivity) : ContentValues {
            val values = ContentValues().apply {
                put("distanceRun",activity.distanceRun)
                put("steps",activity.steps)
                put("date", activity.date.toString())
                put("start_time", activity.startSleepTime.toString())
                put("end_time", activity.endSleepTime.toString())
                put("avg_temperature", activity.avg_temperature)
                put("avg_pressure", activity.avg_pressure)
                put("avg_humidity", activity.avg_humidity)
            }
            return values
        }

        @RequiresApi(Build.VERSION_CODES.O)
        private fun convertEntryActivity(cursor : Cursor) : DailyActivity {
            val distance = cursor.getFloat(cursor.getColumnIndexOrThrow("distanceRun"))
            val steps = cursor.getInt(cursor.getColumnIndexOrThrow("steps"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            val startTime = cursor.getString(cursor.getColumnIndexOrThrow("start_time"))
            val endTime = cursor.getString(cursor.getColumnIndexOrThrow("end_time"))
            val avg_temperature = cursor.getFloat(cursor.getColumnIndexOrThrow("avg_temperature"))
            val avg_humidity = cursor.getInt(cursor.getColumnIndexOrThrow("avg_humidity"))
            val avg_pressure = cursor.getInt(cursor.getColumnIndexOrThrow("avg_pressure"))
            return DailyActivity(distance,steps, LocalDate.parse(date), LocalDateTime.parse(startTime),  LocalDateTime.parse(endTime),avg_temperature,avg_humidity,avg_pressure)
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
            attributes.addAtribute("distanceRun","FLOAT",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("steps","INT",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("date","STRING",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("start_time","STRING",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("end_time","STRING",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("avg_temperature","FLOAT",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("avg_pressure","INT",isNull = false,isPrimaryKey = false,autoIncrement = false)
            attributes.addAtribute("avg_humidity","INT",isNull = false,isPrimaryKey = false,autoIncrement = false)
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