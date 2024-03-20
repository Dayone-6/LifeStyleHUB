package ru.dayone.lifestylehub.api.weather.model

import com.google.gson.annotations.SerializedName
import java.util.TimeZone

data class WeatherModel (
    @SerializedName("coord") val coords: CoordPair,
    @SerializedName("weather") val type: List<WeatherType>,
    @SerializedName("base") val from: String,
    @SerializedName("main") val mainData: WeatherMainData,
    @SerializedName("visibility") val viewRange: Int,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("rain") val rain: Rain,
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("dt") val dayTime: Int,
    @SerializedName("sys") val placeData: PlaceData,
    @SerializedName("timezone") val timeZone: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("cod") val code: Int
)