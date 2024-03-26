package ru.dayone.lifestylehub.data.local.activity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ActivityDao {
    @Insert
    fun saveActivity(activity: ActivityEntity)

    @Query("SELECT * FROM activities")
    fun getAllActivities(): List<ActivityEntity>

    @Update
    fun updateActivity(activity: ActivityEntity)
}