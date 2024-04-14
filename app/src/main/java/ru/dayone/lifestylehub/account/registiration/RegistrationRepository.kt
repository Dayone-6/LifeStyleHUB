package ru.dayone.lifestylehub.account.registiration

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.json.JSONArray
import ru.dayone.lifestylehub.account.model.User
import ru.dayone.lifestylehub.data.local.AppPrefs
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random


class RegistrationRepository(
    context: Context
) {
    fun signUpUser(user: User): Task<AuthResult>{
        return AppPrefs.getAuthInstance().createUserWithEmailAndPassword(user.email, user.password)
    }
}