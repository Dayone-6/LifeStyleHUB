package ru.dayone.lifestylehub.ui.home.main

import android.util.Log
import retrofit2.Call
import retrofit2.Retrofit
import ru.dayone.lifestylehub.data.remote.weather.model.WeatherModel
import ru.dayone.lifestylehub.data.remote.weather.service.WeatherService

class WeatherRepository {
    private val client: Retrofit = ru.dayone.lifestylehub.data.remote.client.RetrofitClient.getWeatherClient()
    private val service: WeatherService = client.create(WeatherService::class.java)
    
    fun getWeatherCall(apiKey: String, lon: Double, lat: Double, lang: String): Call<WeatherModel>{
        val call = service.getWeather(apiKey, lat, lon, "metric", lang)
        Log.d("Data", call.request().url().toString())
        return call
    }
}