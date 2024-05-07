package com.example.application.network.csstats

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.application.model.csstats.Cache
import com.example.application.model.csstats.Stats
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
        private val api_url : String = "http://192.168.1.93:5000"
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

        @RequiresApi(Build.VERSION_CODES.O)
        fun default_suc(res : Response<Stats>) {
            Cache.getInstance().saveInfo(res)
        }

        fun default_err1(res : Response<Stats>) {
            Log.d("DebugApp","Error 1 when contacting the API")
        }
        fun default_err2(t : Throwable) {
            Log.d("DebugApp","Error 2 when contacting the API " + t.message.toString())
        }
    }
}