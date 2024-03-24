package ru.dayone.lifestylehub.data.local.details

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place_details")
data class PlaceDetailsEntity (
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "photos_url_prefix") val photoPrefix: String?,
    @ColumnInfo(name = "photos_url_suffixes") var suffixes: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "categories") val categories: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "likes_count") val likes: Int?,
    @ColumnInfo(name = "status") val status: String?,
    @ColumnInfo(name = "isOpen") val isOpen: Boolean?,
    @ColumnInfo(name = "phone_number") val phone: String?,
    @ColumnInfo(name = "get_time") val gotTime: Long
)