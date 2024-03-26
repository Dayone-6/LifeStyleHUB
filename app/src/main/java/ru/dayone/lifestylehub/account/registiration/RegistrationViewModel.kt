package ru.dayone.lifestylehub.account.registiration

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dayone.lifestylehub.account.local_data.User
import ru.dayone.lifestylehub.account.utils.AccountFailureCode
import ru.dayone.lifestylehub.account.utils.AccountStatus
import ru.dayone.lifestylehub.account.utils.AccountSuccessCode

class RegistrationViewModel(
    context: Context
) : ViewModel() {
    private val registrationRepository: RegistrationRepository = RegistrationRepository(context)

    private val _accountStatus: MutableLiveData<AccountStatus> = MutableLiveData(
        AccountStatus.Default()
    )
    val accountStatus: LiveData<AccountStatus> = _accountStatus

    private val _userData: MutableLiveData<User> = MutableLiveData()
    val userData: LiveData<User> = _userData

    fun addUser(user: User){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val users = registrationRepository.getAllUsers()
                if(users.indexOfFirst { it.login == user.login } == -1){
                    registrationRepository.addUser(user)
                    _accountStatus.postValue(AccountStatus.Succeed(AccountSuccessCode.ADD_USER_SUCCEED))
                }else{
                    _accountStatus.postValue(AccountStatus.Failed(AccountFailureCode.LOGIN_ALREADY_EXISTS))
                }
            }catch (e: Exception){
                _accountStatus.postValue(AccountStatus.Failed(AccountFailureCode.DEFAULT))
            }
        }
    }

    fun getRandomUser(){
        _accountStatus.value = AccountStatus.InProcess()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _userData.postValue(registrationRepository.getRandomUser())
                _accountStatus.postValue(AccountStatus.Succeed(AccountSuccessCode.GET_RANDOM_USER_SUCCEED))
            }catch (e: Exception){
                e.printStackTrace()
                _accountStatus.postValue(AccountStatus.Failed(AccountFailureCode.GET_RANDOM_USER_FAILED))
            }
        }
    }
}