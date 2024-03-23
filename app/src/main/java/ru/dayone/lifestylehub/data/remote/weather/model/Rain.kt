package ru.dayone.lifestylehub.data.remote.weather.model

import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("1h") val height: Float
)