package ru.dayone.lifestylehub.data.remote.place_details.model

import com.google.gson.annotations.SerializedName

data class PlaceWorkStatus(
    @SerializedName("status") val status: String,
    @SerializedName("isOpen") val isOpen: Boolean
)
