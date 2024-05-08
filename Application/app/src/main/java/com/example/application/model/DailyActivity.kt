package com.example.application.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Duration

data class DailyActivity(
    val distanceRun : Float,
    val steps : Int,
    val date : LocalDate,
    val startSleepTime : LocalDateTime,
    val endSleepTime : LocalDateTime,
    val avgTemperature : Float,
    val avgHumidity : Int,
    val avgPressure : Int){

    @RequiresApi(Build.VERSION_CODES.O)
    fun getPerformance(): Float {
        val distanceWeight = 2
        val stepsWeight = 3
        val sleepWeight = 2.5f
        val tempWeight = 1
        val humidityWeight = 0.5f
        val pressureWeight = 0.02f

        val sleeptime = Duration.between(startSleepTime, endSleepTime).toMinutes()
        return (distanceRun * distanceWeight) +
                (steps * stepsWeight) +
                (sleeptime * sleepWeight) +
                (avgTemperature * tempWeight) +
                (avgHumidity * humidityWeight) +
                (avgPressure * pressureWeight)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSleepingTimeMinutes() : Float {
        return Duration.between(startSleepTime, endSleepTime).toMinutes().toFloat()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getSleepingTimeHours() : Float {
        return Duration.between(startSleepTime, endSleepTime).toHours().toFloat()
    }
    fun getPerformancePhysicalActivity(): Float {
        val distanceWeight = 0.0005f
        val stepsWeight = 0.0007f
        val score = (distanceRun * distanceWeight) + (steps * stepsWeight)
        return score
    }
}

