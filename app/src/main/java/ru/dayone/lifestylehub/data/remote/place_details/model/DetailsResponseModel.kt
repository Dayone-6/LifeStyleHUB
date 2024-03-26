package ru.dayone.lifestylehub.data.remote.place_details.model

import com.google.gson.annotations.SerializedName
import ru.dayone.lifestylehub.data.remote.places.model.MetaData

data class DetailsResponseModel(
    @SerializedName("meta") val metadata: MetaData,
    @SerializedName("response") val response: DetailsResponse,
)
