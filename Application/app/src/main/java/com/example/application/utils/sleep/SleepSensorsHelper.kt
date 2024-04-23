package com.example.application.utils.sleep

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class SleepSensorsHelper(private val context: Context) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometerSensor: Sensor? = null
    private var gravitySensor: Sensor? = null
    private var gyroscopeSensor: Sensor? = null
    private var lightSensor: Sensor? = null

    companion object {
        var accelerometerData: FloatArray = FloatArray(3)
        var gravityData: FloatArray = FloatArray(3)
        var gyroscopeData: FloatArray = FloatArray(3)
        var sensorlight: Float = 0.0f
    }

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gravitySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY)
        gyroscopeSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)
        Log.d("SleepSensorsHelper", "Initialized")
    }

    fun start() {
        accelerometerSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        gravitySensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        gyroscopeSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        lightSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                System.arraycopy(event.values, 0, accelerometerData, 0, event.values.size)
            }
            Sensor.TYPE_GRAVITY -> {
                System.arraycopy(event.values, 0, gravityData, 0, event.values.size)
            }
            Sensor.TYPE_GYROSCOPE -> {
                System.arraycopy(event.values, 0, gyroscopeData, 0, event.values.size)
            }
            Sensor.TYPE_LIGHT -> {
                sensorlight = event.values[0]
            }
        }
        // Update SleepDetector with sensor data
        SleepDetector.updateSensorData(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used, but required to implement SensorEventListener
    }
}
