package ru.dayone.lifestylehub.data.remote.activity

import com.google.gson.annotations.SerializedName

data class ActivityModel(
    @SerializedName("key") val key: String,
    @SerializedName("activity") val activity: String,
    @SerializedName("accessibility") val accessibility: Double,
    @SerializedName("type") val type: String,
    @SerializedName("participants") val participants: Int,
    @SerializedName("price") val price: Double
)