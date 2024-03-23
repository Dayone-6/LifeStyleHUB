package ru.dayone.lifestylehub.data.remote.weather.model

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed") val speed: Float,
    @SerializedName("deg") val degrees: Int,
    @SerializedName("gust") val gust: Float
)