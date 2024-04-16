package com.example.application.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.HoverMode
import com.anychart.enums.TooltipPositionMode
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Anchor
import com.anychart.enums.Position
import com.example.application.R
import com.example.application.api.csstats.StatsAPI
import com.example.application.data.config.ConfigTableFuns
import com.example.application.data.csstats.Cache
import com.example.application.data.csstats.Performance
import com.example.application.data.csstats.Stats
import com.example.application.data.physicalactivity.ActivityTableFuns
import com.example.application.data.physicalactivity.DailyActivity
import com.example.application.databinding.FragmentFirstBinding
import retrofit2.Response


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun getDataChartActivity(): List<Pair<Performance, DailyActivity?>> {
        val activities = ActivityTableFuns.getDailyActivity()
        val association = mutableListOf<Pair<Performance, DailyActivity?>>()
        Cache.getInstance().getDailyPerformance()?.forEach {
            association.add(Pair(it.value, activities[it.key]))
        }
        return association
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // teste_api()
        // binding.buttonFirst.setOnClickListener {
        //     findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        // }
        val anyChartView: AnyChartView = view.findViewById(R.id.teste)
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar))

        val cartesian = AnyChart.column()

        val data: MutableList<DataEntry> = ArrayList()
        data.add(ValueDataEntry("Rouge", 80540))
        data.add(ValueDataEntry("Foundation", 94190))
        data.add(ValueDataEntry("Mascara", 102610))
        data.add(ValueDataEntry("Lip gloss", 110430))
        data.add(ValueDataEntry("Lipstick", 128000))
        data.add(ValueDataEntry("Nail polish", 143760))
        data.add(ValueDataEntry("Eyebrow pencil", 170670))
        data.add(ValueDataEntry("Eyeliner", 213210))
        data.add(ValueDataEntry("Eyeshadows", 249980))

        val column: Column = cartesian.column(data)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("\${%Value}{groupsSeparator: }")

        cartesian.animation(true)
        cartesian.title("Top 10 Cosmetic Products by Revenue")

        cartesian.yScale().minimum(0.0)

        cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Product")
        cartesian.yAxis(0).title("Revenue")

        anyChartView.setChart(cartesian)

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun suc(res : Response<Stats>) {
        Cache.getInstance().saveInfo(res)
        Log.d("DebugApp","Guardou na cache")
        Log.d("DebugApp","Cache: " + Cache.getInstance().toString())
        Log.d("DebugApp","Cache: " + Cache.getInstance().getDailyPerformance())
    }

    fun erro1(res : Response<Stats>) {
        Log.d("DebugApp","Error 1 when contacting the API")
    }
    fun erro2(t : Throwable) {
        Log.d("DebugApp","Error 2 when contacting the API " + t.message.toString())
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun teste_api() {
        val config = ConfigTableFuns.getLastVersion()
        if(config != null) {
            if (Cache.getInstance().needsUpdate()) {
                Toast.makeText(activity,"Dados não estão em cache", Toast.LENGTH_SHORT).show()
                StatsAPI.getData(config.csstatsID,::suc,::erro1, ::erro2)
            }
            else
                Toast.makeText(activity,"Dados já em cache", Toast.LENGTH_SHORT).show()
        }

        else
            Toast.makeText(activity, "No Configs in the system", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}