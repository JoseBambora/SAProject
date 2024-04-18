package com.example.application.utils.ActivitySensors

import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.application.model.physicalactivity.calculateDistance
import java.time.LocalDate
import kotlin.math.abs

class ActivitySensorsAux {
    private var activity = ""

    private val DIF_WALKING = 0.65f
    private val DIF_RUNNING = 5.0f
    private val DIF_STOP = 0.045f

    private var location : Location = Location("")
    private var lastAcc : Float = 0.0f

    private var distanceRun : Float = 0.0f
    private var steps : Int = 0
    @RequiresApi(Build.VERSION_CODES.O)
    private var date : LocalDate = LocalDate.now()

    private fun calculateAcceleration(x: Float, y: Float, z: Float): Float {
        val accCur = Math.sqrt((x * x + y * y + z * z) / 3.0).toFloat()
        val res = abs(lastAcc - accCur)
        lastAcc = accCur
        return res
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun reset(localDate: LocalDate) {
        distanceRun = 0.0f
        steps = 0
        this.date = localDate
    }
    fun incrementStepCounter() {
        steps++
    }

    fun newActivity(x: Float, y: Float, z: Float) {
        val difference = calculateAcceleration(x,y,z)
        this.activity = when {
            difference <= DIF_STOP -> "None"
            difference <= DIF_WALKING -> "Walking"
            difference <= DIF_RUNNING -> "Running"
            else -> "Unknown"
        }
    }

    fun newLocation(location: Location) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("DebugApp","New Location Activity $date $distanceRun $steps")
        }
        this.distanceRun += calculateDistance(this.location,location)
        this.location = location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createDailyActivity()
        }
        else {
            Log.d("DebugApp","Build Version does not support LocalDate")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDailyActivity() {
    }
}
