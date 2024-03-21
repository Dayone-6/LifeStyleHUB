package ru.dayone.lifestylehub.ui.home

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.api.weather.model.WeatherModel
import ru.dayone.lifestylehub.databinding.FragmentHomeBinding
import ru.dayone.lifestylehub.utils.FailureCode
import ru.dayone.lifestylehub.utils.LocationStatus
import ru.dayone.lifestylehub.utils.WEATHER_API_KEY
import ru.dayone.lifestylehub.utils.WeatherStatus
import java.util.Locale
import kotlin.math.roundToInt


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var location: Location? = null
    private var city: String? = null

    private lateinit var homeViewModel: HomeViewModel

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(requireContext())
        )[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel.locationStatus.observe(viewLifecycleOwner){
            when(it){
                is LocationStatus.Success -> {
                    location = it.location
                    city = it.city
                    getWeather()
                }
                is LocationStatus.Failure -> {
                    when(it.failCode){
                        FailureCode.DEFAULT -> {
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.message_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        FailureCode.GET_LOCATION_PERMISSION_DENIED -> {
                            weatherNotAvailable(getString(R.string.text_access_location))
                        }
                        FailureCode.LOCATION_UNAVAILABLE -> {
                            weatherNotAvailable(getString(R.string.text_enable_location))
                        }
                        FailureCode.USER_NOT_AUTHORIZED -> {
                            weatherNotAvailable(getString(R.string.text_weather_available_with_account))
                        }
                        else -> {}
                    }
                }
            }
        }

        startWeatherUiLoading()
        homeViewModel.getLocation()

        homeViewModel.weatherStatus.observe(viewLifecycleOwner){
            when(it){
                is WeatherStatus.Success -> {
                    setUpWeather(it.weather)
                }
                is WeatherStatus.Failure -> {
                    when(it.failCode) {
                        FailureCode.GET_WEATHER_FAILED -> { weatherNotAvailable(getString(R.string.message_failed)) }
                        else -> {}
                    }
                }
            }
        }

        binding.homeRefresh.setOnRefreshListener {
            startWeatherUiLoading()
            homeViewModel.getLocation()
        }

        return binding.root
    }

    private fun getWeather(){
        homeViewModel.getWeather(
            WEATHER_API_KEY,
            location!!.latitude,
            location!!.longitude,
            if (Locale.getDefault().language == "ru") {
                "ru"
            } else {
                "en"
            }
        )
    }

    private fun startWeatherUiLoading(){
        binding.viewWeather.visibility = View.VISIBLE
        binding.viewWeather.startLoading()
        binding.tvWeatherMessage.visibility = View.GONE
        binding.homeRefresh.isRefreshing = true
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

        binding.homeRefresh.isRefreshing = false
        binding.viewWeather.endLoading()
    }

    private fun weatherNotAvailable(message: String){
        binding.viewWeather.visibility = View.GONE
        binding.tvWeatherMessage.visibility = View.VISIBLE
        binding.tvWeatherMessage.text = message
        binding.homeRefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}