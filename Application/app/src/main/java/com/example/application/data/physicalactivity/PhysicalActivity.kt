package com.example.application.data.physicalactivity

import kotlin.math.abs

class PhysicalActivity {
    private var step_counter = 0
    private var activity = ""

    private val DIF_WALKING = 0.65f
    private val DIF_RUNNING = 5.0f
    private val DIF_STOP = 0.045f

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

    fun newActivity(x: Float, y: Float, z: Float) : Boolean {
        val difference = calculateAcceleration(x,y,z)
        val activity = when {
            difference <= DIF_STOP -> "Stationary"
            difference <= DIF_WALKING -> "Walking"
            difference <= DIF_RUNNING -> "Running"
            else -> "Unknown"
        }
        if(activity != this.activity)
        {
            this.activity = activity
            return true
        }
        return  false
    }

    fun getActivity() : String {
        return activity
    }

}
