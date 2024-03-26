package ru.dayone.lifestylehub.data.remote.place_details.model

import com.google.gson.annotations.SerializedName

data class DetailsResponse(
    @SerializedName("venue") val venue: PlaceDetails
)