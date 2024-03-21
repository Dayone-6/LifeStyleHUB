package ru.dayone.lifestylehub.api.weather.model

import com.google.gson.annotations.SerializedName

data class CoordPair(
    @SerializedName("lon") val longitude: Double,
    @SerializedName("lat") val latitude: Double
)