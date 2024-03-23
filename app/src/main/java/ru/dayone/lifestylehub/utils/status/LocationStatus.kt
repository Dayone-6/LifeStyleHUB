package ru.dayone.lifestylehub.utils.status

import android.location.Location
import ru.dayone.lifestylehub.utils.FailureCode

open class LocationStatus {
    data class Success(val location: Location, val city: String): LocationStatus()
    data class Failure(val failCode: FailureCode): LocationStatus()
}