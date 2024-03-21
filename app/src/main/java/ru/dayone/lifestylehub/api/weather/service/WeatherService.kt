package ru.dayone.lifestylehub.api.weather.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.dayone.lifestylehub.api.weather.model.WeatherModel

interface WeatherService {
    @GET("weather?")
    fun getWeather(
        @Query("appid") apiKey: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("lang") lang: String
    ): Call<WeatherModel>
}