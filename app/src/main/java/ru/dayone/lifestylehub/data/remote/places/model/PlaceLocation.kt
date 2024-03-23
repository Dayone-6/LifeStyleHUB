package ru.dayone.lifestylehub.data.remote.places.model

import com.google.gson.annotations.SerializedName

data class PlaceLocation(
    @SerializedName("address") val address: String,
    @SerializedName("formattedAddress") val fullAddress: List<String>
)