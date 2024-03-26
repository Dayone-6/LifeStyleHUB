package ru.dayone.lifestylehub.ui.find_activity

import android.content.Context
import retrofit2.Call
import ru.dayone.lifestylehub.data.local.activity.ActivityDatabase
import ru.dayone.lifestylehub.data.local.activity.ActivityEntity
import ru.dayone.lifestylehub.data.remote.RetrofitClient
import ru.dayone.lifestylehub.data.remote.activity.ActivityModel
import ru.dayone.lifestylehub.data.remote.activity.ActivityService

class FindActivityRepository(context: Context) {
    private val database = ActivityDatabase.getInstance(context)
    private val activityDao = database.getActivityDao()

    private val client = RetrofitClient.getActivityClient()
    private val service = client.create(ActivityService::class.java)

    fun getAllActivities(): List<ActivityEntity> {
        return activityDao.getAllActivities()
    }

    fun saveActivity(activity: ActivityEntity) {
        activityDao.saveActivity(activity)
    }

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
    ): Call<ActivityModel> {
        return service.getActivity(
            key,
            type,
            participants,
            maxPrice,
            minPrice,
            price,
            accessibility,
            maxAccessibility,
            minAccessibility
        )
    }

    fun updateActivity(activity: ActivityEntity){
        activityDao.updateActivity(activity)
    }
}