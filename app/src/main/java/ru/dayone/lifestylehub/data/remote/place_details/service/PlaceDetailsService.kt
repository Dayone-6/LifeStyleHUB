package ru.dayone.lifestylehub.data.remote.place_details.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.dayone.lifestylehub.data.remote.place_details.model.DetailsResponseModel
import ru.dayone.lifestylehub.data.remote.places.model.PhotoData

interface PlaceDetailsService {
    @GET("v2/venues/{id}/?")
    fun getDetails(
        @Path("id") id: String,
        @Query("v") date: String,
        @Query("oauth_token") token: String
    ): Call<DetailsResponseModel>

    @GET("v3/places/{id}/photos?")
    fun getPhotos(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): Call<List<PhotoData>>
}