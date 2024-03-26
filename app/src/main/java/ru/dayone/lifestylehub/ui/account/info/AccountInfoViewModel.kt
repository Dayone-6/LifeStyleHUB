package ru.dayone.lifestylehub.ui.account.info

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dayone.lifestylehub.account.local_data.User
import ru.dayone.lifestylehub.utils.FailureCode
import ru.dayone.lifestylehub.utils.status.Status
import ru.dayone.lifestylehub.utils.SuccessCode

class AccountInfoViewModel(
    context: Context
) : ViewModel() {
    private val repository: AccountInfoRepository = AccountInfoRepository(context)

    private val _status: MutableLiveData<Status> = MutableLiveData(Status.Default())
    val status: LiveData<Status> = _status

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    fun getUserByLogin(login: String){
        _status.value = Status.InProgress()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val user = repository.getUserByLogin(login)
                if (user != null) {
                    // если убрать !! начинает красным подсвечивать
                    _user.postValue(user!!)
                    _status.postValue(Status.Success(SuccessCode.DEFAULT))
                }else{
                    _status.postValue(Status.Failure(FailureCode.DEFAULT))
                }
            }catch (e: Exception){
                e.printStackTrace()
                _status.postValue(Status.Failure(FailureCode.DEFAULT))
            }
        }
    }
}