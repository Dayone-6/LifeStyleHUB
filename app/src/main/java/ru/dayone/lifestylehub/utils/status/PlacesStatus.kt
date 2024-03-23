package ru.dayone.lifestylehub.utils.status

import ru.dayone.lifestylehub.model.FormattedPlaceModel
import ru.dayone.lifestylehub.utils.FailureCode

open class PlacesStatus {
    data class Succeed(val placesResponseModel: List<FormattedPlaceModel>): PlacesStatus()
    data class Failed(val code: FailureCode): PlacesStatus()
}