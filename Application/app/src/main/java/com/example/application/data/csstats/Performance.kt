package com.example.application.data.csstats

enum class Performance(val value: Int) {
    Terrible(1),
    VeryBad(2),
    Bad(3),
    Medium(4),
    Good(5),
    VeryGood(6),
    Excellent(7)
}

fun calculatePerformance(factor: Float, liminf: Float, limsup: Float): Performance {
    val interval: Float = (limsup - liminf) / 7
    return when {
        factor < liminf + interval -> Performance.Terrible
        factor < liminf + interval * 2 -> Performance.VeryBad
        factor < liminf + interval * 3 -> Performance.Bad
        factor < liminf + interval * 4 -> Performance.Medium
        factor < liminf + interval * 5 -> Performance.Good
        factor < liminf + interval * 6 -> Performance.VeryGood
        else -> Performance.Excellent
    }
}

fun calculateAverage(list : List<Performance>) : Performance {
    val average = list.map { it.value }.average()
    return when {
        average < 1.5f -> Performance.Terrible
        average < 2.5f -> Performance.VeryBad
        average < 3.5f -> Performance.Bad
        average < 4.5f -> Performance.Medium
        average < 5.5f -> Performance.Good
        average < 6.5f -> Performance.VeryGood
        else -> Performance.Excellent
    }
}
