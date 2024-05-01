package com.example.application.utils.ActivitySensors

import android.location.Location
import com.example.application.model.physicalactivity.PhysicalActivity
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class PhysicalActivityData {

    private var location : Location = Location("")

    private var distanceRun : Float = 0.0f
    private var steps : Int = 0
    fun reset() {
        distanceRun = 0.0f
        steps = 0
    }
    fun incrementStepCounter() {
        steps++
    }

    private fun calculateDistance(location1: Location, location2: Location) : Float {
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

    fun newLocation(location: Location) {
        if(this.location.provider != "")
            this.distanceRun += calculateDistance(this.location,location)
        this.location = location
    }

    fun getData() : PhysicalActivity {
        return PhysicalActivity(steps,distanceRun)
    }

    companion object {
        val instance = PhysicalActivityData()
    }
}
