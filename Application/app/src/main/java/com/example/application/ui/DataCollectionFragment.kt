package com.example.application.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.application.R
import com.example.application.data.DailyActivityTableFuns
import com.example.application.utils.physicalactivity.PhysicalActivityData
import com.example.application.utils.weather.WeatherData


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
        view.findViewById<TextView>(R.id.startSleeping).text = startSleep.toString()
        view.findViewById<TextView>(R.id.endSleeping).text = endSleep.toString()
        view.findViewById<TextView>(R.id.avgTemperature).text = weatherData?.main?.temperature.toString()
        view.findViewById<TextView>(R.id.avgHumidity).text = weatherData?.main?.humidity.toString()
        view.findViewById<TextView>(R.id.avgPressure).text = weatherData?.main?.pressure.toString()
        view.findViewById<TextView>(R.id.dataStored).text = DailyActivityTableFuns.getSizeData().toString()
    }
}