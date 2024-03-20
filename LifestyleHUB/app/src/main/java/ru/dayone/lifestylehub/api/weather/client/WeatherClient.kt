package ru.dayone.lifestylehub.api.weather.client

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dayone.lifestylehub.utils.Constants

object WeatherClient {
    private var client: Retrofit? = null

    fun getClient(): Retrofit{
        return client ?: run {
            client = Retrofit.Builder()
                .baseUrl(Constants.WEATHER_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            client!!
        }
    }
}