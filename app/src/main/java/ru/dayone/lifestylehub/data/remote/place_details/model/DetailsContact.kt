package ru.dayone.lifestylehub.data.remote.place_details.model

import com.google.gson.annotations.SerializedName

data class DetailsContact(
    @SerializedName("phone") val phone: String,
    @SerializedName("formattedPhone") val formattedPhone: String
)
