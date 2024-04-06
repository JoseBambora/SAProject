package com.example.application.listeners

import android.location.Location
import com.google.android.gms.location.LocationListener;
import android.util.Log
import com.example.application.data.location.LocationTableFuns

class CoordinatesListener : LocationListener {

    override fun onLocationChanged(location: Location) {
        Log.d("DebugApp","New Location. Inserting it")
        LocationTableFuns.newLocation(location)
    }
}
