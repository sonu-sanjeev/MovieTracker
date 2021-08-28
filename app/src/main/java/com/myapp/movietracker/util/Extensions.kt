package com.myapp.movietracker.util

import java.text.SimpleDateFormat
import java.util.*

fun String.getDate(): String {
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormat = SimpleDateFormat("dd-MM-yyyy")
        val date: Date = inputFormat.parse(this)
        return outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return this
}