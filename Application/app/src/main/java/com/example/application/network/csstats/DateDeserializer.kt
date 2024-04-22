package com.example.application.network.csstats

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateDeserializer : JsonDeserializer<LocalDate> {
    private val dateFormat = SimpleDateFormat("EEE dd MMM yy", Locale.ENGLISH)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate? {
        val dateString = json?.asString
        val string = dateString?.replace(Regex("(st|nd|rd|th)"),"")
        return if (string != null && string.contains("ago")) {
            val numberAgo = string.split(" ")[0].toInt()
            val calendar = Calendar.getInstance()
            when {
                string.contains("day") -> calendar.add(Calendar.DAY_OF_YEAR, -numberAgo)
                string.contains("hour") -> calendar.add(Calendar.HOUR_OF_DAY, -numberAgo)
            }
            calendar.time.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        } else {
            dateFormat.parse(string).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        }
    }
}