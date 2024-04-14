package ru.dayone.lifestylehub.utils.status

import ru.dayone.lifestylehub.account.model.User

open class UserInfoStatus {
    data class Succeed(val user: User): UserInfoStatus()
    data class Failed(val message: String): UserInfoStatus()
}