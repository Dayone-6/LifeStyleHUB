package ru.dayone.lifestylehub.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dayone.lifestylehub.api.weather.model.WeatherModel
import ru.dayone.lifestylehub.utils.FailureCode
import ru.dayone.lifestylehub.utils.Status
import ru.dayone.lifestylehub.utils.SuccessCode

class HomeViewModel : ViewModel() {
    private val _status: MutableLiveData<Status> = MutableLiveData()
    val status: LiveData<Status> = _status

    private val _weatherData: MutableLiveData<WeatherModel> = MutableLiveData()
    val weatherData: LiveData<WeatherModel> = _weatherData

    private val weatherRepository = WeatherRepository()

    fun getWeather(apiKey: String, lat: Double, lon: Double, lang: String){
        weatherRepository.getWeatherCall(apiKey, lon, lat, lang).enqueue(object : Callback<WeatherModel>{
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                _weatherData.postValue(response.body())
                Log.d("Data", response.body().toString())
                _status.postValue(Status.Success(SuccessCode.GET_WEATHER_SUCCEED))
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                _status.postValue(Status.Failure(FailureCode.GET_WEATHER_FAILED))
            }
        })
    }
}