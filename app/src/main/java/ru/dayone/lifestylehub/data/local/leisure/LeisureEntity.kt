package ru.dayone.lifestylehub.data.local.leisure

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leisure")
data class LeisureEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "userLogin") val userLogin: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "place_id") val placeId: String?
)