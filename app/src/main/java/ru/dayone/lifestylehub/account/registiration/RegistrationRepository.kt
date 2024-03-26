package ru.dayone.lifestylehub.account.registiration

import android.content.Context
import android.util.Log
import org.json.JSONArray
import ru.dayone.lifestylehub.account.local_data.UsersDatabase
import ru.dayone.lifestylehub.account.local_data.UsersDao
import ru.dayone.lifestylehub.account.local_data.User
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random


class RegistrationRepository(
    context: Context
) {
    private val db: UsersDatabase = UsersDatabase.getUsersDatabase(context)
    private val usersDao: UsersDao = db.usersDao()

    private val apiUrl = "https://randomapi.com/api/6de6abfedb24f889e0b5f675edc50deb?fmt=raw&sole"

    companion object{
        private var users: ArrayList<User>? = null
    }

    fun getAllUsers(): List<User>{
        return users ?: run {
            users = usersDao.getAllUsers() as ArrayList<User>
            users!!
        }
    }

    fun addUser(user: User){
        usersDao.addUser(user)
        users = usersDao.getAllUsers() as ArrayList<User>
    }

    private fun getUsersJson(): String{
        val connection: HttpURLConnection?
        val reader: BufferedReader?

        connection = URL(apiUrl).openConnection() as HttpURLConnection
        connection.connect()

        val stream = connection.inputStream
        reader = BufferedReader(InputStreamReader(stream))
        val buffer = StringBuffer()

        val line: String = reader.readLine()
        buffer.append(line + "\n")

        try {
            connection.disconnect()
            reader.close()
        }catch (e: Exception){
            e.printStackTrace()
        }

        Log.d("Data", "test")

        return buffer.toString()
    }

    fun getRandomUser(): User {
        val json = getUsersJson()

        val userJson = JSONArray(json).getJSONObject(0)
        val userName = userJson.getString("first")
        val userSurname = userJson.getString("last")
        val userEmail = userJson.getString("email")
        val userLogin = userName + userSurname + (Random.nextFloat() * 100).toInt().toString()
        Log.d("Data", userLogin)

        return User(
            userLogin,
            0,
            userName,
            userSurname,
            userEmail
        )
    }
}