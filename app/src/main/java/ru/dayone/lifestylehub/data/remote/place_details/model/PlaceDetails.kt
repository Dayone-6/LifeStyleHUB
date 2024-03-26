package ru.dayone.lifestylehub.data.remote.place_details.model

import com.google.gson.annotations.SerializedName
import ru.dayone.lifestylehub.data.remote.places.model.PhotoData
import ru.dayone.lifestylehub.data.remote.places.model.PlaceCategory
import ru.dayone.lifestylehub.data.remote.places.model.PlaceLocation

data class PlaceDetails(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("contact") val phone: DetailsContact?,
    @SerializedName("location") val location: PlaceLocation?,
    @SerializedName("categories") val category: List<PlaceCategory>?,
    @SerializedName("url") val url: String?,
    @SerializedName("likes") val likes: LikesData?,
    @SerializedName("hours") val hours: PlaceWorkStatus?,
    @SerializedName("bestPhoto") val mainPhoto: PhotoData?
)