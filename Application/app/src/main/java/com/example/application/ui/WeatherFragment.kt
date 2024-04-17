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
import com.example.application.sensors.WeatherSensorsHelper
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
        fetchWeather(WeatherSensorsHelper.latitude, WeatherSensorsHelper.longitude)
        //updateSensorData()
    }

    @SuppressLint("SetTextI18n")
    private fun displayLocation() {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        val addresses = geocoder.getFromLocation(
            WeatherSensorsHelper.latitude,
            WeatherSensorsHelper.longitude, 1)
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

        WeatherSensorsHelper.pressure.takeIf { it != 0.0f }?.let {
            view?.findViewById<TextView>(R.id.pressureTextView)?.apply {
                text = "Pressure: $it hPa"
                visibility = View.VISIBLE
            }
        }

        WeatherSensorsHelper.relativeHumidity.takeIf { it != 0.0f }?.let {
            view?.findViewById<TextView>(R.id.humidityTextView)?.apply {
                text = "Humidity: $it %"
                visibility = View.VISIBLE
            }
        }
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
