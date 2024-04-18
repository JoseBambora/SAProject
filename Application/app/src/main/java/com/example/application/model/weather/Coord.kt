package com.example.application.model.weather

import com.google.gson.annotations.SerializedName

data class Coord(
    @SerializedName("lon") val longitude : Double,
    @SerializedName("lat") val latitude : Double
)
