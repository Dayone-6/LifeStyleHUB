package ru.dayone.lifestylehub.account.local_data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UsersDao {
    @Query(value = "SELECT * FROM users")
    fun getAllUsers(): List<User>

    @Insert(entity = User::class)
    fun addUser(newUser: User)

    @Query(value = "SELECT * FROM users WHERE login = :login")
    fun getUserByLogin(login: String): User?

    @Delete
    fun deleteUser(user: User)

    @Update(entity = User::class)
    fun updateUser(user: User)
}