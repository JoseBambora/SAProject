package com.example.application.data.csstats

import com.example.application.api.DateDeserializer
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
    @SerializedName("rating") val rating : Float)
