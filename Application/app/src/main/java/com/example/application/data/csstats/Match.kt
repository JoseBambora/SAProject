package com.example.application.data.csstats

import com.example.application.api.csstats.DateDeserializer
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Match(
    @SerializedName("date") @JsonAdapter(DateDeserializer::class) val date : Date,
    @SerializedName("map") val map : String,
    @SerializedName("score") val score : String,
    @SerializedName("kills") val kills : Int,
    @SerializedName("deaths") val deaths : Int,
    @SerializedName("assists") val assists : Int,
    @SerializedName("HS") val hs: Int,
    @SerializedName("ADR") val adr : Int,
    @SerializedName("rating") val rating : Float) {

    private fun  getScore() : Pair<Int,Int> {
        val regex = Regex("""(\d+)-(\d+)""")
        val matchResult = regex.find(score)
        return if (matchResult != null) {
            val (firstNumber,secondNumber) = matchResult.destructured
            Pair(firstNumber.toInt(),secondNumber.toInt())
        } else
            Pair(0,0)
    }
    fun evaluate() : Performance {
        val result = this.getScore()
        val won : Int = result.first - result.second
        val kd : Float = (kills + assists * 0.75f) / deaths
        val adr = this.adr / 500f
        val factor = rating * 1.5f + won * 2.0f + kd + adr * 1.2f + hs * 0.2f
        return calculatePerformance(factor,-26f,33.4f)
    }
}
