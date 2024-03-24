package ru.dayone.lifestylehub.data.local.leisure

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LeisureEntity::class], version = 1)
abstract class LeisureDatabase : RoomDatabase() {
    abstract fun getLeisureDao() : LeisureDao

    companion object{
        private var instance: LeisureDatabase? = null
        fun getInstance(context: Context): LeisureDatabase{
            return instance ?: synchronized(this) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    LeisureDatabase::class.java,
                    "leisure_db"
                ).build()
                instance!!
            }
        }
    }
}