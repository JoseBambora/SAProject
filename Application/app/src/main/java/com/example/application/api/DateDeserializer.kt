package com.example.application.api

import android.icu.text.SimpleDateFormat
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.util.Date
import java.util.Locale

class DateDeserializer : JsonDeserializer<Date> {
    private val dateFormat = SimpleDateFormat("EEE dd MMM yy", Locale.ENGLISH)

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date {
        val dateString = json?.asString
        val string = dateString?.replace(Regex("(st|nd|rd|th)"),"")
        return dateFormat.parse(string)
    }
}