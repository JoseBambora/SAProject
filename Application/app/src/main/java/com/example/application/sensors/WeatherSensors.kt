package com.example.application.sensors

import android.location.Location
import com.google.android.gms.location.LocationListener

class WeatherSensors : LocationListener {
    companion object {
        var latitude : Double = 0.0
        var longitude : Double = 0.0
        var altitude : Double = 0.0

    }
    override fun onLocationChanged(p0: Location) {
        latitude = p0.latitude
        longitude = p0.longitude
        altitude = p0.altitude
    }
}