package ru.dayone.lifestylehub.data.remote.places.model

import com.google.gson.annotations.SerializedName

data class Place(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: PlaceLocation,
    @SerializedName("categories") val categories: List<PlaceCategory>
)
