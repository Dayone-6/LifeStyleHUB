package ru.dayone.lifestylehub.data.remote.weather.model

import com.google.gson.annotations.SerializedName

data class WeatherType(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val iconId: String
)