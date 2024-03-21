package ru.dayone.lifestylehub.utils


open class Status {
    data class Success(val code: SuccessCode): Status()
    data class Failure(val code: FailureCode): Status()
    class InProgress(): Status()
    class Default(): Status()
}