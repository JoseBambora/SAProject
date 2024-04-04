package com.example.coordenadsa

class MyLocationListener : LocationListener {
    fun onLocationChanged(location: Location) {
        // Obter as coordenadas (x, y, z)
        val latitude: Double = location.getLatitude()
        val longitude: Double = location.getLongitude()
        val altitude: Double = location.getAltitude()

        // ... processar as coordenadas
    }
}
