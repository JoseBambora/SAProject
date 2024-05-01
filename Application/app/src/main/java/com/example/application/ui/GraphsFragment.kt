package com.example.application.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.application.R
import com.example.application.data.DailyActivityTableFuns
import com.example.application.databinding.FragmentSecondBinding
import com.example.application.model.config.Config
import com.example.application.model.config.ConfigTableFuns
import com.example.application.model.csstats.Cache
import com.example.application.model.DailyActivity
import java.time.LocalDate


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class GraphsFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val cache : Cache = Cache.getInstance()
    private val config : Config? = ConfigTableFuns.getLastVersion()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun fakeDataActivity() : Map<LocalDate, DailyActivity>{
        val yesterday = LocalDate.of(2024,4,20)
        val day_18 = LocalDate.of(2024,4,18)
        val day_14 = LocalDate.of(2024,4,14)
        val day_12 = LocalDate.of(2024,4,12)
        val day_11 = LocalDate.of(2024,4,11)

        val data_activity = mutableMapOf<LocalDate, DailyActivity>()
        data_activity.put(yesterday,
            DailyActivity(20000f,5000,yesterday , yesterday.minusDays(1).atTime(22,0), yesterday.atTime(8,0), 20f,10, 1)
        )
        data_activity.put(day_18,
            DailyActivity(20000f,5000,day_18 , day_18.minusDays(1).atTime(22,0), day_18.atTime(8,0), 20f,10, 1)
        )
        data_activity.put(day_14,
            DailyActivity(500f,100,day_14 , day_14.minusDays(1).atTime(22,0), day_14.atTime(8,0), 20f,10, 1)
        )
        data_activity.put(day_12,
            DailyActivity(10000f,1000,day_12 , day_12.minusDays(1).atTime(22,0), day_12.atTime(8,0), 20f,10, 1)
        )
        data_activity.put(day_11,
            DailyActivity(22000f,5500,day_11 , day_11.minusDays(1).atTime(22,0), day_11.atTime(8,0), 20f,10, 1)
        )
        return data_activity
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDailyActivityData() : Map<LocalDate,DailyActivity>{
        return DailyActivityTableFuns.getDailyActivity()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun histogramPerformanceActivity(graph1 : AnyChartView) {

        val cartesian = AnyChart.column()

        val data_matches = cache.getDailyPerformance()
        val data_activity = getDailyActivityData()

        Log.d("DebugApp", data_activity.keys.toString())
        Log.d("DebugApp", data_matches?.keys.toString())

        binding.numberDaysDataBase.text = data_activity.size.toString()


        val data: MutableList<DataEntry> = ArrayList()
        if(data_matches != null) {
            data_matches.forEach {
                if(data_activity.containsKey(it.key))
                    data.add(ValueDataEntry(data_activity.get(it.key)?.getPerformance(), it.value.value))
            }
        }

        val column: Column = cartesian.column(data)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(10000000.0)
            .format("\${%Value}{groupsSeparator: }")

        cartesian.animation(true)
        cartesian.title("Histogram Performance Daily Activity")
        cartesian.yScale().minimum(0.0)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Score Daily Activity")
        cartesian.yAxis(0).title("Avg Daily Match performance")

        graph1.setChart(cartesian)
    }

    fun frequencyMatchPerformances(graph2 : AnyChartView) {
        val data_frequency = cache.getPerformanceFrequency()
        val pie = AnyChart.pie()
        val data : MutableList<DataEntry> = mutableListOf()
        if(data_frequency != null) {
            data_frequency.forEach {
                data.add(ValueDataEntry(it.first.name,it.second))
            }
        }

        pie.data(data)

        pie.title("Match performance frequency")

        pie.labels().position("outside")

        pie.legend().title().enabled(true)
        pie.legend().title()
            .text("Performance")
            .padding(0.0, 0.0, 10.0, 0.0)

        pie.legend()
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(Align.CENTER)

        graph2.setChart(pie)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun histogramPhysicalActivityImpact(graph3: AnyChartView) {
        val cartesian = AnyChart.column()

        val data_matches = cache.getDailyPerformance()
        val data_activity = getDailyActivityData()

        Log.d("DebugApp", data_activity.keys.toString())
        Log.d("DebugApp", data_matches?.keys.toString())


        val data: MutableList<DataEntry> = ArrayList()
        if(data_matches != null) {
            data_matches.forEach {
                if(data_activity.containsKey(it.key))
                    data.add(ValueDataEntry(data_activity.get(it.key)?.getPerformancePhysicalActivity(), it.value.value))
            }
        }

        val column: Column = cartesian.column(data)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(10000000.0)
            .format("\${%Value}{groupsSeparator: }")

        cartesian.animation(true)
        cartesian.title("Histogram Performance Daily Physical Activity")
        cartesian.yScale().minimum(0.0)

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Score Daily Physical Activity")
        cartesian.yAxis(0).title("Avg Daily Match performance")

        graph3.setChart(cartesian)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val graph1: AnyChartView = view.findViewById(R.id.graph1)
        val graph2: AnyChartView = view.findViewById(R.id.graph2)
        val graph3: AnyChartView = view.findViewById(R.id.graph3)
        graph1.setProgressBar(view.findViewById(R.id.progress_bar_1))
        graph2.setProgressBar(view.findViewById(R.id.progress_bar_2))
        graph3.setProgressBar(view.findViewById(R.id.progress_bar_3))
        APIlib.getInstance().setActiveAnyChartView(graph1)
        histogramPerformanceActivity(graph1)
        APIlib.getInstance().setActiveAnyChartView(graph2)
        frequencyMatchPerformances(graph2)
        APIlib.getInstance().setActiveAnyChartView(graph3)
        histogramPhysicalActivityImpact(graph3)
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}