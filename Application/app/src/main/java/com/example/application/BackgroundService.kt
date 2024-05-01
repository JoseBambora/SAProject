package com.example.application

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.application.utils.physicalactivity.ActivitySensorsHelper
import com.example.application.utils.SaveData
import com.example.application.utils.weather.WeatherSensorsHelper
import com.example.application.utils.sleep.SleepSensorsHelper


class BackgroundService : Service() {

    private lateinit var activitySensorsHelper: ActivitySensorsHelper
    private lateinit var weatherSensorsHelper : WeatherSensorsHelper
    private lateinit var sleepSensorsHelper: SleepSensorsHelper

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun scheduleMidnightAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, SaveData::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val intervalMillis = AlarmManager.INTERVAL_DAY // 24 hours

        val triggerAtMillis = System.currentTimeMillis()

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            intervalMillis, //60 * 1000,
            pendingIntent
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("DebugApp","A começar serviço")
        activitySensorsHelper.onStart()
        weatherSensorsHelper.onStart()
        sleepSensorsHelper.onStart()
        scheduleMidnightAlarm()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("DebugApp","A criar serviço")
        activitySensorsHelper = ActivitySensorsHelper(this)
        weatherSensorsHelper = WeatherSensorsHelper(this)
        sleepSensorsHelper = SleepSensorsHelper(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("DebugApp","A terminar serviço")
        activitySensorsHelper.onStop()
        weatherSensorsHelper.onStop()
        sleepSensorsHelper.onStop()
    }
}