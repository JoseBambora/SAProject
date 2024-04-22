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
import com.example.application.network.openweather.OpenWeatherAPI
import com.example.application.model.weather.Weather
import com.example.application.utils.WeatherSensorsHelper
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
            //updateSensorData()
        } else {
            Log.e("WeatherFragment", "No address found")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayData(data: Response<Weather>) {
        val weather = data.body()
        weather?.main?.let { main ->
            view?.findViewById<TextView>(R.id.temperatureTextView)?.text = "Ambient Temperature: ${main.temperature} ºC"
            view?.findViewById<TextView>(R.id.feelsLikeTextView)?.text = "Feels Like: ${main.feels_like} ºC"
            view?.findViewById<TextView>(R.id.maxTemperatureTextView)?.text = "Max Temperature: ${main.temp_max} ºC"
            view?.findViewById<TextView>(R.id.minTemperatureTextView)?.text = "Min Temperature: ${main.temp_min} ºC"
            view?.findViewById<TextView>(R.id.pressureTextView)?.text = "Pressure: ${main.pressure} hPa"
            view?.findViewById<TextView>(R.id.humidityTextView)?.text = "Humidity: ${main.humidity}%"
            view?.findViewById<TextView>(R.id.seaLevelTextView)?.text = "Sea Level: ${main.sea_level} hPa"
            view?.findViewById<TextView>(R.id.groundLevelTextView)?.text = "Ground Level: ${main.grnd_level} hPa"
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
