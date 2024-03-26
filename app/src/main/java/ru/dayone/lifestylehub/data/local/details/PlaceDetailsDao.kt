package ru.dayone.lifestylehub.data.local.details

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PlaceDetailsDao {
    @Query("SELECT * FROM place_details WHERE id = :id")
    fun getPlaceById(id: String): PlaceDetailsEntity?

    @Query("DELETE FROM place_details WHERE id = :id")
    fun deletePlace(id: String)

    @Insert(entity = PlaceDetailsEntity::class)
    fun addPlaceDetails(place: PlaceDetailsEntity)
}