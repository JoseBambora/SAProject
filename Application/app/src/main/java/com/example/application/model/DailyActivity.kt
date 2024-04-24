package com.example.application.model

import java.time.LocalDate
import java.time.LocalDateTime

data class DailyActivity(
    val distanceRun : Float,
    val steps : Int,
    val date : LocalDate,
    val startSleepTime : LocalDateTime,
    val endSleepTime : LocalDateTime,
    val avg_temperature : Float,
    val avg_humidity : Int,
    val avg_pressure : Int){

    fun getPerformance(): Float {
        val distanceWeight = 2
        val stepsWeight = 3
        val tempWeight = 1
        val humidityWeight = 0.5f
        val pressureWeight = 0.02f
        return (distanceRun * distanceWeight) +
                (steps * stepsWeight) +
                (avg_temperature * tempWeight) +
                (avg_humidity * humidityWeight) +
                (avg_pressure * pressureWeight)
    }

    fun getPerformancePhysicalActivity(): Float {
        val distanceWeight = 2
        val stepsWeight = 3
        val score = (distanceRun * distanceWeight) + (steps * stepsWeight)
        return score
    }
}

