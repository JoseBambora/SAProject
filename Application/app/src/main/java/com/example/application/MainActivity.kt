package com.example.application

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.application.databinding.ActivityMainBinding
import com.example.application.data.ManagerDB
import com.example.application.utils.ActivitySensors.ActivitySensorsHelper
import com.example.application.utils.weather.WeatherSensorsHelper
import com.example.application.utils.LightSensorsHelper

class MainActivity : AppCompatActivity() {
    private val PERMISSION_LOCATION_SENSORS : Int = 1000

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var activitySensorsHelper: ActivitySensorsHelper
    private lateinit var weatherSensorsHelper : WeatherSensorsHelper
    private lateinit var lightSensorsHelper: LightSensorsHelper

    private var permissionsGranted : Boolean = false

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
        activitySensorsHelper = ActivitySensorsHelper(this)
        weatherSensorsHelper = WeatherSensorsHelper(this)

        // Initialize LightSensorsHelper
        lightSensorsHelper = LightSensorsHelper(this)


        // Check and request permission for light sensor
        checkPermission()
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

    override fun onResume() {
        super.onResume()
        if(permissionsGranted) {
            activitySensorsHelper.onStart()
            weatherSensorsHelper.onStart()
            lightSensorsHelper.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if(permissionsGranted) {
            activitySensorsHelper.onStop()
            weatherSensorsHelper.onStop()
            lightSensorsHelper.stop()
        }
    }

    // Update the TextView with the current light intensity value


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        if (navController.currentDestination?.id == R.id.weatherFragment) {
            supportActionBar?.title = getString(R.string.sa_project)
        }
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun checkPermission() {
        if (activitySensorsHelper.checkPermissions(this) && weatherSensorsHelper.checkPermissions(this))
            permissionsGranted = true
        else
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACTIVITY_RECOGNITION),
                PERMISSION_LOCATION_SENSORS)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_LOCATION_SENSORS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Log.d("DebugApp","Permissions granted")
                permissionsGranted = true
            } else {
                Toast.makeText(this, "Location permissions are required to access user location.", Toast.LENGTH_LONG).show()
            }
        }
    }
}