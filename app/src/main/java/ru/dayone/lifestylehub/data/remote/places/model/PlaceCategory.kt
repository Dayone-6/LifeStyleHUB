package ru.dayone.lifestylehub.data.remote.places.model

import com.google.gson.annotations.SerializedName

data class PlaceCategory(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("pluralName") val pluralName: String,
    @SerializedName("shortName") val shortName: String,
    @SerializedName("categoryCode") val code: Int
)