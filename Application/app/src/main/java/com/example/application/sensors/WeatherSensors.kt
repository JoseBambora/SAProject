    package com.example.application.sensors

    import android.hardware.Sensor
    import android.hardware.SensorEvent
    import android.hardware.SensorEventListener
    import android.hardware.SensorManager
    import android.location.Location
    import com.google.android.gms.location.LocationListener

    class WeatherSensors(
        private val sensorManager: SensorManager
    ) : LocationListener, SensorEventListener {

        companion object {


            var latitude: Double = 0.0
            var longitude: Double = 0.0
            var altitude: Double = 0.0
            var ambientTemperature: Float = 0.0f
            var pressure: Float = 0.0f
            var relativeHumidity: Float = 0.0f
        }

        init {
            sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)?.also {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
            sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)?.also {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
            sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)?.also {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // Not needed for this example
        }

        override fun onSensorChanged(event: SensorEvent) {
            when (event.sensor.type) {
                Sensor.TYPE_AMBIENT_TEMPERATURE -> ambientTemperature = event.values[0]
                Sensor.TYPE_PRESSURE -> pressure = event.values[0]
                Sensor.TYPE_RELATIVE_HUMIDITY -> relativeHumidity = event.values[0]
            }
        }

        override fun onLocationChanged(p0: Location) {
            latitude = p0.latitude
            longitude = p0.longitude
            altitude = p0.altitude
        }

        // Unregister sensor listeners when not needed
        fun unregisterListeners() {
            sensorManager.unregisterListener(this)
        }
    }
