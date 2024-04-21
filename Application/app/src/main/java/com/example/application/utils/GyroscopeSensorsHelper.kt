package com.example.application.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class GyroscopeSensorsHelper(private val context: Context) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var gyroscopeSensor: Sensor? = null

    companion object {
        var gyroscopeData: FloatArray = FloatArray(3)
    }

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroscopeSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        Log.d("GyroscopeSensorHelper", "Initialized")
    }

    fun start() {
        gyroscopeSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
            System.arraycopy(event.values, 0, gyroscopeData, 0, event.values.size)
            Log.d("GyroscopeSensor", "X: ${gyroscopeData[0]}, Y: ${gyroscopeData[1]}, Z: ${gyroscopeData[2]}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used, but required to implement SensorEventListener
    }
}