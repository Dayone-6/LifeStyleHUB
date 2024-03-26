package ru.dayone.lifestylehub.data.local.activity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey val key: String,
    @ColumnInfo("activity") val activity: String,
    @ColumnInfo("accessibility") val accessibility: Double,
    @ColumnInfo("type") val type: String,
    @ColumnInfo("participants") val participants: Int,
    @ColumnInfo("price") val price: Double,
    @ColumnInfo("is_favourite") var isFavourite: Boolean
)