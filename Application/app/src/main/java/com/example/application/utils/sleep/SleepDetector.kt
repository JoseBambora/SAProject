package com.example.application.utils.sleep

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import com.example.application.utils.sleep.SleepSensorsHelper

class SleepDetector {

    companion object {
        // Threshold values for sleep detection
        private const val LIGHT_THRESHOLD = 10 // Adjust as needed
        private const val ACCELEROMETER_THRESHOLD = 1.5 // Adjust as needed
        private const val GRAVITY_THRESHOLD = 1.5 // Adjust as needed
        private const val GYROSCOPE_THRESHOLD = 0.5 // Adjust as needed

        // Sleep detection flag
        private var isSleeping: Boolean = false
        private var sleepStartTime: LocalDateTime? = null

        // Function to update sensor data
        @RequiresApi(Build.VERSION_CODES.O)
        fun updateSensorData(sensorHelper: SleepSensorsHelper) {
            val lightIntensity = SleepSensorsHelper.sensorlight
            val accelerometerData = SleepSensorsHelper.accelerometerData
            val gravityData = SleepSensorsHelper.gravityData
            val gyroscopeData = SleepSensorsHelper.gyroscopeData

            // Log light intensity for debugging
            // Log.d("SleepDetector.updateSensorData", "Light Intensity: $lightIntensity")

            detectSleep(lightIntensity, accelerometerData, gravityData, gyroscopeData)
        }

        // Function to detect sleep based on sensor data
        @RequiresApi(Build.VERSION_CODES.O)
        private fun detectSleep(lightIntensity: Float, accelerometerData: FloatArray, gravityData: FloatArray, gyroscopeData: FloatArray) {
            val isDark = lightIntensity < LIGHT_THRESHOLD
            val isStationary = calculateAccelerationMagnitude(accelerometerData) < ACCELEROMETER_THRESHOLD
            val isLayingDown = calculateAccelerationMagnitude(gravityData) < GRAVITY_THRESHOLD
            val isInactive = calculateAccelerationMagnitude(gyroscopeData) < GYROSCOPE_THRESHOLD

            // Log values for debugging
            // Log.d("SleepDetector", "isDark: $isDark, isStationary: $isStationary, isLayingDown: $isLayingDown, isInactive: $isInactive")

            // Check if conditions indicate sleep
            if (isDark && isStationary && isLayingDown && isInactive) {
                if (!isSleeping) {
                    // Start of sleep detected
                    isSleeping = true
                    sleepStartTime = LocalDateTime.now()
                }
            } else {
                if (isSleeping) {
                    // End of sleep detected
                    isSleeping = false
                    val sleepEndTime = LocalDateTime.now()
                    // Save sleep duration or perform further actions
                    saveSleepData(sleepStartTime!!, sleepEndTime)
                }
            }
        }

        // Helper function to calculate magnitude of acceleration
        private fun calculateAccelerationMagnitude(data: FloatArray): Double {
            return kotlin.math.sqrt(data.map { it * it }.sum().toDouble())
        }

        // Function to save sleep data or perform further actions
        private fun saveSleepData(startTime: LocalDateTime, endTime: LocalDateTime) {
            // Save sleep duration or perform further actions
            SleepData.instance.setSleepTime(startTime, endTime)
        }
    }
}
