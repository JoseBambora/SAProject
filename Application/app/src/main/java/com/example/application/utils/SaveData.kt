package com.example.application.utils

import SleepData
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.application.data.DailyActivityTableFuns
import com.example.application.model.DailyActivity
import com.example.application.model.config.ConfigTableFuns
import com.example.application.model.physicalactivity.PhysicalActivity
import com.example.application.model.weather.Weather
import com.example.application.network.csstats.StatsAPI
import com.example.application.utils.physicalactivity.PhysicalActivityData
import com.example.application.utils.weather.WeatherData
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Locale

class SaveData : BroadcastReceiver() {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        private var lastInsert : LocalDate = LocalDate.now()

        @RequiresApi(Build.VERSION_CODES.O)
        fun nextInsert() : String {
            val res = lastInsert.plusDays(1).dayOfWeek.toString()
            return res[0] + res.substring(1).lowercase(Locale.ROOT)
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        saveData()
    }

    private fun resetValues() {
        WeatherData.instance.reset()
        PhysicalActivityData.instance.reset()
        SleepData.instance.reset()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertData(weather: Weather, physical: PhysicalActivity, sleepData : SleepData) {
        val today = LocalDate.now()
        val distanceRun = physical.distanceRun
        val steps = physical.steps
        val start = sleepData.getStartTime() ?: LocalDateTime.now()
        val end = sleepData.getEndTime() ?: LocalDateTime.now()
        val avgTemperature = weather.main.temperature.toFloat()
        val avgHumidity = weather.main.humidity
        val avgPressure = weather.main.pressure
        val dailyActivity = DailyActivity(
            distanceRun,
            steps,
            today,
            start,
            end,
            avgTemperature,
            avgHumidity,
            avgPressure
        )
        // val config = ConfigTableFuns.getLastVersion()
        // if(config != null)
        //     StatsAPI.getData(config.csstatsID, StatsAPI::default_suc, StatsAPI::default_err1, StatsAPI::default_err2)
        DailyActivityTableFuns.newDailyActivity(dailyActivity)
        lastInsert = LocalDate.now()
        Log.d("DebugApp", "New Daily activity on the database $dailyActivity")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveData() {
        if(LocalDate.now().dayOfWeek != lastInsert.dayOfWeek) {
            val weather = WeatherData.instance.getAvgWeather()
            val physical = PhysicalActivityData.instance.getData()
            val sleepData = SleepData.instance
            resetValues()

            if(weather != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                insertData(weather,physical,sleepData)
            }
        }
    }
}