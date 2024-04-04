package com.example.application.listeners

import android.location.Location
import com.google.android.gms.location.LocationListener;
import android.util.Log

class CoordinatesListener : LocationListener {

    override fun onLocationChanged(location: Location) {
        val latitude: Double = location.getLatitude()
        val longitude: Double = location.getLongitude()
        val altitude: Double = location.getAltitude()
        Log.d("DebugApp","lat $latitude, lon $longitude, alt $altitude")
    }
}
