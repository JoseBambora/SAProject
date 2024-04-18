package com.example.application.model.csstats

import android.os.Build
import androidx.annotation.RequiresApi
import retrofit2.Response
import java.time.Instant
import java.util.Calendar
import java.util.Date

class Cache {
    companion object {
        private val instance : Cache = Cache()
        fun getInstance() : Cache {
            return instance
        }
    }

    private var stats : Stats? = null
    @RequiresApi(Build.VERSION_CODES.O)
    private var lastUpdate : Date? = null
    private val hoursUpdate = 24

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveInfo(stats: Response<Stats>) {
        this.stats = stats.body()
        this.lastUpdate = Date.from(Instant.now())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun needsUpdate() : Boolean{
        if (lastUpdate != null) {
            val calendar = Calendar.getInstance()
            calendar.time = lastUpdate
            calendar.add(Calendar.HOUR_OF_DAY, hoursUpdate)
            val deadline = calendar.time
            return Date.from(Instant.now()).after(deadline)
        }
        return true
    }

    fun hasInfo() : Boolean {
        return stats != null
    }

    override fun toString(): String {
        return stats.toString()
    }

    fun getOverallPerformance() : Performance? {
        return stats?.getOverallPerformance()
    }

    fun getMatchesPerformance() : List<Performance>? {
        return stats?.getMatchPerformance()
    }
    fun getDailyPerformance() : Map<Date,Performance>? {
        return stats?.getDayPerformance()
    }
}