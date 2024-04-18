package com.example.application.model.physicalactivity

import android.location.Location
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class DailyActivity(
    val distanceRun : Float,
    val steps : Int,
    val date : LocalDate,
    val startSleepTime : LocalDateTime,
    val endSleepTime : LocalDateTime,
    val avg_temperature : Float,
    val avg_humidity : Float,
    val avg_pressure : Float){

    fun getPerformance() : Float {
        val distanceWeight = 2
        val stepsWeight = 3
        val normalizedDistance = distanceRun * 1000
        val score = (normalizedDistance * distanceWeight) + (steps * stepsWeight)
        return score
    }
}


fun calculateDistance(location1: Location, location2: Location) : Float {
    val R = 6371
    val lon1Rad = Math.toRadians(location1.longitude)
    val lat1Rad = Math.toRadians(location1.latitude)
    val lon2Rad = Math.toRadians(location2.longitude)
    val lat2Rad = Math.toRadians(location2.latitude)
    val lonDiff = lon2Rad - lon1Rad
    val latDiff = lat2Rad - lat1Rad
    val a: Double = sin(latDiff / 2).pow(2.0) + cos(lat1Rad) * cos(lat2Rad) * sin(lonDiff / 2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val distance = R * c
    return (distance * 1000 + abs(location1.altitude - location2.altitude)).toFloat()
}

