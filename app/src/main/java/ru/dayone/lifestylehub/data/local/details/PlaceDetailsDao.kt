package ru.dayone.lifestylehub.data.local.details

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlaceDetailsDao {
    @Query("SELECT * FROM place_details WHERE id = :id")
    fun getPlaceById(id: String): PlaceDetailsEntity?

    @Delete
    fun deletePlace(place: PlaceDetailsEntity)

    @Insert
    fun addPlaceDetails(place: PlaceDetailsEntity)
}