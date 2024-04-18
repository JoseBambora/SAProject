package com.example.application.model.csstats

import com.google.gson.annotations.SerializedName

data class MetaData(
    @SerializedName("name") val player_name : String,
    @SerializedName("kpd") val kpd : Float,
    @SerializedName("rating") val rating : Float,
    @SerializedName("played") val played : Int,
    @SerializedName("won") val won : Int,
    @SerializedName("lost") val lost : Int,
    @SerializedName("tied") val tied : Int,
    @SerializedName("kills") val kills : Int,
    @SerializedName("deaths") val deaths : Int,
    @SerializedName("assists") val assists : Int,
    @SerializedName("headshots") val headshots : Int,
    @SerializedName("damage") val damage : Int,
    @SerializedName("rounds") val rounds : Int
){
    fun evaluate() : Performance {
        val kpd : Float = this.kpd / 3
        val wr : Float = won * 1.0f / played
        val hp : Float = headshots * 1.0f / kills
        val adr : Float = damage / (rounds * 500f)
        val kpr : Float = kills * 1.0f / rounds
        val factor : Float = kpd + wr * 1.5f + hp * 0.3f + adr + kpr * 0.8f
        return calculatePerformance(factor,0f,4.6f)
    }
}
