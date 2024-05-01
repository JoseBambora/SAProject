package com.example.application.utils.weather

import android.util.Log
import com.example.application.model.weather.Coord
import com.example.application.model.weather.Main
import com.example.application.model.weather.Weather

class WeatherData {

    private val list : MutableList<Weather> = mutableListOf()

    fun addWeather(weather : Weather) {list.add(weather)}

    fun getAvgWeather(): Weather? {
        if (list.isNotEmpty()) {
            val mainAttributes = list.map { it.main }
            val coordAttributes = list.map { it.coords }
            val averageLongitude = coordAttributes.map { it.longitude }.average()
            val averageLatitude = coordAttributes.map { it.latitude }.average()
            val averageTemperature = mainAttributes.map { it.temperature }.average()
            val averageFeelsLike = mainAttributes.map { it.feels_like }.average()
            val averageMaxTemperature = mainAttributes.map { it.temp_max }.average()
            val averageMinTemperature = mainAttributes.map { it.temp_min }.average()
            val averagePressure = mainAttributes.map { it.pressure }.average()
            val averageHumidity = mainAttributes.map { it.humidity }.average()
            val averageSeaLevel = mainAttributes.map { it.sea_level }.average()
            val averageGroundLevel = mainAttributes.map { it.grnd_level }.average()

            val averageCoord = Coord(averageLongitude, averageLatitude)

            val averageMain = Main(
                averageTemperature,
                averageFeelsLike,
                averageMaxTemperature,
                averageMinTemperature,
                averagePressure.toInt(),
                averageHumidity.toInt(),
                averageSeaLevel.toInt(),
                averageGroundLevel.toInt()
            )

            // Return a new Weather object with the average Main attributes
            return Weather(averageCoord,averageMain)

        } else {
            Log.d("WeatherDataWriter", "Empty Data")
            return null
        }
    }

    fun reset() {
        list.clear()
    }

    companion object {
        val instance : WeatherData = WeatherData()
    }


}