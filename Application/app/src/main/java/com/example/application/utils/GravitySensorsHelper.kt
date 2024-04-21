package com.example.application.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class GravitySensorsHelper(private val context: Context) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var gravitySensor: Sensor? = null

    companion object {
        var gravityData: FloatArray = FloatArray(3)
    }

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gravitySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY)
        Log.d("GravitySensorHelper", "Initialized")
    }

    fun start() {
        gravitySensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GRAVITY) {
            System.arraycopy(event.values, 0, gravityData, 0, event.values.size)
            Log.d("GravitySensor", "X: ${gravityData[0]}, Y: ${gravityData[1]}, Z: ${gravityData[2]}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used, but required to implement SensorEventListener
    }
}