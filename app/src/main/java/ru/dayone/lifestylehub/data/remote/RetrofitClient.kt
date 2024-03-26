package ru.dayone.lifestylehub.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.dayone.lifestylehub.utils.ACTIVITY_BASE_URL
import ru.dayone.lifestylehub.utils.PLACES_API_BASE_URL
import ru.dayone.lifestylehub.utils.PLACES_DETAILS_BASE_URL
import ru.dayone.lifestylehub.utils.WEATHER_API_BASE_URL

object RetrofitClient {
    private var weatherClient: Retrofit? = null

    fun getWeatherClient(): Retrofit{
        return weatherClient ?: run {
            weatherClient = Retrofit.Builder()
                .baseUrl(WEATHER_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            weatherClient!!
        }
    }

    private var placesClient: Retrofit? = null

    fun getPlacesClient(): Retrofit{
        return placesClient ?: run {
            placesClient = Retrofit.Builder()
                .baseUrl(PLACES_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            placesClient!!
        }
    }

    private var placeDetailsClient: Retrofit? = null
    fun getPlaceDetailsClient(): Retrofit{
        return placeDetailsClient ?: run {
            placeDetailsClient = Retrofit.Builder()
                .baseUrl(PLACES_DETAILS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            placeDetailsClient!!
        }
    }

    private var activityClient: Retrofit? = null
    fun getActivityClient(): Retrofit{
        return activityClient ?: run {
            activityClient = Retrofit.Builder()
                .baseUrl(ACTIVITY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            activityClient!!
        }
    }
}