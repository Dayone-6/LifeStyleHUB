package ru.dayone.lifestylehub.api.weather.model

import android.health.connect.datatypes.units.Percentage
import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") val percentage: Int
)