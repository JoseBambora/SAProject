package com.example.application.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.application.data.DailyActivityTableFuns
import com.example.application.model.DailyActivity
import com.example.application.model.physicalactivity.PhysicalActivity
import com.example.application.model.weather.Weather
import com.example.application.utils.ActivitySensors.PhysicalActivityData
import java.time.LocalDate

class SaveData : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        //saveData()
    }

    private fun resetValues() {
        WeatherData.instance.reset()
        PhysicalActivityData.instance.reset()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertData(weather: Weather, physical: PhysicalActivity) {
        val today = LocalDate.now()
        val distanceRun = physical.distanceRun
        val steps = physical.steps
        // TO DO
        val start_sleeping = LocalDate.now().atStartOfDay()
        val end_sleeping = LocalDate.now().atStartOfDay()
        val avg_temperature = weather.main.temperature.toFloat()
        val avg_humidity = weather.main.humidity
        val avg_pressure = weather.main.pressure
        val dailyActivity = DailyActivity(distanceRun,steps,today ,start_sleeping,end_sleeping,avg_temperature,avg_humidity,avg_pressure)
        DailyActivityTableFuns.newDailyActivity(dailyActivity)
    }

    private fun saveData() {
        val weather = WeatherData.instance.getAvgWeather()
        val physical = PhysicalActivityData.instance.getData()
        resetValues()

        if(weather != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           insertData(weather,physical)
        }
    }
}