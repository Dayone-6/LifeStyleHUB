package ru.dayone.lifestylehub.utils

import ru.dayone.lifestylehub.api.weather.model.WeatherModel

open class WeatherStatus {
    data class Success(val weather: WeatherModel): WeatherStatus()
    data class Failure(val failCode: FailureCode): WeatherStatus()
}