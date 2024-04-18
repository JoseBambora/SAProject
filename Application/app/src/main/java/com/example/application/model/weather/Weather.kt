package com.example.application.model.weather

import com.google.gson.annotations.SerializedName


data class Weather(
    @SerializedName("coord") val coords : Coord,
    @SerializedName("main") val main : Main
)