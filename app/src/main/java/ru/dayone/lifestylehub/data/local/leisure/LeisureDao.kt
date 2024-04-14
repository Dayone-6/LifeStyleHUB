package ru.dayone.lifestylehub.data.local.leisure

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface LeisureDao {
    @Query("SELECT * FROM leisure WHERE user_id = :userId")
    fun getAllLeisure(userId: String): List<LeisureEntity>

    @Update(entity = LeisureEntity::class)
    fun updateLeisure(leisure: LeisureEntity)

    @Insert
    fun addLeisure(leisure: LeisureEntity)

    @Query("SELECT * FROM leisure WHERE place_id = :placeId AND user_id = :userId")
    fun getLeisureByPLaceId(userId: String, placeId: String): List<LeisureEntity>

    @Query("DELETE FROM leisure WHERE id = :id")
    fun deleteLeisure(id: Int)
}