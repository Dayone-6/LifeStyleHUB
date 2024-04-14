package ru.dayone.lifestylehub.account.login

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.dayone.lifestylehub.account.model.User
import ru.dayone.lifestylehub.data.local.AppPrefs

class LoginRepository(
    context: Context
) {
    fun signInUser(email: String, password: String): Task<AuthResult>{
        return AppPrefs.getAuthInstance().signInWithEmailAndPassword(email, password)
    }
}