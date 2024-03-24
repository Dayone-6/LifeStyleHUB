package ru.dayone.lifestylehub.data.remote.places.model

import com.google.gson.annotations.SerializedName

data class PhotoData(
    @SerializedName("id") val id: String?,
    @SerializedName("prefix") val urlPrefix: String?,
    @SerializedName("suffix") val urlSuffix: String?,
    @SerializedName("width") val width: Int?,
    @SerializedName("height") val height: Int?,
    @SerializedName("visibility") val visibility: String?
)
