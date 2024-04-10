package com.example.application.api.csstats

import com.example.application.data.csstats.Stats
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface CSStatsAPI {
    @GET("/stats/{id}")
    fun getStats(@Path("id") id : String) : Call<Stats>
}
class StatsAPI {
    companion object {
        private val api_url : String = "http://10.0.2.2:5000"
        private fun create_refroit() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(api_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        fun getData(id : String, sucess : (Response<Stats>) -> Unit, unsucess : (Response<Stats>) -> Unit, fail : (Throwable) -> Unit) {
            val retrofit = create_refroit()
            val service = retrofit.create(CSStatsAPI::class.java)
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