package ru.dayone.lifestylehub.ui.find_activity

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dayone.lifestylehub.data.local.activity.ActivityEntity
import ru.dayone.lifestylehub.data.remote.activity.ActivityModel
import ru.dayone.lifestylehub.utils.FailureCode
import ru.dayone.lifestylehub.utils.status.ActivityStatus

class FindActivityViewModel(context: Context) : ViewModel(){
    private val repository = FindActivityRepository(context)

    private val _remoteStatus: MutableLiveData<ActivityStatus.Remote> = MutableLiveData()
    val remoteStatus: LiveData<ActivityStatus.Remote> = _remoteStatus

    private val _localStatus: MutableLiveData<ActivityStatus.Local> = MutableLiveData()
    val localStatus: LiveData<ActivityStatus.Local> = _localStatus

    fun getRemoteActivity(
        key: String?,
        type: String?,
        participants: Int?,
        minPrice: Double?,
        maxPrice: Double?,
        price: Double?,
        accessibility: Double?,
        minAccessibility: Double?,
        maxAccessibility: Double?
    ){
        repository.getRemoteActivity(
            key,
            type,
            participants,
            minPrice,
            maxPrice,
            price,
            accessibility,
            maxAccessibility,
            minAccessibility
        ).enqueue(object : Callback<ActivityModel>{
            override fun onResponse(call: Call<ActivityModel>, response: Response<ActivityModel>) {
                try {
                    val model = response.body()!!
                    Log.d("Activity Response", model.toString())
                    _remoteStatus.postValue(ActivityStatus.Remote.Succeed(ActivityEntity(
                        model.key,
                        model.activity,
                        model.accessibility,
                        model.type,
                        model.participants,
                        model.price,
                        false,
                    )))
                }catch (e: Exception){
                    e.printStackTrace()
                    _remoteStatus.postValue(ActivityStatus.Remote.Failed(FailureCode.NO_ACTIVITY_BY_THIS_REQUEST))
                }
            }

            override fun onFailure(call: Call<ActivityModel>, t: Throwable) {
                t.printStackTrace()
                _remoteStatus.postValue(ActivityStatus.Remote.Failed(FailureCode.DEFAULT))
            }
        })
    }

    fun getAllActivities(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _localStatus.postValue(ActivityStatus.Local.Succeed(repository.getAllActivities()))
            }catch (e: Exception){
                e.printStackTrace()
                _localStatus.postValue(ActivityStatus.Local.Failed())
            }
        }
    }

    fun saveActivity(activity: ActivityEntity){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.saveActivity(activity)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun updateActivity(activity: ActivityEntity){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.updateActivity(activity)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}