package com.example.application.sensors

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationListener
import androidx.core.content.ContextCompat
import com.example.application.data.physicalactivity.ActivitySensorsAux
import com.example.application.ui.utils.Delays
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class ActivitySensorsHelper(val context: Context) : LocationListener, SensorEventListener {

    private val sensorManager : SensorManager
    private val accelerometerSensor : Sensor?
    private val stepCounterSensor : Sensor?
    private val fusedLocationClient : FusedLocationProviderClient
    private val physicalActivity : ActivitySensorsAux = ActivitySensorsAux()

    init {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    override fun onLocationChanged(location: Location) {
        physicalActivity.newLocation(location)
    }


    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR)
            physicalActivity.incrementStepCounter()
        else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER)
            physicalActivity.newActivity(event.values[0], event.values[1], event.values[2])
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    @SuppressLint("MissingPermission")
    fun onStart() {
        if(checkPermissions(context)) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
            fusedLocationClient.requestLocationUpdates(
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, Delays.DELAY_LOCATION_SENSOR_ACTIVITY).build(),
                this,
                Looper.getMainLooper()
            )
        }
    }

    fun onStop() {
        if(checkPermissions(context)) {
            sensorManager.unregisterListener(this)
            fusedLocationClient.removeLocationUpdates(this)
        }
    }

    fun checkPermissions(context : Context) : Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        val activityRecognitionPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACTIVITY_RECOGNITION)
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
                activityRecognitionPermission == PackageManager.PERMISSION_GRANTED
    }
}
