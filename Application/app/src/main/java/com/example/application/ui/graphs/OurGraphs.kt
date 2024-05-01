package com.example.application.ui.graphs

import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.charts.Cartesian
import com.anychart.charts.Pie
import com.anychart.charts.Scatter
import com.anychart.core.cartesian.series.Column
import com.anychart.core.cartesian.series.Line
import com.anychart.core.scatter.series.Marker
import com.anychart.data.Mapping
import com.anychart.data.Set
import com.anychart.enums.Align
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.LegendLayout
import com.anychart.enums.MarkerType
import com.anychart.enums.Position
import com.anychart.enums.TooltipDisplayMode
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.SolidFill
import com.anychart.graphics.vector.Stroke
import com.anychart.graphics.vector.text.HAlign


class OurGraphs {
    companion object {
        fun histogram(data : List<DataEntry>, title : String, xTitle : String, yTitle : String) : Cartesian {
            val cartesian = AnyChart.column()
            val column: Column = cartesian.column(data)

            column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0.0)
                .offsetY(10000000.0)
                .format("\${%Value}{groupsSeparator: }")

            cartesian.animation(true)
            cartesian.title(title)

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
            cartesian.interactivity().hoverMode(HoverMode.BY_X)

            cartesian.xAxis(0).title(xTitle)
            cartesian.yAxis(0).title(yTitle)
            return cartesian
        }


        fun pie(data : List<DataEntry>, title: String, label : String ): Pie {

            val pie = AnyChart.pie()

            pie.data(data)

            pie.title(title)

            pie.labels().position("outside")

            pie.legend().title().enabled(true)
            pie.legend().title()
                .text(label)
                .padding(0.0, 0.0, 10.0, 0.0)

            pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER)
            return pie
        }

        fun scatterPlot(markerData : List<DataEntry>, title : String, xTitle : String, yTitle : String) : Scatter {
            val scatter = AnyChart.scatter()

            scatter.animation(true)

            scatter.title(title)

            scatter.yAxis(0).title(yTitle)
            scatter.xAxis(0)
                .title(xTitle)
                .drawFirstLabel(false)
                .drawLastLabel(false)

            scatter.interactivity()
                .hoverMode(HoverMode.BY_SPOT)
                .spotRadius(30.0)

            scatter.tooltip().displayMode(TooltipDisplayMode.UNION)

            val marker: Marker = scatter.marker(markerData)
            marker.type(MarkerType.TRIANGLE_UP)
                .size(4.0)
            marker.hovered()
                .size(7.0)
                .fill(SolidFill("gold", 1.0))
                .stroke("anychart.color.darken(gold)")
            marker.tooltip()
                .hAlign(HAlign.START)
                .format("Waiting time: \${%Value} min.\\nDuration: \${%X} min.")
            return scatter
        }
        private fun lineCharAux(cartesian : Cartesian,line : String, seriesMapping : Mapping) : Line {
            val series : Line = cartesian.line(seriesMapping)
            series.name(line)
            series.hovered().markers().enabled(true)
            series.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4.0)
            series.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5.0)
                .offsetY(5.0)
            return series;
        }
        fun lineChart(data : List<DataEntry>, title : String, xTitle: String, yTitle: String, line1: String, line2: String, line3 : String, line4: String, line5: String, line6 : String) : Cartesian {
            val cartesian = AnyChart.line()

            cartesian.animation(true)

            cartesian.padding(10.0, 20.0, 5.0, 20.0)

            cartesian.crosshair().enabled(true)
            cartesian.crosshair()
                .yLabel(true)
                .yStroke(null as Stroke?, null, null, null as String?, null as String?)

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)

            cartesian.title(title)

            cartesian.yAxis(0).title(yTitle)
            cartesian.xAxis(0).title(xTitle).labels().padding(5.0, 5.0, 5.0, 5.0)

            val set: Set = Set.instantiate()
            set.data(data)
            val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")
            val series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }")
            val series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }")
            val series4Mapping = set.mapAs("{ x: 'x', value: 'value4' }")
            val series5Mapping = set.mapAs("{ x: 'x', value: 'value5' }")
            val series6Mapping = set.mapAs("{ x: 'x', value: 'value6' }")

            lineCharAux(cartesian,line1,series1Mapping)
            lineCharAux(cartesian,line2,series2Mapping)
            lineCharAux(cartesian,line3,series3Mapping)
            lineCharAux(cartesian,line4,series4Mapping)
            lineCharAux(cartesian,line5,series5Mapping)
            lineCharAux(cartesian,line6,series6Mapping)

            cartesian.legend().enabled(true)
            cartesian.legend().fontSize(13.0)
            cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)
            return cartesian
        }
    }
}