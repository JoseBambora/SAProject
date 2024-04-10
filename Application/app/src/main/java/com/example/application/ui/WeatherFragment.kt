package com.example.application.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.application.R
import java.util.*
import android.widget.TextView
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherFragment : Fragment() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1001
    private var locationManager: LocationManager? = null
    private val OPEN_WEATHER_MAP_API_KEY = "ec348bace2ece84a869476a3febb7784"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is already granted, so fetch the location
            fetchLocation()
        } else {
            // Request location permission
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission granted, fetch location
            fetchLocation()
        }
    }

    private fun fetchLocation() {
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (locationManager != null && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                0f,
                locationListener
            )
        }
    }

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            locationManager?.removeUpdates(this)
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val cityName = addresses?.get(0)?.locality
            val countryName = addresses?.get(0)?.countryName
            val locationText = "$cityName, $countryName"
            displayLocation(locationText)

            fetchWeather(location.latitude, location.longitude)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }

    private fun displayLocation(location: String) {
        // Display the location in your UI elements
        val locationTextView = view?.findViewById<TextView>(R.id.locationTextView)
        if (locationTextView != null) {
            locationTextView.text = location
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager?.removeUpdates(locationListener)
    }

    private fun fetchWeather(latitude: Double, longitude: Double) {
        val apiUrl = "https://api.openweathermap.org/data/2.5/weather?lat=$latitude&lon=$longitude&appid=$OPEN_WEATHER_MAP_API_KEY&units=metric"
        FetchWeatherTask().execute(apiUrl)
    }

    @SuppressLint("StaticFieldLeak")
    private inner class FetchWeatherTask : AsyncTask<String, Void, String>() {
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: String?): String {
            val apiUrl = params[0]
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            connection.requestMethod = "GET"
            connection.connect()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = StringBuilder()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()
                return response.toString()
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!result.isNullOrEmpty()) {
                val jsonObject = JSONObject(result)
                val main = jsonObject.getJSONObject("main")
                val temperature = main.getDouble("temp")
                displayTemperature(temperature)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayTemperature(temperature: Double) {

        val temperatureText = view?.findViewById<TextView>(R.id.temperatureTextView)
        if (temperatureText != null) {
            temperatureText.text = "$temperature ºC"
        }
    }
}
