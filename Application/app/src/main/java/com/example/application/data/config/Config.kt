package com.example.application.data.config

data class Config(val version : Int, val csstatsID : String, val bodyWeight : Float, val locationSensor : Boolean, val current_version : Int) {
    fun noMoreCurrentVersion() : Config {
        return this.copy(current_version = 0)
    }
}