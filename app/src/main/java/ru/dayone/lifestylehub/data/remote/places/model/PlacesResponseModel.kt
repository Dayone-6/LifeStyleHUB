package ru.dayone.lifestylehub.data.remote.places.model

import com.google.gson.annotations.SerializedName

data class PlacesResponseModel (
    @SerializedName("meta") val metaData: MetaData,
    @SerializedName("response") val response: MainData
)