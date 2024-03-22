package ru.dayone.lifestylehub.api.places.model

import com.google.gson.annotations.SerializedName

data class PlacesResponseModel (
    @SerializedName("meta") val metaData: MetaData,
    @SerializedName("response") val response: MainData
)