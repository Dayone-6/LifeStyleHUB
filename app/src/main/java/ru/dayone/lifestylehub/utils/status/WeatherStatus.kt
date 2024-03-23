package ru.dayone.lifestylehub.utils.status

import ru.dayone.lifestylehub.data.remote.weather.model.WeatherModel
import ru.dayone.lifestylehub.utils.FailureCode

open class WeatherStatus {
    data class Success(val weather: WeatherModel): WeatherStatus()
    data class Failure(val failCode: FailureCode): WeatherStatus()
}