package ru.dayone.lifestylehub.api.places.model

import com.google.gson.annotations.SerializedName

data class PlaceLocation(
    @SerializedName("address") val address: String,
    @SerializedName("distance") val distance: Int,
    @SerializedName("formattedAddress") val fullAddress: List<String>
)