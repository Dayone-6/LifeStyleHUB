package ru.dayone.lifestylehub.ui.leisure.all

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dayone.lifestylehub.ui.leisure.LeisureRepository
import ru.dayone.lifestylehub.utils.status.LeisureStatus

class LeisureViewModel(
    context: Context
) : ViewModel() {
    private val repository = LeisureRepository(context)

    private val _status: MutableLiveData<LeisureStatus> = MutableLiveData()
    val status: LiveData<LeisureStatus> = _status

    fun getAllLeisure(userLogin: String){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _status.postValue(LeisureStatus.Succeed(repository.getAllLeisure(userLogin)))
            }catch (e: Exception){
                _status.postValue(LeisureStatus.Failed())
            }
        }
    }

    fun deleteLeisure(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.deleteLeisure(id)
            }catch (e: Exception){
                _status.postValue(LeisureStatus.Failed())
            }
        }
    }
}