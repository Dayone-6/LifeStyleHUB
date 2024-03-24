package ru.dayone.lifestylehub.data.local.details

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PlaceDetailsEntity::class], version = 1)
abstract class PlaceDetailsDatabase : RoomDatabase() {
    abstract fun getPlaceDetailsDao(): PlaceDetailsDao

    companion object{
        private var instance: PlaceDetailsDatabase? = null
        fun getDatabase(context: Context): PlaceDetailsDatabase{
            return instance ?: synchronized(this) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlaceDetailsDatabase::class.java,
                    "place_details_db"
                ).build()
                instance!!
            }
        }
    }
}