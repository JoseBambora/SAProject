package com.example.application.model.weather

import com.google.gson.annotations.SerializedName

/**
 * "main": {
 *     "temp": 298.48,
 *     "feels_like": 298.74,
 *     "temp_min": 297.56,
 *     "temp_max": 300.05,
 *     "pressure": 1015,
 *     "humidity": 64,
 *     "sea_level": 1015,
 *     "grnd_level": 933
 *   }
 */
data class Main(
    @SerializedName("temp") val temperature : Double,
    @SerializedName("feels_like") val feels_like : Double,
    @SerializedName("temp_max") val temp_max: Double,
    @SerializedName("temp_min") val temp_min: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("sea_level") val sea_level: Int,
    @SerializedName("grnd_level") val grnd_level: Int
    )
