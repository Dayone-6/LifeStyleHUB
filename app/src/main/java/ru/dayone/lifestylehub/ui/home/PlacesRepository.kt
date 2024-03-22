package ru.dayone.lifestylehub.ui.home

import retrofit2.Call
import retrofit2.Retrofit
import ru.dayone.lifestylehub.api.client.RetrofitClient
import ru.dayone.lifestylehub.api.places.model.PlacesResponseModel
import ru.dayone.lifestylehub.api.places.service.PlacesService

class PlacesRepository {
    private val client: Retrofit = RetrofitClient.getPlacesClient()
    private val service: PlacesService = client.create(PlacesService::class.java)

    fun getPlacesCall(token: String, ll: String, date: String, limit: Int, offset: Int): Call<PlacesResponseModel>{
        return service.getPLaces(token, ll, date, limit, offset)
    }
}