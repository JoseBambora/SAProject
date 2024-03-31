package com.example.application.data

import com.google.gson.annotations.SerializedName

data class MetaData(
    @SerializedName("name") val player_name : String,
    @SerializedName("kpd") val kpd : Float,
    @SerializedName("raing") val rating : Float,
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
)
