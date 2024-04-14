package ru.dayone.lifestylehub.data.local

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.faltenreich.skeletonlayout.SkeletonConfig
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.utils.AUTH_USER_KEY
import ru.dayone.lifestylehub.utils.IS_AUTHORIZED_KEY


object AppPrefs {
    private lateinit var prefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var isAuthorized: Boolean = false

    private var authorizedUserLogin: String = ""

    private var isLocationPermissionGranted: Boolean = false

    private var skeletonConfig: SkeletonConfig? = null

    private var location: Location? = null

    private var authInstance: FirebaseAuth? = null

    fun initPrefs(context: Context, name: String){
        prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)
        editor = prefs.edit()

        authorizedUserLogin = prefs.getString(AUTH_USER_KEY, "")!!

        authInstance = Firebase.auth

        isAuthorized = authInstance!!.currentUser != null

        val defaultSkeletonConfig = SkeletonConfig.default(context)
        skeletonConfig = SkeletonConfig(
            context.getColor(R.color.skeleton_color),
            defaultSkeletonConfig.maskCornerRadius,
            defaultSkeletonConfig.showShimmer,
            context.getColor(R.color.skeleton_shimmer),
            1200L,
            defaultSkeletonConfig.shimmerDirection,
            defaultSkeletonConfig.shimmerAngle
        )
    }

    fun setIsAuthorized(isAuthorized: Boolean){
        AppPrefs.isAuthorized = isAuthorized
    }

    fun getIsAuthorized() = isAuthorized

    fun getAuthorizedUserLogin() = authorizedUserLogin

    fun setAuthorizedUserLogin(login: String){
        authorizedUserLogin = login

        editor.putString(AUTH_USER_KEY, login)
        editor.commit()
    }

    fun setIsLocationPermissionGranted(isGranted: Boolean){
        isLocationPermissionGranted = isGranted
    }

    fun getIsLocationPermissionGranted() = isLocationPermissionGranted

    fun getSkeletonConfig() = skeletonConfig!!

    fun setLocation(location: Location){
        AppPrefs.location = location
    }

    fun getLocation() = location


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork: Network? = connectivityManager.activeNetwork
        val capabilities: NetworkCapabilities? =
            connectivityManager.getNetworkCapabilities(activeNetwork)

        if (capabilities != null) {
            return if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) true else if (capabilities.hasTransport(
                    NetworkCapabilities.TRANSPORT_CELLULAR
                )
            ) true else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) true else false
        }
        return false
    }

    fun getAuthInstance() = authInstance!!

}