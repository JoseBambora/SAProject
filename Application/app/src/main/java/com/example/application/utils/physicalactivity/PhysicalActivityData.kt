package com.example.application.utils.physicalactivity

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
        location = Location("")
        distanceRun = 0.0f
        steps = 0
    }
    fun incrementStepCounter() {
        steps++
    }

    private fun calculateDistance(location1: Location, location2: Location) : Float {
        return location1.distanceTo(location2)
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
