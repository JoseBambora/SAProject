    package com.example.application.utils

    import android.annotation.SuppressLint
    import android.content.Context
    import android.content.pm.PackageManager
    import android.hardware.Sensor
    import android.hardware.SensorEvent
    import android.hardware.SensorEventListener
    import android.hardware.SensorManager
    import android.location.Location
    import android.os.Looper
    import android.util.Log
    import androidx.core.content.ContextCompat
    import com.google.android.gms.location.FusedLocationProviderClient
    import com.google.android.gms.location.LocationListener
    import com.google.android.gms.location.LocationRequest
    import com.google.android.gms.location.LocationServices
    import com.google.android.gms.location.Priority

    class WeatherSensorsHelper(
        val context : Context
    ) : LocationListener, SensorEventListener {
        private val DELAY_LOCATION_SENSOR_WEATHER : Long = 1000
        private val sensorManager : SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        private val pressureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        private val humiditySensor : Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        private val fusedLocationClient : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        companion object {
            var latitude: Double = 0.0
            var longitude: Double = 0.0
            var altitude: Double = 0.0
            var pressure: Float = 0.0f
            var relativeHumidity: Float = 0.0f
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not needed for this example
        }

        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_PRESSURE -> pressure = event.values[0]
                Sensor.TYPE_RELATIVE_HUMIDITY -> relativeHumidity = event.values[0]
            }
        }

        override fun onLocationChanged(p0: Location) {
            Log.d("DebugApp","New Location Weather.")
            latitude = p0.latitude
            longitude = p0.longitude
            altitude = p0.altitude
        }
        @SuppressLint("MissingPermission")
        fun onStart() {
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL)
            fusedLocationClient.requestLocationUpdates(
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, DELAY_LOCATION_SENSOR_WEATHER).build(),
                this,
                Looper.getMainLooper()
            )
        }

        // Unregister sensor listeners when not needed
        fun onStop() {
            sensorManager.unregisterListener(this)
        }
        fun checkPermissions(context : Context) : Boolean {
            val fineLocationPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            val coarseLocationPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    coarseLocationPermission == PackageManager.PERMISSION_GRANTED
        }
    }
