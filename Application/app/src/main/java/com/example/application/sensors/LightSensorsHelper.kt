// LightSensorsHelper.kt
package com.example.application.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.application.ui.MainActivity

class LightSensorsHelper(private val context: Context) : SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var lightSensor: Sensor? = null
    private var mainActivity: MainActivity? = null

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (context is MainActivity) {
            mainActivity = context
        } else {
            Log.e("LightSensorsHelper", "Context is not an instance of MainActivity")
        }
    }

    fun start() {
        lightSensor?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    fun stop() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            val lightIntensity = event.values[0]
            Log.d("LightSensor", "Light intensity: $lightIntensity lux")
            mainActivity?.updateLightIntensityTextView(lightIntensity)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used, but required to implement SensorEventListener
    }
}
