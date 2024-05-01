package com.example.application

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.application.databinding.ActivityMainBinding
import com.example.application.data.ManagerDB
import com.example.application.model.config.ConfigTableFuns
import com.example.application.model.csstats.Cache
import com.example.application.network.csstats.StatsAPI
import com.example.application.utils.physicalactivity.ActivitySensorsHelper
import com.example.application.utils.weather.WeatherSensorsHelper

class MainActivity : AppCompatActivity() {
    private val PERMISSION_LOCATION_SENSORS : Int = 1000

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


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

        initiateCache()
        checkPermission()
        initiateService()
        binding.bottomNavigationView.selectedItemId=R.id.home
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.reload->initiateCache()
                R.id.sensorData->findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.dataCollectionFragment)
                R.id.home->findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.FirstFragment)
                R.id.graphs->
                            if(Cache.getInstance().hasInfo())
                                findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.SecondFragment)
                            else
                                Toast.makeText(this, "No performance Data", Toast.LENGTH_SHORT).show()
                R.id.settings->findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.ConfigFragment)
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    private fun initiateService() {
        if(permissionsGranted) {
            Log.d("DebugApp","Service started")
            val serviceIntent = Intent(this, BackgroundService::class.java)
            startService(serviceIntent)
        }
    }

    private fun checkPermission() {
        if (ActivitySensorsHelper.checkPermissions(this) && WeatherSensorsHelper.checkPermissions(this))
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
                initiateService()
            } else {
                Toast.makeText(this, "Location permissions are required to access user location.", Toast.LENGTH_LONG).show()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initiateCache() {
        val config = ConfigTableFuns.getLastVersion()
        if(config != null) {
            if(Cache.getInstance().needsUpdate()) {
                Toast.makeText(this,"Data not in cache. Contacting API.", Toast.LENGTH_SHORT).show()
                StatsAPI.getData(config.csstatsID, StatsAPI::default_suc, StatsAPI::default_err1, StatsAPI::default_err2)
            }
            else
                Toast.makeText(this,"Cache does not need to be updated",Toast.LENGTH_SHORT).show()

        }
        else
            Toast.makeText(this,"No configuration saved",Toast.LENGTH_SHORT).show()
    }
}