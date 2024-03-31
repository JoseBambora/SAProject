package com.example.application.data

import com.google.gson.annotations.SerializedName

data class Stats(
    @SerializedName("metadata") val metaData: MetaData,
    @SerializedName("matches") val match: List<Match>)
