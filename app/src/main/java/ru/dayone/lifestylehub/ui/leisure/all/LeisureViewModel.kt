package ru.dayone.lifestylehub.ui.leisure.all

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dayone.lifestylehub.data.local.leisure.LeisureEntity
import ru.dayone.lifestylehub.ui.leisure.LeisureRepository
import ru.dayone.lifestylehub.utils.status.LeisureStatus

class LeisureViewModel(
    context: Context
) : ViewModel() {
    private val repository = LeisureRepository(context)

    private val _status: MutableLiveData<LeisureStatus> = MutableLiveData()
    val status: LiveData<LeisureStatus> = _status

    fun getAllLeisure(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("Data", repository.getAllLeisure().toString())
                _status.postValue(LeisureStatus.Succeed(repository.getAllLeisure()))
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