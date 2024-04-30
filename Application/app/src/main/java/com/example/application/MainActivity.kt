package com.example.application

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.application.databinding.ActivityMainBinding
import com.example.application.data.ManagerDB
import com.example.application.model.config.ConfigTableFuns
import com.example.application.model.csstats.Cache
import com.example.application.network.csstats.StatsAPI
import com.example.application.utils.ActivitySensors.ActivitySensorsHelper
import com.example.application.utils.SaveData
import com.example.application.utils.WeatherSensorsHelper
import com.example.application.utils.sleep.SleepSensorsHelper

class MainActivity : AppCompatActivity() {
    private val PERMISSION_LOCATION_SENSORS : Int = 1000

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var activitySensorsHelper: ActivitySensorsHelper
    private lateinit var weatherSensorsHelper : WeatherSensorsHelper
    private lateinit var sleepSensorsHelper: SleepSensorsHelper

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

        //binding.fab.setOnClickListener { _ ->
            //findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.ConfigFragment)
        //}
        //binding.reloadCache.setOnClickListener{_ -> initiateCache()}
        activitySensorsHelper = ActivitySensorsHelper(this)
        weatherSensorsHelper = WeatherSensorsHelper(this)

        // Initialize SleepSensorHelper
        sleepSensorsHelper = SleepSensorsHelper(this)

        // Check and request permission for light sensor
        scheduleMidnightAlarm()
        initiateCache()
        checkPermission()
        binding.bottomNavigationView.selectedItemId=R.id.home
        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.reload->Toast.makeText(this, "TODO", Toast.LENGTH_SHORT).show()
                R.id.sensorData->findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.weatherFragment)
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

    override fun onResume() {
        super.onResume()
        if(permissionsGranted) {
            activitySensorsHelper.onStart()
            weatherSensorsHelper.onStart()
            sleepSensorsHelper.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if(permissionsGranted) {
            activitySensorsHelper.onStop()
            weatherSensorsHelper.onStop()
            sleepSensorsHelper.stop()
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
    private fun scheduleMidnightAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, SaveData::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val intervalMillis = AlarmManager.INTERVAL_DAY

        val triggerAtMillis = System.currentTimeMillis()

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            intervalMillis,// 60 * 1000,
            pendingIntent
        )
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