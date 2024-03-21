package ru.dayone.lifestylehub.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.api.weather.model.WeatherModel
import ru.dayone.lifestylehub.prefs.AppPrefs
import ru.dayone.lifestylehub.utils.FailureCode
import ru.dayone.lifestylehub.utils.LocationStatus
import ru.dayone.lifestylehub.utils.WeatherStatus
import java.util.Locale

class HomeViewModel(
    context: Context
) : ViewModel() {
    private val _weatherStatus: MutableLiveData<WeatherStatus> = MutableLiveData()
    val weatherStatus: LiveData<WeatherStatus> = _weatherStatus

    private val _locationStatus: MutableLiveData<LocationStatus> = MutableLiveData()
    val locationStatus: LiveData<LocationStatus> = _locationStatus

    private val weatherRepository = WeatherRepository()
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val geocoder = Geocoder(context, Locale.getDefault())

    fun getWeather(apiKey: String, lat: Double, lon: Double, lang: String){
        weatherRepository.getWeatherCall(apiKey, lon, lat, lang).enqueue(object : Callback<WeatherModel>{
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                Log.d("Data", response.body().toString())
                if(response.body() != null) {
                    _weatherStatus.postValue(WeatherStatus.Success(response.body()!!))
                }else{
                    _weatherStatus.postValue(WeatherStatus.Failure(FailureCode.GET_WEATHER_FAILED))
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                _weatherStatus.postValue(WeatherStatus.Failure(FailureCode.GET_WEATHER_FAILED))
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun getLocation(){
        if(!AppPrefs.getIsAuthorized()){
            _locationStatus.value = LocationStatus.Failure(FailureCode.USER_NOT_AUTHORIZED)
            return
        }else if(!AppPrefs.getIsLocationPermissionGranted()){
            _locationStatus.value = LocationStatus.Failure(FailureCode.GET_LOCATION_PERMISSION_DENIED)
            return
        }
        val provider: String = if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            LocationManager.GPS_PROVIDER
        }else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            LocationManager.NETWORK_PROVIDER
        }else if(locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)){
            LocationManager.PASSIVE_PROVIDER
        }else{
            _locationStatus.value = LocationStatus.Failure(FailureCode.LOCATION_UNAVAILABLE)
            return
        }
        locationManager.requestLocationUpdates(
            provider,
            200,
            5000f
        ) {location ->
            try {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    geocoder.getFromLocation(
                        location.latitude,
                        location.longitude,
                        1, (Geocoder.GeocodeListener {
                            _locationStatus.postValue(LocationStatus.Success(location, it[0].locality))
                        }))
                }else{
                    CoroutineScope(Dispatchers.IO).launch {
                        val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        withContext(Dispatchers.Main) {
                            _locationStatus.postValue(LocationStatus.Success(location, address!![0].locality))
                        }
                    }
                }
            }catch (e: Exception){
                _locationStatus.postValue(LocationStatus.Failure(FailureCode.DEFAULT))
            }
        }
    }
}