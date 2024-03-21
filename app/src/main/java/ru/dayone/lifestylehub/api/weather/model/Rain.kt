package ru.dayone.lifestylehub.api.weather.model

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h") val height: Float
)