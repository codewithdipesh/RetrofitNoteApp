package com.example.notesappretrofit.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale



fun getDatefromString(dateString: String) : String{
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val date = dateFormat.parse(dateString) // Parsing the date string
    calendar.time = date

    val outputFormat = SimpleDateFormat("EEE, dd MMM yy", Locale.ENGLISH)
    return outputFormat.format(calendar.time)
}


fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("EEE, dd MMM yy", Locale.getDefault())
    return dateFormat.format(Date())
}