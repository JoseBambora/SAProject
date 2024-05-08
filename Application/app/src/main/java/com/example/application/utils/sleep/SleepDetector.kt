package com.example.application.utils.sleep

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import kotlin.math.abs

class SleepDetector {

    companion object {
        // Threshold values for sleep detection
        private const val LIGHT_THRESHOLD = 10 // Adjust as needed
        private const val ACCELEROMETER_THRESHOLD = 1.5 // Adjust as needed
        private const val GRAVITY_THRESHOLD = 1.5 // Adjust as needed
        private const val GYROSCOPE_THRESHOLD = 0.5 // Adjust as

        private var previousAccelerometerData : FloatArray? = null
        private var previousGravityData : FloatArray? = null
        private var previousGyroscopeData : FloatArray? = null



        // Sleep detection flag
        private var isSleeping: Boolean = false
        private var sleepStartTime: LocalDateTime? = null

        // Function to detect sleep based on sensor data

        private fun lowerThanThreshold(data : FloatArray, previousData : FloatArray?, threshold : Double) :Boolean {
            return if(previousData == null) false
            else {
                // difference between previous value and current data
                val dif = FloatArray(3)
                for(i in 0..2)
                    dif[i] = abs(previousData[i] - data[i])
                // Log.d("DebugApp","Dif: ${dif.sum()} ${previousData.toSet()} ${data.toSet()}")
                dif.sum() < threshold
            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        private fun isTimeToSleep() : Boolean {
            val current = LocalDateTime.now()
            return current.hour > 21 || current.hour < 3
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun detectSleep() {
            val lightIntensity = SleepSensorsHelper.sensorlight
            val accelerometerData = SleepSensorsHelper.accelerometerData
            val gravityData = SleepSensorsHelper.gravityData
            val gyroscopeData = SleepSensorsHelper.gyroscopeData

            val isDark = lightIntensity < LIGHT_THRESHOLD
            val isStationary = lowerThanThreshold(accelerometerData, previousAccelerometerData, ACCELEROMETER_THRESHOLD)
            val isLayingDown = lowerThanThreshold(gravityData, previousGravityData,GRAVITY_THRESHOLD)
            val isInactive = lowerThanThreshold(gyroscopeData, previousGyroscopeData,GYROSCOPE_THRESHOLD)

            previousAccelerometerData = accelerometerData.clone()
            previousGravityData = gravityData.clone()
            previousGyroscopeData = gyroscopeData.clone()

            // Log values for debugging
            // Log.d("DebugApp", "isDark: $lightIntensity, isStationary: $isStationary, isLayingDown: $isLayingDown, isInactive: $isInactive")
            // Log.d("SleepDetector", "isDark: $isDark, isStationary: $isStationary, isLayingDown: $isLayingDown, isInactive: $isInactive")

            // Check if conditions indicate sleep
            // if the phone is not moving and isDark is true, we have two options:
            // - the user start sleeping isTimeToSleep is true
            // - the user is already sleeping isTimeToSleep can be true or false, but isSleeping is true
            val flags = isDark && isStationary && isLayingDown && isInactive
            val sleeping = isTimeToSleep() || isSleeping
            if (flags && sleeping) {
                if (!isSleeping) {
                    // Start of sleep detected
                    isSleeping = true
                    sleepStartTime = LocalDateTime.now()
                    // Log.d("DebugApp","Started Sleeping")
                }
            } else if(isSleeping) {
                // End of sleep detected
                isSleeping = false
                val sleepEndTime = LocalDateTime.now()
                // Save sleep duration or perform further actions
                saveSleepData(sleepStartTime!!, sleepEndTime)
                // Log.d("DebugApp","End Sleeping")
            }
        }

        // Function to save sleep data or perform further actions
        private fun saveSleepData(startTime: LocalDateTime, endTime: LocalDateTime) {
            // Save sleep duration or perform further actions
            SleepData.instance.setSleepTime(startTime, endTime)
        }
    }
}
