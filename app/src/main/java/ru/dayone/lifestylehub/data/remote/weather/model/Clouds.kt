package ru.dayone.lifestylehub.data.remote.weather.model

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") val percentage: Int
)