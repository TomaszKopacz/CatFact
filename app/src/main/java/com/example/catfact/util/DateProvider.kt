package com.example.catfact.util

import java.text.SimpleDateFormat
import java.util.*

object DateProvider {
    fun now(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return formatter.format(Date())
    }
}