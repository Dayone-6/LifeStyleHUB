package ru.dayone.lifestylehub.utils

import ru.dayone.lifestylehub.model.FormattedPlaceModel

open class PlacesStatus {
    data class Succeed(val placesResponseModel: List<FormattedPlaceModel>): PlacesStatus()
    data class Failed(val code: FailureCode): PlacesStatus()
}