package ru.dayone.lifestylehub.ui.venue_details

import android.content.Context
import retrofit2.Call
import retrofit2.Retrofit
import ru.dayone.lifestylehub.data.local.details.PlaceDetailsDatabase
import ru.dayone.lifestylehub.data.local.details.PlaceDetailsEntity
import ru.dayone.lifestylehub.data.remote.RetrofitClient
import ru.dayone.lifestylehub.data.remote.place_details.model.DetailsResponseModel
import ru.dayone.lifestylehub.data.remote.place_details.service.PlaceDetailsService
import ru.dayone.lifestylehub.data.remote.places.model.PhotoData

class PlaceDetailsRepository(
    context: Context
) {
    private val db: PlaceDetailsDatabase = PlaceDetailsDatabase.getDatabase(context)
    private val detailsDao = db.getPlaceDetailsDao()

    private val detailsClient: Retrofit = RetrofitClient.getPlaceDetailsClient()
    private val detailsService: PlaceDetailsService = detailsClient.create(PlaceDetailsService::class.java)

    fun getLocalDetails(id: String): PlaceDetailsEntity?{
        return detailsDao.getPlaceById(id)
    }

    fun addDetails(details: PlaceDetailsEntity){
        detailsDao.addPlaceDetails(details)
    }
    fun getRemoteDetails(id: String, date: String, token: String): Call<DetailsResponseModel>{
        return detailsService.getDetails(id, date, token)
    }

    fun getPhotos(id: String, token: String): Call<List<PhotoData>>{
        return detailsService.getPhotos(id, token)
    }

    fun deleteDetails(id: String){
        detailsDao.deletePlace(id)
    }
}