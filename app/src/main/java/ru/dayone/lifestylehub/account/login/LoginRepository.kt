package ru.dayone.lifestylehub.account.login

import android.content.Context
import ru.dayone.lifestylehub.account.local_data.UsersDatabase
import ru.dayone.lifestylehub.account.local_data.UsersDao
import ru.dayone.lifestylehub.account.local_data.User

class LoginRepository(
    context: Context
) {
    private val db: UsersDatabase = UsersDatabase.getUsersDatabase(context)
    private val usersDao: UsersDao = db.usersDao()

    fun getUserByLogin(login: String): User?{
        return usersDao.getUserByLogin(login)
    }
}