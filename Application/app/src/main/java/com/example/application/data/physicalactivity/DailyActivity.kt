package com.example.application.data.physicalactivity

import android.location.Location
import java.time.LocalDate
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class DailyActivity(
    val calories : Float,
    val distanceRun : Float,
    val steps : Int,
    val date : LocalDate){

    fun getPerformance() : Float {
        val caloriesWeight = 1
        val distanceWeight = 2
        val stepsWeight = 3
        val normalizedDistance = distanceRun * 1000
        val score = (calories * caloriesWeight) + (normalizedDistance.toInt() * distanceWeight) + (steps * stepsWeight)
        return score
    }
}

fun calculateCalories(activity: String): Float {
    return when (activity) {
        "Running" -> 0.1f
        "Walking" -> 0.05f
        else -> 0.0f
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

