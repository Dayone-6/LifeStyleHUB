package ru.dayone.lifestylehub.account.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dayone.lifestylehub.account.utils.AccountStatus
import ru.dayone.lifestylehub.data.local.AppPrefs

class LoginViewModel(
    context: Context
) : ViewModel() {
    private val _accountStatus: MutableLiveData<AccountStatus> = MutableLiveData()
    val accountStatus: LiveData<AccountStatus> = _accountStatus

    private val repository = LoginRepository(context)

    fun loginUser(email: String, password: String) {
        repository.signInUser(email, password).addOnSuccessListener {
            AppPrefs.setIsAuthorized(true)
            _accountStatus.postValue(AccountStatus.Succeed())
        }.addOnFailureListener {
            _accountStatus.postValue(AccountStatus.Failed(it.message!!))
        }.addOnCanceledListener {
            _accountStatus.postValue(AccountStatus.Failed("Canceled"))
        }
    }
}