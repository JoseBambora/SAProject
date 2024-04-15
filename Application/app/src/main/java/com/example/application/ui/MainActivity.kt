package com.example.application.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.application.R
import com.example.application.databinding.ActivityMainBinding
import com.example.application.db.ManagerDB
import com.example.application.sensors.ActivitySensors
import com.example.application.sensors.WeatherSensors
import com.example.application.ui.utils.Codes
import com.example.application.ui.utils.Delays
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var activitySensors: ActivitySensors
    private lateinit var weatherSensors : WeatherSensors

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ManagerDB.initialize(this.application)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { _ ->
            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.ConfigFragment)
        }
        initSensors()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.ConfigFragment)
                return true
            }
            R.id.action_weather -> {
                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.weatherFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        if (navController.currentDestination?.id == R.id.weatherFragment) {
            supportActionBar?.title = getString(R.string.sa_project)
        }
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
    @SuppressLint("SetTextI18n")
    private fun onChangeActivity(act :String ) {
        findViewById<TextView>(R.id.textview_first).setText("Current Activity: $act")
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initSensors() {
        activitySensors = ActivitySensors(::onChangeActivity)
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        weatherSensors = WeatherSensors(sensorManager)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkPermission()

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun hasPermissions() : Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        val activityRecognitionPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED &&
                activityRecognitionPermission == PackageManager.PERMISSION_GRANTED
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermission() {
        if (hasPermissions())
            requestLocationUpdates()
        else
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACTIVITY_RECOGNITION), Codes.PERMISSION_LOCATION_SENSORS) // Replace LOCATION_PERMISSION_REQUEST_CODE with a unique integer
    }

    private fun startPhysicalSensors() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        val accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if(stepCounterSensor != null && accelerometerSensor != null) {
            sensorManager.registerListener(activitySensors, stepCounterSensor, SensorManager.SENSOR_DELAY_NORMAL)
            sensorManager.registerListener(activitySensors, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        else
            Log.d("DebugApp","Sensors not available")
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        startPhysicalSensors()
        fusedLocationClient.requestLocationUpdates(
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,Delays.DELAY_LOCATION_SENSOR_ACTIVITY).build(),
            activitySensors,
            Looper.getMainLooper()
        )

        fusedLocationClient.requestLocationUpdates(
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,Delays.DELAY_LOCATION_SENSOR_WEATHER).build(),
            weatherSensors,
            Looper.getMainLooper()
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Codes.PERMISSION_LOCATION_SENSORS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            } else {
                Toast.makeText(this, "Location permissions are required to access user location.", Toast.LENGTH_LONG).show()
            }
        }
    }
}