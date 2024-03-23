package ru.dayone.lifestylehub.data.remote.weather.model

import com.google.gson.annotations.SerializedName

data class CoordPair(
    @SerializedName("lon") val longitude: Double,
    @SerializedName("lat") val latitude: Double
)