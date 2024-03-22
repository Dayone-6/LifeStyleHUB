package ru.dayone.lifestylehub.api.places.model

import com.google.gson.annotations.SerializedName

data class MetaData (
    @SerializedName("code") val code: Int,
    @SerializedName("requestId") val requestId: String
)