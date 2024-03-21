package ru.dayone.lifestylehub.prefs

import android.content.Context
import android.content.SharedPreferences
import ru.dayone.lifestylehub.utils.AUTH_USER_KEY
import ru.dayone.lifestylehub.utils.IS_AUTHORIZED_KEY

object AppPrefs {
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var isAuthorized: Boolean = false

    private var authorizedUserLogin: String = ""

    private var isLocationPermissionGranted: Boolean = false

    fun initPrefs(context: Context, name: String){
        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        editor = prefs.edit()

        isAuthorized = prefs.getBoolean(IS_AUTHORIZED_KEY, false)
        authorizedUserLogin = prefs.getString(AUTH_USER_KEY, "")!!
    }

    fun setIsAuthorized(isAuthorized: Boolean){
        this.isAuthorized = isAuthorized

        editor.putBoolean(IS_AUTHORIZED_KEY, isAuthorized)
        editor.apply()
    }

    fun getIsAuthorized() = isAuthorized

    fun getAuthorizedUserLogin() = authorizedUserLogin

    fun setAuthorizedUserLogin(login: String){
        this.authorizedUserLogin = login

        editor.putString(AUTH_USER_KEY, login)
        editor.commit()
    }

    fun setIsLocationPermissionGranted(isGranted: Boolean){
        isLocationPermissionGranted = isGranted
    }

    fun getIsLocationPermissionGranted() = isLocationPermissionGranted

}