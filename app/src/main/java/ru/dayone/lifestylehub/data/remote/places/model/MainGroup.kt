package ru.dayone.lifestylehub.data.remote.places.model

import com.google.gson.annotations.SerializedName

data class MainGroup(
    @SerializedName("results") val results: List<Result>,
    @SerializedName("totalResults") val totalCount: Int
)