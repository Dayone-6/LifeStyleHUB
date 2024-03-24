package ru.dayone.lifestylehub.utils.status

import ru.dayone.lifestylehub.data.local.leisure.LeisureEntity

open class LeisureStatus {
    data class Succeed(val leisure: List<LeisureEntity>) : LeisureStatus()
    class Failed : LeisureStatus()

}