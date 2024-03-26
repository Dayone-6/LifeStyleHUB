package ru.dayone.lifestylehub.account.login

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

class LoginViewModel(
    context: Context
) : ViewModel() {
    private val _accountStatus: MutableLiveData<AccountStatus> = MutableLiveData(
        AccountStatus.Default()
    )
    val accountStatus: LiveData<AccountStatus> = _accountStatus

    private val repository = LoginRepository(context)

    private val _loginUser: MutableLiveData<User> = MutableLiveData()
    val loginUser: LiveData<User> = _loginUser

    fun loginUser(userLogin: String, userPassword: String){
        _accountStatus.value = AccountStatus.InProcess()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user: User? = repository.getUserByLogin(userLogin)
                if(user != null){
                    val passwordHash = userPassword.hashCode()
                    if(user.password == passwordHash){
                        // этот варнинг не убирается, если его убрать, начинает как ошибку показывать
                        _loginUser.postValue(user!!)
                        _accountStatus.postValue(AccountStatus.Succeed(AccountSuccessCode.LOGIN_SUCCEED))
                    }else{
                        _accountStatus.postValue(AccountStatus.Failed(AccountFailureCode.INVALID_PASSWORD))
                    }
                }else{
                    _accountStatus.postValue(AccountStatus.Failed(AccountFailureCode.LOGIN_NOT_EXISTS))
                }
            }catch (e: Exception){
                _accountStatus.postValue(AccountStatus.Failed(AccountFailureCode.DEFAULT))
            }
        }
    }
}