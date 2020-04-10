package com.example.catfact.util.date

import androidx.room.TypeConverter
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class DateConverters {

    @TypeConverter
    fun toTimestamp(value: Long?): Timestamp {
        return Timestamp(value ?: 0)
    }

    @TypeConverter
    fun fromTimestamp(value: Timestamp?): Long {
        return value?.time ?: 0
    }

    fun niceFormat(timestamp: Timestamp): String {
        val formatter = SimpleDateFormat(NICE_DATE_FORMAT, Locale.getDefault())

        return formatter.format(timestamp)
    }

    companion object {
        private const val NICE_DATE_FORMAT = "yyyy-MM-dd hh:mm"
    }
}