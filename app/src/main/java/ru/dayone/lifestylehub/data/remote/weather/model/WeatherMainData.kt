package ru.dayone.lifestylehub.data.remote.weather.model

import com.google.gson.annotations.SerializedName

data class WeatherMainData(
    @SerializedName("temp") val temperature: Float,
    @SerializedName("feels_like") val feelsLike: Float,
    @SerializedName("temp_min") val temperatureMin: Float,
    @SerializedName("temp_max") val temperatureMax: Float,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("sea_level") val seaLevel: Int,
    @SerializedName("grnd_level") val groundLevel: Int
)