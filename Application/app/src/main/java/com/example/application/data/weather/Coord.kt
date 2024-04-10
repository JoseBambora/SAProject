package com.example.application.data.weather

import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lon") val longitude : Double,
    @SerializedName("lat") val latitude : Double
)
