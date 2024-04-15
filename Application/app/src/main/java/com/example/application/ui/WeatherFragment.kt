package com.example.application.ui

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.application.R
import java.util.*
import android.widget.TextView
import com.example.application.api.openweather.OpenWeatherAPI
import com.example.application.data.weather.Weather
import com.example.application.sensors.WeatherSensors
import retrofit2.Response

class WeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayLocation()
        fetchWeather(WeatherSensors.latitude, WeatherSensors.longitude)
        //updateSensorData()
    }

    @SuppressLint("SetTextI18n")
    private fun displayLocation() {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(
            WeatherSensors.latitude,
            WeatherSensors.longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val cityName = addresses[0].locality
            val countryName = addresses[0].countryName
            val locationText = "$cityName, $countryName"
            // Display the location in your UI elements
            val locationTextView = view?.findViewById<TextView>(R.id.locationTextView)
            locationTextView?.text = "Location: $locationText"
            updateSensorData()
        } else {
            Log.e("WeatherFragment", "No address found")
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateSensorData() {
        view?.findViewById<TextView>(R.id.ambientTemperatureTextView)?.text =
            "Local Temperature: ${WeatherSensors.ambientTemperature} ºC"
        view?.findViewById<TextView>(R.id.pressureTextView)?.text =
            "Pressure: ${WeatherSensors.pressure} hPa"
        view?.findViewById<TextView>(R.id.humidityTextView)?.text =
            "Humidity: ${WeatherSensors.relativeHumidity} %"
    }

    @SuppressLint("SetTextI18n")
    private fun displayData(data: Response<Weather>) {
        val weather = data.body()
        weather?.main?.temperature?.let {
            view?.findViewById<TextView>(R.id.temperatureTextView)?.text = "Ambient Temperature: $it ºC"
        }
    }

    private fun error(data: Response<Weather>) {
        Log.d("DebugApp", "Error getting the weather data")
    }

    private fun fail(t: Throwable) {
        Log.d("DebugApp", "Error getting the weather data " + t.message)
    }

    private fun fetchWeather(latitude: Double, longitude: Double) {
        OpenWeatherAPI.getData(latitude, longitude, ::displayData, ::error, ::fail)
    }
}
