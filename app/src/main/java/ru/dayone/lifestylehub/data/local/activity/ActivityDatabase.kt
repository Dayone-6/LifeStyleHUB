package ru.dayone.lifestylehub.data.local.activity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ActivityEntity::class], version = 1)
abstract class ActivityDatabase : RoomDatabase(){
    abstract fun getActivityDao(): ActivityDao

    companion object{
        private var instance: ActivityDatabase? = null
        fun getInstance(context: Context): ActivityDatabase{
            return instance ?: synchronized(this){
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    ActivityDatabase::class.java,
                    "activity_db"
                ).build()
                instance!!
            }
        }
    }
}