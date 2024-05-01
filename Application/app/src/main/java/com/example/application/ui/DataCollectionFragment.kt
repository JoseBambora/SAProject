package com.example.application.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.application.R
import com.example.application.utils.physicalactivity.PhysicalActivityData
import com.example.application.utils.weather.WeatherData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


class DataCollectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_data_collection, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayData(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayData(view : View) {
        val physicalData = PhysicalActivityData.instance.getData()
        val weatherData = WeatherData.instance.getAvgWeather()
        val startSleep = SleepData.instance.getStartTime()
        val endSleep = SleepData.instance.getEndTime()
        view.findViewById<TextView>(R.id.distanceRun).text = physicalData.distanceRun.toString()
        view.findViewById<TextView>(R.id.stepsCounter).text = physicalData.steps.toString()
        view.findViewById<TextView>(R.id.startSleeping).text = formatDate(startSleep)
        view.findViewById<TextView>(R.id.endSleeping).text = formatDate(endSleep)
        view.findViewById<TextView>(R.id.avgTemperature).text = weatherData?.main?.temperature.toString()
        view.findViewById<TextView>(R.id.avgHumidity).text = weatherData?.main?.humidity.toString()
        view.findViewById<TextView>(R.id.avgPressure).text = weatherData?.main?.pressure.toString()
        //view.findViewById<TextView>(R.id.dataStored).text = DailyActivityTableFuns.getSizeData().toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(dateTime: LocalDateTime?): String {
        if (dateTime == null) return ""

        val today = LocalDate.now()
        val date = dateTime.toLocalDate()
        return when (val daysDiff = ChronoUnit.DAYS.between(today, date)) {
            0L -> "${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))} (today)"
            1L -> "${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))} (yesterday)"
            in Long.MIN_VALUE until 0 -> "${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))} (${daysDiff * -1} days ago)"
            else -> "${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))} ($daysDiff days in the future)"
        }
    }



}