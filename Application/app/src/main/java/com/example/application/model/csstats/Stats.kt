package com.example.application.model.csstats

import com.google.gson.annotations.SerializedName
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

    fun getDayPerformance() : Map<Date,Performance> {
        val res : MutableMap<Date,MutableList<Performance>> = mutableMapOf()
        match.forEach {
            if(!res.containsKey(it.date))
                res[it.date] = mutableListOf()
            res[it.date]?.add(it.evaluate())
        }
        return res.mapValues { calculateAverage(it.value) }
    }
}
