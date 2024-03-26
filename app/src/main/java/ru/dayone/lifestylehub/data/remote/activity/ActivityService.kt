package ru.dayone.lifestylehub.data.remote.activity

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ActivityService {
    @GET("activity?")
    fun getActivity(
        @Query("key") key: String?,
        @Query("type") type: String?,
        @Query("participants") participants: Int?,
        @Query("maxprice") maxPrice: Double?,
        @Query("minprice") minPrice: Double?,
        @Query("price") price: Double?,
        @Query("accessibility") accessibility: Double?,
        @Query("maxaccessibility") maxAccessibility: Double?,
        @Query("minaccessibility") minAccessibility: Double?
    ): Call<ActivityModel>
}