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
    import com.example.application.model.weather.Weather
    import com.example.application.network.openweather.OpenWeatherAPI
    import com.google.android.gms.location.FusedLocationProviderClient
    import com.google.android.gms.location.LocationListener
    import com.google.android.gms.location.LocationRequest
    import com.google.android.gms.location.LocationServices
    import com.google.android.gms.location.Priority
    import retrofit2.Response

    class WeatherSensorsHelper(
        val context : Context
    ) : LocationListener {
        private val DELAY_LOCATION_SENSOR_WEATHER : Long = 10000
        private val fusedLocationClient : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        private fun getData(data: Response<Weather>) {
            val res = data.body()
            if(res != null)
                WeatherData.instance.addWeather(res)
        }
        private fun error(data: Response<Weather>) {
            Log.d("DebugApp", "Error getting the weather data")
        }

        private fun fail(t: Throwable) {
            Log.d("DebugApp", "Error getting the weather data " + t.message)
        }
        override fun onLocationChanged(p0: Location) {
            OpenWeatherAPI.getData(p0.latitude, p0.altitude,::getData,::error,::fail)
        }
        @SuppressLint("MissingPermission")
        fun onStart() {
            fusedLocationClient.requestLocationUpdates(
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, DELAY_LOCATION_SENSOR_WEATHER).build(),
                this,
                Looper.getMainLooper()
            )
        }

        // Unregister sensor listeners when not needed
        fun onStop() {
            fusedLocationClient.removeLocationUpdates(this)
        }
        fun checkPermissions(context : Context) : Boolean {
            val fineLocationPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            val coarseLocationPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    coarseLocationPermission == PackageManager.PERMISSION_GRANTED
        }
    }
