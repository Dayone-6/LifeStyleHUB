package ru.dayone.lifestylehub.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.createSkeleton
import ru.dayone.lifestylehub.R
import ru.dayone.lifestylehub.databinding.WidgetWeatherBinding
import ru.dayone.lifestylehub.data.local.AppPrefs

class Weather(context: Context, attr: AttributeSet) : LinearLayout(context, attr) {
    private var _binding: WidgetWeatherBinding? = null
    private val binding get() = _binding!!

    private val ivWeatherSkeleton: Skeleton
    private val tvTempSkeleton: Skeleton
    private val tvTempRangeSkeleton: Skeleton
    private val tvTempFeelSkeleton: Skeleton
    private val tvCitySkeleton: Skeleton
    private val tvWeatherType: Skeleton

    init {
        _binding = WidgetWeatherBinding.inflate(LayoutInflater.from(context), this, false)

        val skeletonConfig = AppPrefs.getSkeletonConfig()
        ivWeatherSkeleton = binding.ivWeatherIcon.createSkeleton(skeletonConfig)
        tvTempSkeleton = binding.tvWeatherTemp.createSkeleton(skeletonConfig)
        tvTempRangeSkeleton = binding.tvWeatherFromTo.createSkeleton(skeletonConfig)
        tvTempFeelSkeleton = binding.tvWeatherPersonTemp.createSkeleton(skeletonConfig)
        tvCitySkeleton = binding.tvWeatherCity.createSkeleton(skeletonConfig)
        tvWeatherType = binding.tvWeatherValue.createSkeleton(skeletonConfig)


        ivWeatherSkeleton.showSkeleton()
        tvCitySkeleton.showSkeleton()
        tvTempSkeleton.showSkeleton()
        tvTempFeelSkeleton.showSkeleton()
        tvTempRangeSkeleton.showSkeleton()
        tvWeatherType.showSkeleton()

        addView(binding.root)
    }

    fun startLoading(){
        ivWeatherSkeleton.showSkeleton()
        tvCitySkeleton.showSkeleton()
        tvTempSkeleton.showSkeleton()
        tvTempFeelSkeleton.showSkeleton()
        tvTempRangeSkeleton.showSkeleton()
        tvWeatherType.showSkeleton()
    }

    fun setTemperature(temp: Int){
        val tempStr = "$temp°C"
        binding.tvWeatherTemp.text = tempStr
    }

    fun setTemperatureRange(minValue: Int, maxValue: Int){
        val range = "$minValue°C ... $maxValue°C"
        binding.tvWeatherFromTo.text = range
    }

    fun setTemperatureFeel(temp: Int){
        val tempStr = "($temp°C)"
        binding.tvWeatherPersonTemp.text = tempStr
    }

    fun setWeatherIconCode(code: Int){
        val icon: Int = if(code == 800){
            R.drawable.ic_weather_clear
        }else{
            when (code / 100){
                2 -> {R.drawable.ic_weather_thunder}
                3 -> {R.drawable.ic_weather_rainy}
                5 -> {R.drawable.ic_weather_rainy}
                6 -> {R.drawable.ic_weather_snow}
                7 -> {R.drawable.ic_weather_fog}
                8 -> {R.drawable.ic_weather_cloudy}
                else -> {R.drawable.ic_weather_clear}
            }
        }
        binding.ivWeatherIcon.setImageResource(icon)
    }

    fun setCity(city: String){
        binding.tvWeatherCity.text = city
    }

    fun setType(type: String){
        binding.tvWeatherValue.text = type
    }

    fun endLoading(){
        ivWeatherSkeleton.showOriginal()
        tvCitySkeleton.showOriginal()
        tvTempSkeleton.showOriginal()
        tvTempFeelSkeleton.showOriginal()
        tvTempRangeSkeleton.showOriginal()
        tvWeatherType.showOriginal()
    }
}