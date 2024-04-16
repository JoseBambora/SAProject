package com.example.application.data.physicalactivity

import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class DailyActivity(activities : List<Activity>) {
    private val calories : Float
    private val distanceRun : Float
    private val steps : Int

    init {
        calories = activities.map { calculateCaloriesBurned(it.steps,it.act)}.sum()
        distanceRun = totalDistance(activities)
        steps = activities.map { it.steps }.sum()
    }

    fun getPerformance() : Float {
        val caloriesWeight = 1
        val distanceWeight = 2
        val stepsWeight = 3
        val normalizedDistance = distanceRun * 1000
        val score = (calories * caloriesWeight) + (normalizedDistance.toInt() * distanceWeight) + (steps * stepsWeight)
        return score
    }
}

fun calculateCaloriesBurned(steps: Int, activity: String): Float {
    val caloriesPerStep: Float = when (activity) {
        "Running" -> 0.1f
        "Walking" -> 0.05f
        else -> 0.0f
    }
    return steps * caloriesPerStep
}

fun distance(activity1: Activity, activity2: Activity) : Float {
    val R = 6371
    val lon1Rad = Math.toRadians(activity1.lon)
    val lat1Rad = Math.toRadians(activity1.lan)
    val lon2Rad = Math.toRadians(activity2.lon)
    val lat2Rad = Math.toRadians(activity2.lan)
    val lonDiff = lon2Rad - lon1Rad
    val latDiff = lat2Rad - lat1Rad
    val a: Double = sin(latDiff / 2).pow(2.0) + cos(lat1Rad) * cos(lat2Rad) * sin(lonDiff / 2).pow(2.0)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    val distance = R * c
    return (distance * 1000 + abs(activity1.alt - activity2.alt)).toFloat()
}

fun totalDistance(activities: List<Activity>) : Float {
    var totalDistance = 0.0f
    for (i in 1 until activities.size) {
        val activity1 = activities[i - 1]
        val activity2 = activities[i]
        totalDistance += distance(activity1, activity2)
    }
    return totalDistance
}

