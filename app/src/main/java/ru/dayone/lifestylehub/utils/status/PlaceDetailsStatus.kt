package ru.dayone.lifestylehub.utils.status

import ru.dayone.lifestylehub.data.local.details.PlaceDetailsEntity
import ru.dayone.lifestylehub.utils.FailureCode

open class PlaceDetailsStatus {
    data class Succeed(val details: PlaceDetailsEntity): PlaceDetailsStatus()
    data class Failed(val failureCode: FailureCode): PlaceDetailsStatus()
}