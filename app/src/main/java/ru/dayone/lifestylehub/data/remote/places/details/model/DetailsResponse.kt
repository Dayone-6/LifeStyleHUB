package ru.dayone.lifestylehub.data.remote.places.details.model

import com.google.gson.annotations.SerializedName

data class DetailsResponse(
    @SerializedName("venue") val venue: PlaceDetails
)