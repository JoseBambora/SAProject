package com.example.application.api

import android.icu.text.SimpleDateFormat
import android.util.Log
import com.example.application.data.Cache
import com.example.application.data.Stats
import com.google.gson.JsonDeserializer
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.Date
import java.util.Locale

interface API {
    @GET("/stats/{id}")
    fun getStats(@Path("id") id : String) : Call<Stats>
}
class StatsAPI {
    companion object {
        private val api_url : String = "http://10.0.2.2:5000"

        private fun parseDate(dateString: String?): Date? {
            if (dateString == null) return null
            val dateFormat = SimpleDateFormat("EEE dd MMM yy", Locale.ENGLISH)
            return dateFormat.parse(dateString)
        }
        private fun create_refroit() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(api_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        fun getData(id : String, sucess : (Response<Stats>) -> Unit, unsucess : (Response<Stats>) -> Unit, fail : (Throwable) -> Unit) {
            val retrofit = create_refroit()
            val service = retrofit.create(API::class.java)
            service.getStats(id).enqueue(object : Callback<Stats> {
                override fun onResponse(call: Call<Stats>, response: Response<Stats>) {
                    if (response.isSuccessful && response.body() != null)
                        sucess.invoke(response)
                    else
                        unsucess.invoke(response)
                }

                override fun onFailure(call: Call<Stats>, t: Throwable) {
                    fail.invoke(t)
                }
            })
        }
    }
}