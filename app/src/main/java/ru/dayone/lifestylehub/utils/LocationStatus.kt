package ru.dayone.lifestylehub.utils

import android.location.Location

open class LocationStatus {
    data class Success(val location: Location, val city: String): LocationStatus()
    data class Failure(val failCode: FailureCode): LocationStatus()
}