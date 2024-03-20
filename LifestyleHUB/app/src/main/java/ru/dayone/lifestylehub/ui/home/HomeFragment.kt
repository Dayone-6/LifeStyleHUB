package ru.dayone.lifestylehub.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.dayone.lifestylehub.api.weather.model.WeatherModel
import ru.dayone.lifestylehub.databinding.FragmentHomeBinding
import ru.dayone.lifestylehub.prefs.AppPrefs
import ru.dayone.lifestylehub.utils.Constants
import ru.dayone.lifestylehub.utils.FailureCode
import ru.dayone.lifestylehub.utils.Status
import ru.dayone.lifestylehub.utils.SuccessCode
import java.util.Locale
import kotlin.math.roundToInt


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationClient: LocationManager
    private lateinit var location: Location

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        if(AppPrefs.getIsLocationPermissionGranted()){
            locationClient = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationClient.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                200,
                5000f
            ) {
                location = it
                homeViewModel.getWeather(
                    Constants.WEATHER_API_KEY,
                    it.latitude,
                    it.longitude,
                    if (Locale.getDefault().language == "ru") {
                        "ru"
                    } else {
                        "en"
                    }
                )
            }
        }else{

        }

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel.status.observe(viewLifecycleOwner){
            when(it){
                is Status.Success -> {
                    when(it.code){
                        SuccessCode.GET_WEATHER_SUCCEED -> { onGetWeatherSucceed() }
                        else -> {}
                    }
                }
                is Status.Failure -> {
                    when(it.code){
                        FailureCode.GET_WEATHER_FAILED -> { onGetWeatherFailed() }
                        else -> {}
                    }
                }
            }
        }

        homeViewModel.weatherData.observe(viewLifecycleOwner){weather ->
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            Log.d("Data", "geocode")
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                geocoder.getFromLocation(location.latitude, location.longitude, 1, (Geocoder.GeocodeListener {
                    binding.viewWeather.setCity(it[0].getAddressLine(0))
                    setUpWeather(weather)
                }))
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    withContext(Dispatchers.Main) {
                        binding.viewWeather.setCity(address!![0].locality)
                        setUpWeather(weather)
                    }
                }
            }
        }

        return binding.root
    }

    private fun setUpWeather(weather: WeatherModel){
        binding.viewWeather.setTemperature(weather.mainData.temperature.roundToInt())
        binding.viewWeather.setTemperatureFeel(weather.mainData.feelsLike.roundToInt())
        binding.viewWeather.setWeatherIconCode(weather.type[0].id)
        binding.viewWeather.setTemperatureRange(
            weather.mainData.temperatureMin.roundToInt(),
            weather.mainData.temperatureMax.roundToInt()
        )
        binding.viewWeather.setType(weather.type[0].name)
        binding.viewWeather.endLoading()
    }

    private fun onGetWeatherSucceed(){

    }

    private fun onGetWeatherFailed(){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}