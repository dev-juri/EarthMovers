package com.earthmovers.app.utils

import org.joda.time.DateTime
import org.joda.time.DateTimeZone

fun getFormattedTime(dateTime: String): String {
    val localDateTime = DateTime.parse(dateTime).withZone(DateTimeZone.getDefault())
    return localDateTime.toString("hh:mm a")
}

fun getFormattedDate(dateTime: String): String {
    val localDateTime = DateTime.parse(dateTime).withZone(DateTimeZone.getDefault())
    return localDateTime.toString("dd MMM yyyy")
}