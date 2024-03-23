package ru.dayone.lifestylehub.data.remote.places.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.dayone.lifestylehub.data.remote.places.model.PlacesResponseModel

interface PlacesService  {
    @GET("recommendations?")
    fun getPLaces(
        @Query("oauth_token") token: String,
        @Query("ll") coords: String,
        @Query("v") date: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<PlacesResponseModel>
}