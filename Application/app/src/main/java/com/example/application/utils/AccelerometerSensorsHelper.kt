package com.example.application.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
class AccelerometerSensorsHelper(private val context: Context) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometerSensor: Sensor? = null

    companion object {
        var accelerometerData: FloatArray = FloatArray(3)
    }

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        accelerometerSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        Log.d("AccelerometerHelper", "Initialized")
    }

    fun start() {
        accelerometerSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerData, 0, event.values.size)
            Log.d("Accelerometer", "X: ${accelerometerData[0]}, Y: ${accelerometerData[1]}, Z: ${accelerometerData[2]}")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used, but required to implement SensorEventListener
    }
}