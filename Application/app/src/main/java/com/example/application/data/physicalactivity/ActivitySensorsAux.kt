package com.example.application.data.physicalactivity

import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.util.Date
import kotlin.math.abs

class ActivitySensorsAux {
    private var step_counter = 0
    private var activity = ""

    private val DIF_WALKING = 0.65f
    private val DIF_RUNNING = 5.0f
    private val DIF_STOP = 0.045f

    private var location : Location = Location("")

    private var lastAcc : Float = 0.0f

    fun incrementStepCounter() {
        step_counter++
    }

    private fun calculateAcceleration(x: Float, y: Float, z: Float): Float {
        val accCur = Math.sqrt((x * x + y * y + z * z) / 3.0).toFloat()
        val res = abs(lastAcc - accCur)
        lastAcc = accCur
        return res
    }

    fun newActivity(x: Float, y: Float, z: Float) {
        val difference = calculateAcceleration(x,y,z)
        val activity = when {
            difference <= DIF_STOP -> "None"
            difference <= DIF_WALKING -> "Walking"
            difference <= DIF_RUNNING -> "Running"
            else -> "Unknown"
        }
        if(activity != this.activity)
            this.activity = activity
    }

    fun newLocation(location: Location) {
        this.location = location
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createActivity() : Activity {
        val res = Activity(location.longitude,location.latitude,location.altitude,activity,step_counter, Date.from(Instant.now()))
        step_counter = 0
        return res
    }

}
