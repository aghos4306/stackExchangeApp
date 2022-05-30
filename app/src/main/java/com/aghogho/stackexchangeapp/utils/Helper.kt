package com.aghogho.stackexchangeapp.utils

import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

fun TextView.getTime(time: Long) {
    val date = java.util.Date(time * 1000L)
    val timeFormat = java.text.SimpleDateFormat("dd-MM-yyy").format(date)
    this.text = timeFormat
}

