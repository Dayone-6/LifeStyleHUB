package ru.dayone.lifestylehub.api.places.model

import com.google.gson.annotations.SerializedName

data class MainData(
    @SerializedName("group") val group: MainGroup,
    @SerializedName("context") val context: MainContext
)
