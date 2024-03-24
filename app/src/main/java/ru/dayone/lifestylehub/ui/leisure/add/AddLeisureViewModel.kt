package ru.dayone.lifestylehub.ui.leisure.add

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dayone.lifestylehub.data.local.leisure.LeisureEntity
import ru.dayone.lifestylehub.ui.leisure.LeisureRepository
import ru.dayone.lifestylehub.utils.FailureCode
import ru.dayone.lifestylehub.utils.SuccessCode
import ru.dayone.lifestylehub.utils.status.Status

class AddLeisureViewModel(
    context: Context
) : ViewModel() {
    private val repository = LeisureRepository(context)

    private val _status: MutableLiveData<Status> = MutableLiveData()
    val status: LiveData<Status> = _status

    fun addLeisure(leisure: LeisureEntity){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.addLeisure(leisure)
                _status.postValue(Status.Success(SuccessCode.DEFAULT))
            }catch (e: Exception){
                _status.postValue(Status.Failure(FailureCode.DEFAULT))
            }
        }
    }
}