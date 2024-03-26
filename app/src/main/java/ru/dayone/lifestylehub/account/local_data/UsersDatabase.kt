package ru.dayone.lifestylehub.account.local_data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UsersDatabase : RoomDatabase(){
    abstract fun usersDao(): UsersDao

    companion object {
        @Volatile
        private var usersDatabaseInstance: UsersDatabase? = null
        fun getUsersDatabase(context: Context): UsersDatabase {
            return usersDatabaseInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UsersDatabase::class.java,
                    "users_db"
                ).build()
                usersDatabaseInstance = instance
                instance
            }
        }
    }
}