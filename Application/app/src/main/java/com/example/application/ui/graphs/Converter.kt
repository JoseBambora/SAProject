package com.example.application.ui.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.example.application.model.DailyActivity
import com.example.application.model.csstats.Performance
import java.time.LocalDate

class Converter {
    companion object {
        private fun association(dataMatches : Map<LocalDate,Performance>?, dataActivity : Map<LocalDate,Float>)  : List<DataEntry> {
            val data: MutableList<DataEntry> = ArrayList()
            dataMatches?.forEach {
                if(dataActivity.containsKey(it.key))
                    data.add(ValueDataEntry(dataActivity.get(it.key), it.value.value))
            }
            return data
        }
        private fun getDaysPerformance(dataMatches : Map<LocalDate,Performance>?) : Map<Performance,List<LocalDate>> {
            val performanceValues = mutableMapOf<Performance,MutableList<LocalDate>>()
            dataMatches?.forEach{
                if(!performanceValues.containsKey(it.value))
                    performanceValues[it.value] = mutableListOf()
                performanceValues[it.value]?.add(it.key)
            }
            return performanceValues
        }
        private fun association2(performanceValues : Map<Performance,List<LocalDate>> , dataActivity : Map<LocalDate,Float>) : List<DataEntry> {
            val data: MutableList<DataEntry> = ArrayList()
            performanceValues.forEach{ pdates ->
                val avg = dataActivity.filter { it.key in pdates.value  }
                    .map { it.value }
                    .average()
                data.add(ValueDataEntry(pdates.key.name,avg))
            }
            return data
        }
        private fun association3(performanceValues : Map<Performance,List<LocalDate>> , dataActivity : Map<LocalDate,Triple<Float,Int,Int>>) : List<DataEntry> {
            val data: MutableList<DataEntry> = ArrayList()
            performanceValues.forEach{ pdates ->
                val filter = dataActivity.filter { it.key in pdates.value  }
                val avgWeather = filter.map { it.value.first }.average()
                val avgHumidity = filter.map { it.value.second }.average()
                val avgPressure = filter.map { it.value.third }.average()
                data.add(DataEntryWeather(pdates.key.name,avgWeather, avgHumidity,avgPressure))
            }
            return data
        }
        fun associationPhysicalActivity(dataMatches : Map<LocalDate,Performance>?, dataActivity : Map<LocalDate,DailyActivity>) : List<DataEntry> {
            val performanceValues = getDaysPerformance(dataMatches)
            val dataActivityScore = dataActivity.mapValues { it.value.getPerformancePhysicalActivity() }
            return association2(performanceValues,dataActivityScore)
        }

        fun associationWeather(dataMatches : Map<LocalDate,Performance>?, dataActivity : Map<LocalDate,DailyActivity>) : List<DataEntry> {
            val performanceValues = getDaysPerformance(dataMatches)
            val weatherData = dataActivity.mapValues { Triple(it.value.avgTemperature,it.value.avgHumidity,it.value.avgPressure) }
            return association3(performanceValues,weatherData)
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun associationSleepingTime(dataMatches : Map<LocalDate,Performance>?, dataActivity : Map<LocalDate,DailyActivity>) : List<DataEntry> {
            return association(dataMatches,dataActivity.mapValues { it.value.getSleepingTimeMinutes() })
        }
        fun percentagePerformance(dataMatches : List<Pair<Performance, Int>>?) : List<DataEntry> {
            val data : MutableList<DataEntry> = mutableListOf()
            dataMatches?.forEach {
                data.add(ValueDataEntry(it.first.name,it.second))
            }
            return data
        }
        private fun timeIntervals(hours : Float) : String {
            return when {
                hours < 2 -> "0-2"
                hours < 4 -> "2-4"
                hours < 6 -> "4-6"
                hours < 8 -> "6-8"
                hours < 10 -> "8<10"
                else -> "10-24"
            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun percentageSleep(dataActivity : Map<LocalDate,DailyActivity>) : List<DataEntry> {
            val data : MutableList<DataEntry> = mutableListOf()
            dataActivity.map { it.value.getSleepingTimeHours() }.map { timeIntervals(it) }.groupBy { it }.forEach { data.add(ValueDataEntry(it.key,it.value.size)) }
            return data
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun overallData(dataMatches : Map<LocalDate,Performance>?, dataActivity : Map<LocalDate,DailyActivity>) : List<DataEntry> {
            val data : MutableList<DataEntryOverall> = mutableListOf()
            dataMatches?.forEach {
                if(dataActivity.containsKey(it.key)) {
                    val act = dataActivity[it.key]!!
                    data.add(DataEntryOverall(it.key.toString(),it.value.value,act.getPerformancePhysicalActivity(),act.getSleepingTimeHours(),act.avgTemperature,act.avgHumidity,act.avgPressure))
                }
            }
            return data.sortedBy { it.x }
        }
    }


    private class DataEntryOverall(
        val x: String,
        value: Number,
        value2: Number,
        value3: Number,
        value4: Number,
        value5: Number,
        value6: Number
    ) :
        ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
            setValue("value3", value3)
            setValue("value4", value4)
            setValue("value5", value5)
            setValue("value6", value6)
        }
    }

    private class DataEntryWeather(
        val x: String,
        value: Number,
        value2: Number,
        value3: Number
    ) :
        ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
            setValue("value3", value3)
        }
    }

}