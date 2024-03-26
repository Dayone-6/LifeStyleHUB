package ru.dayone.lifestylehub.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dayone.lifestylehub.model.FormattedPlaceModel
import ru.dayone.lifestylehub.data.local.AppPrefs
import ru.dayone.lifestylehub.data.remote.places.model.PlacesResponseModel
import ru.dayone.lifestylehub.data.remote.weather.model.WeatherModel
import ru.dayone.lifestylehub.utils.FailureCode
import ru.dayone.lifestylehub.utils.status.LocationStatus
import ru.dayone.lifestylehub.utils.status.PlacesStatus
import ru.dayone.lifestylehub.utils.status.WeatherStatus
import java.util.Locale

class HomeViewModel(
    context: Context
) : ViewModel() {
    private val _weatherStatus: MutableLiveData<WeatherStatus> = MutableLiveData()
    val weatherStatus: LiveData<WeatherStatus> = _weatherStatus

    private val _locationStatus: MutableLiveData<LocationStatus> = MutableLiveData()
    val locationStatus: LiveData<LocationStatus> = _locationStatus

    private val _placesStatus: MutableLiveData<PlacesStatus> = MutableLiveData()
    val placesStatus: LiveData<PlacesStatus> = _placesStatus

    private val weatherRepository = WeatherRepository()
    private val placesRepository = PlacesRepository()

    private val locationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val geocoder = Geocoder(context, Locale.getDefault())

    private val fusedLocationProvider = LocationServices.getFusedLocationProviderClient(context)
    private val isGoogleAvailable = GoogleApiAvailability.getInstance()
        .isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS


    fun getWeather(apiKey: String, lat: Double, lon: Double, lang: String) {
        weatherRepository.getWeatherCall(apiKey, lon, lat, lang)
            .enqueue(object : Callback<WeatherModel> {
                override fun onResponse(
                    call: Call<WeatherModel>,
                    response: Response<WeatherModel>
                ) {
                    try {
                        Log.d("Data", response.body().toString())
                        if (response.body() != null) {
                            _weatherStatus.postValue(WeatherStatus.Success(response.body()!!))
                        } else {
                            _weatherStatus.postValue(WeatherStatus.Failure(FailureCode.GET_WEATHER_FAILED))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        _weatherStatus.postValue(WeatherStatus.Failure(FailureCode.GET_WEATHER_FAILED))
                    }
                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    _weatherStatus.postValue(WeatherStatus.Failure(FailureCode.GET_WEATHER_FAILED))
                }
            })
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        Log.d("GetLocationVm", "!!")
        if (!AppPrefs.getIsLocationPermissionGranted()) {
            _locationStatus.value =
                LocationStatus.Failure(FailureCode.GET_LOCATION_PERMISSION_DENIED)
            return
        }
        if (!isGoogleAvailable) {
            val provider: String =
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    LocationManager.GPS_PROVIDER
                } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    LocationManager.NETWORK_PROVIDER
                } else if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                    LocationManager.PASSIVE_PROVIDER
                } else {
                    _locationStatus.value = LocationStatus.Failure(FailureCode.LOCATION_UNAVAILABLE)
                    return
                }
            locationManager.requestLocationUpdates(
                provider,
                200,
                5000f
            ) { location ->
                getGeocodedLocation(location)
            }
        } else {
            fusedLocationProvider.requestLocationUpdates(
                LocationRequest
                    .Builder(200)
                    .setMaxUpdates(1)
                    .build(),
                {
                    getGeocodedLocation(it)
                },
                Looper.getMainLooper()
            )
        }
    }

    private fun getGeocodedLocation(location: Location) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1

                ) {
                    _locationStatus.postValue(
                        LocationStatus.Success(
                            location,
                            it[0].locality
                        )
                    )
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    // этот метод нужен для получения локации в версиях андроида < tiramisu
                    val address =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    withContext(Dispatchers.Main) {
                        _locationStatus.postValue(
                            LocationStatus.Success(
                                location,
                                address!![0].locality
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            _locationStatus.postValue(LocationStatus.Failure(FailureCode.DEFAULT))
        }
    }

    fun getPlaces(token: String, ll: String, date: String, limit: Int, offset: Int) {
        placesRepository.getPlacesCall(token, ll, date, limit, offset)
            .enqueue(object : Callback<PlacesResponseModel> {
                override fun onResponse(
                    call: Call<PlacesResponseModel>,
                    response: Response<PlacesResponseModel>
                ) {
                    try {
                        if (response.body()!!.metaData.code != 200) {
                            _placesStatus.postValue(PlacesStatus.Failed(FailureCode.GET_PLACES_FAILED))
                            return
                        }
                        val formattedPlaces = ArrayList<FormattedPlaceModel>()
                        for (result in response.body()!!.response.group.results) {
                            try {
                                Log.d("Data", result.photo.toString())
                                formattedPlaces.add(
                                    FormattedPlaceModel(
                                        id = result.place.id,
                                        name = result.place.name,
                                        address = result.place.location.fullAddress.joinToString(),
                                        categories = result.place.categories.map { it.name },
                                        photoUrl = result.photo.urlPrefix + "1000x400/" + result.photo.urlSuffix!!.substring(
                                            1
                                        ),
                                        allCount = response.body()!!.response.group.totalCount
                                    )
                                )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        Log.d("PlacesGet", "get")
                        _placesStatus.postValue(PlacesStatus.Succeed(formattedPlaces))
                    } catch (e: Exception) {
                        e.printStackTrace()
                        _placesStatus.postValue(PlacesStatus.Failed(FailureCode.DEFAULT))
                    }
                }

                override fun onFailure(call: Call<PlacesResponseModel>, t: Throwable) {
                    _placesStatus.postValue(PlacesStatus.Failed(FailureCode.GET_PLACES_FAILED))
                }
            })

    }
}