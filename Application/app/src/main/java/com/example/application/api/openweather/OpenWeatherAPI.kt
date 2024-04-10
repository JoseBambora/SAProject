package com.example.application.api.openweather

import com.example.application.data.weather.Weather
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPIInterface {
    @GET("data/2.5/weather")
    fun getWeatherConditions(@Query("lat") lat : Double, @Query("lon") lon : Double, @Query("appid") appid : String, @Query("units") units : String) : Call<Weather>
}

class OpenWeatherAPI {
    companion object {
        private val api_url : String = "https://api.openweathermap.org/"
        private val OPEN_WEATHER_MAP_API_KEY = "ec348bace2ece84a869476a3febb7784"
        private val unit: String = "metric"
        private fun create_refroit() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(OpenWeatherAPI.api_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun getData(lat : Double, lon : Double, sucess : (Response<Weather>) -> Unit, unsucess : (Response<Weather>) -> Unit, fail : (Throwable) -> Unit) {
            val retrofit = OpenWeatherAPI.create_refroit()
            val service = retrofit.create(OpenWeatherAPIInterface::class.java)
            service.getWeatherConditions(lat,lon, OPEN_WEATHER_MAP_API_KEY,unit).enqueue(object :
                Callback<Weather> {
                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    if (response.isSuccessful && response.body() != null)
                        sucess.invoke(response)
                    else
                        unsucess.invoke(response)
                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    fail.invoke(t)
                }
            })
        }
    }
}