package com.example.application.model.csstats

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.Date

data class Stats(
    @SerializedName("metadata") val metaData: MetaData,
    @SerializedName("matches") val match: List<Match>) {

    fun getOverallPerformance() : Performance {
        return metaData.evaluate()
    }

    fun getMatchPerformance() : List<Performance> {
        return match.map { it.evaluate() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDayPerformance() : Map<LocalDate,Performance> {
        val res : MutableMap<LocalDate,MutableList<Performance>> = mutableMapOf()
        match.forEach {
            if(!res.containsKey(it.date))
                res[it.date] = mutableListOf()
            res[it.date]?.add(it.evaluate())
        }
        return res.mapValues { calculateAverage(it.value) }
    }

    fun getPerformanceFrequency() : List<Pair<Performance,Int>> {
        val performances = getMatchPerformance()
        val frequencyMap = performances.groupingBy { it }.eachCount()
        val pairList = frequencyMap.entries.map { it.key to it.value }
        return pairList
    }
}
