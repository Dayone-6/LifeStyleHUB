package ru.dayone.lifestylehub.account.utils

open class AccountStatus {
    data class Succeed(val code: AccountSuccessCode) : AccountStatus()
    data class Failed(val code: AccountFailureCode) : AccountStatus()
    class InProcess : AccountStatus()
    class Default : AccountStatus()
}