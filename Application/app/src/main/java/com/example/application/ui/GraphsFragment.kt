package com.example.application.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.anychart.APIlib
import com.anychart.AnyChartView
import com.example.application.data.DailyActivityTableFuns
import com.example.application.databinding.FragmentGraphsBinding
import com.example.application.model.DailyActivity
import com.example.application.model.csstats.Cache
import com.example.application.ui.graphs.Converter
import com.example.application.ui.graphs.OurGraphs
import java.time.LocalDate

class GraphsFragment : Fragment() {

    private var _binding: FragmentGraphsBinding? = null

    private val binding get() = _binding!!

    private val cache : Cache = Cache.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGraphsBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDailyActivityData() : Map<LocalDate,DailyActivity>{
        return DailyActivityTableFuns.getDailyActivity()
    }
    private fun frequencyMatchPerformances(graph2 : AnyChartView) {
        val dataFrequency = cache.getPerformanceFrequency()
        val data = Converter.percentagePerformance(dataFrequency)
        val pie = OurGraphs.pie(data,"Match performance frequency","Performance")
        graph2.setChart(pie)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun histogramPhysicalActivityImpact(graph3: AnyChartView) {
        val dataMatches = cache.getDailyPerformance()
        val dataActivity = getDailyActivityData()
        val data = Converter.associationPhysicalActivity(dataMatches,dataActivity)
        val histogram = OurGraphs.histogram(data,"Daily Physical Activity Impact","Match Performance","Avg Daily Activity Score")
        graph3.setChart(histogram)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun histogramWeatherImpact(graph3: AnyChartView) {
        val dataMatches = cache.getDailyPerformance()
        val dataActivity = getDailyActivityData()
        val data = Converter.associationWeather(dataMatches,dataActivity)
        val histogram = OurGraphs.histogram3Params(data,"Weather Impact","Match Performance","Avg Weather data")
        graph3.setChart(histogram)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun scatterPlotPerformances(graph4: AnyChartView) {
        val dataMatches = cache.getDailyPerformance()
        val dataActivity = getDailyActivityData()
        val data = Converter.associationSleepingTime(dataMatches,dataActivity)
        val scatter = OurGraphs.scatterPlot(data,"Performance Daily Activity","Sleeping Minutes","Avg Daily Match Performance")
        graph4.setChart(scatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun pieSleepingTime(graph5: AnyChartView) {
        val dataActivity = getDailyActivityData()
        val data = Converter.percentageSleep(dataActivity)
        val pie = OurGraphs.pie(data,"Sleeping time frequency","Time Intervals")
        graph5.setChart(pie)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun progressOverTime(graph6: AnyChartView) {
        val dataMatches = cache.getDailyPerformance()
        val dataActivity = getDailyActivityData()
        val data = Converter.overallData(dataMatches,dataActivity)
        val lineChart = OurGraphs.lineChart(data,"Overall Graph","Date","Values","CS Performance","Physical Activity","Sleeping Time","Temperature","Humidity","Pressure")
        graph6.setChart(lineChart)
    }

    private fun drawGraph(graph : AnyChartView, progressBar: ProgressBar, function : (AnyChartView) -> Unit) {
        graph.setProgressBar(progressBar)
        APIlib.getInstance().setActiveAnyChartView(graph)
        function.invoke(graph)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        drawGraph(binding.graph2.graph,binding.graph2.progressBar,::frequencyMatchPerformances)
        drawGraph(binding.graph3.graph,binding.graph3.progressBar,::histogramPhysicalActivityImpact)
        drawGraph(binding.graph4.graph,binding.graph4.progressBar,::scatterPlotPerformances)
        drawGraph(binding.graph5.graph,binding.graph5.progressBar,::pieSleepingTime)
        drawGraph(binding.graph6.graph,binding.graph6.progressBar,::progressOverTime)
        drawGraph(binding.graph7.graph,binding.graph7.progressBar,::histogramWeatherImpact)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}