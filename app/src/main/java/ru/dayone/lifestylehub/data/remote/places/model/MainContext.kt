package ru.dayone.lifestylehub.data.remote.places.model

import com.google.gson.annotations.SerializedName

data class MainContext(
    @SerializedName("searchLocationNearYou") val isNear: Boolean,
    @SerializedName("searchLocationMapBounds") val isMapBounds: Boolean,
    @SerializedName("searchLocationType") val type: String,
    @SerializedName("boundsSummaryRadius") val radius: Int,
    @SerializedName("currency") val currency: String
)
