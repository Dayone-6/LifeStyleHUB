package ru.dayone.lifestylehub.account.registiration

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dayone.lifestylehub.account.model.User
import ru.dayone.lifestylehub.account.utils.AccountStatus
import ru.dayone.lifestylehub.data.local.AppPrefs

class RegistrationViewModel(
    context: Context
) : ViewModel() {
    private val registrationRepository: RegistrationRepository = RegistrationRepository(context)

    private val _accountStatus: MutableLiveData<AccountStatus> = MutableLiveData()
    val accountStatus: LiveData<AccountStatus> = _accountStatus

    fun signUpNewUser(user: User) {
        registrationRepository.signUpUser(user).addOnSuccessListener {
            AppPrefs.setIsAuthorized(true)
            _accountStatus.postValue(AccountStatus.Succeed())
        }.addOnFailureListener {
            _accountStatus.postValue(AccountStatus.Failed(it.message!!))
        }.addOnCanceledListener {
            _accountStatus.postValue(AccountStatus.Failed("Canceled"))
        }
    }
}