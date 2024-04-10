package com.example.application.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.location.Location
import com.google.android.gms.location.LocationListener;
import android.util.Log
import com.example.application.data.location.LocationTableFuns
import com.example.application.data.physicalactivity.PhysicalActivity
class ActivitySensors(private val onChange : (String) -> Unit) : LocationListener, SensorEventListener {

    companion object {
        val physicalActivity : PhysicalActivity = PhysicalActivity()
    }

    override fun onLocationChanged(location: Location) {
        Log.d("DebugApp","New Location. Inserting it")
        LocationTableFuns.newLocation(location)
    }



    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            physicalActivity.incrementStepCounter()
        }
        else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            physicalActivity.newActivity(event.values[0], event.values[1], event.values[2])
            val act = physicalActivity.getActivity()
            Log.d("DebugApp",act)
            onChange.invoke(physicalActivity.getActivity())
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
    }
}
