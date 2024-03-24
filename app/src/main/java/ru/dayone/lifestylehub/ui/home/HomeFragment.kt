package ru.dayone.lifestylehub.ui.home

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.adapters.FeedAdapter
import ru.dayone.lifestylehub.databinding.FragmentHomeBinding
import ru.dayone.lifestylehub.model.FormattedPlaceModel
import ru.dayone.lifestylehub.data.local.AppPrefs
import ru.dayone.lifestylehub.data.remote.weather.model.WeatherModel
import ru.dayone.lifestylehub.utils.DATE_KEY
import ru.dayone.lifestylehub.utils.FailureCode
import ru.dayone.lifestylehub.utils.FeedItem
import ru.dayone.lifestylehub.utils.PAGINATION_LIMIT
import ru.dayone.lifestylehub.utils.status.LocationStatus
import ru.dayone.lifestylehub.utils.PLACES_OAUTH_KEY
import ru.dayone.lifestylehub.utils.status.PlacesStatus
import ru.dayone.lifestylehub.utils.WEATHER_API_KEY
import ru.dayone.lifestylehub.utils.status.WeatherStatus
import java.util.Locale
import kotlin.math.roundToInt


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var location: Location? = null
    private var city: String? = null

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var placesSkeleton: Skeleton

    private var isPlacesLoaded: Boolean = false
    private var isWeatherLoaded: Boolean = false

    private lateinit var feedAdapter: FeedAdapter

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
                    if(AppPrefs.getIsAuthorized()) {
                        getWeather()
                    }else{
                        weatherNotAvailable(getString(R.string.text_weather_available_with_account))
                    }
                    AppPrefs.setLocation(location!!)
                    getPlaces()
                }
                is LocationStatus.Failure -> {
                    binding.homeRefresh.isRefreshing = false
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
                            placesNotAvailable(getString(R.string.text_there_is_no_places))
                        }
                        FailureCode.LOCATION_UNAVAILABLE -> {
                            weatherNotAvailable(getString(R.string.text_enable_location))
                            placesNotAvailable(getString(R.string.text_there_is_no_places))
                        }
                        else -> {}
                    }
                }
            }
        }

        feedAdapter = FeedAdapter(listOf(FeedItem.PagingControl()), requireContext(), homeViewModel, findNavController())
        binding.rvPlaces.adapter = feedAdapter
        binding.rvPlaces.layoutManager = LinearLayoutManager(requireContext())

        startPlacesUiLoading()
        startWeatherUiLoading()
        homeViewModel.getLocation()

        if(!AppPrefs.getIsAuthorized()){
            weatherNotAvailable(getString(R.string.text_weather_available_with_account))
        }

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

        homeViewModel.placesStatus.observe(viewLifecycleOwner){
            when(it){
                is PlacesStatus.Succeed -> { onGetPlacesSucceed(it.placesResponseModel) }
                is PlacesStatus.Failed -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.message_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.homeRefresh.setOnRefreshListener {
            if(!isPlacesLoaded || !isWeatherLoaded){
                binding.homeRefresh.isRefreshing = false
                return@setOnRefreshListener
            }
            feedAdapter = FeedAdapter(listOf(FeedItem.PagingControl()), requireContext(), homeViewModel, findNavController())
            startPlacesUiLoading()
            startWeatherUiLoading()
            homeViewModel.getLocation()
        }

        return binding.root
    }

    private fun onGetPlacesSucceed(places: List<FormattedPlaceModel>){
        val lastPosition = binding.rvPlaces.layoutManager!!.onSaveInstanceState()!!
        feedAdapter.extendData(places.map { FeedItem.Place(it) })
        isPlacesLoaded = true
        placesSkeleton.showOriginal()
        if(isWeatherLoaded){
            binding.homeRefresh.isRefreshing = false
        }
        binding.rvPlaces.layoutManager!!.onRestoreInstanceState(lastPosition)
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
            },
        )
    }

    private fun getPlaces(){
        homeViewModel.getPlaces(
            PLACES_OAUTH_KEY,
            "${location!!.latitude},${location!!.longitude}",
            DATE_KEY,
            PAGINATION_LIMIT,
            0
        )
    }

    private fun startWeatherUiLoading(){
        binding.viewWeather.visibility = View.VISIBLE
        binding.viewWeather.startLoading()
        binding.tvWeatherMessage.visibility = View.GONE
        isWeatherLoaded = false
    }

    private fun startPlacesUiLoading(){
        binding.tvPlacesMessage.visibility = View.GONE
        binding.rvPlaces.visibility = View.VISIBLE
        placesSkeleton = binding.rvPlaces.applySkeleton(
            R.layout.item_place,
            PAGINATION_LIMIT,
            AppPrefs.getSkeletonConfig()
        )
        placesSkeleton.showSkeleton()
        isPlacesLoaded = false
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
        binding.viewWeather.setCity(city!!)

        isWeatherLoaded = true
        binding.viewWeather.endLoading()
        if(isPlacesLoaded){
            binding.homeRefresh.isRefreshing = false
        }
    }

    private fun weatherNotAvailable(message: String){
        binding.viewWeather.visibility = View.GONE
        binding.tvWeatherMessage.visibility = View.VISIBLE
        binding.tvWeatherMessage.text = message
        isWeatherLoaded = true
    }

    private fun placesNotAvailable(message: String){
        binding.rvPlaces.visibility = View.GONE
        binding.tvPlacesMessage.apply {
            visibility = View.VISIBLE
            text = message
        }
        isPlacesLoaded = true

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}