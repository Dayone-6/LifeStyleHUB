package ru.dayone.lifestylehub.account.utils

open class AccountStatus {
    class Succeed : AccountStatus()
    data class Failed(val message: String) : AccountStatus()
}