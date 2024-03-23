package ru.dayone.lifestylehub.data.remote.places.model

import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("displayType") val type: String,
    @SerializedName("venue") val place: Place,
    @SerializedName("id") val id: String,
    @SerializedName("photo") val photo: PhotoData,
)
