package ru.dayone.lifestylehub.utils.status

import ru.dayone.lifestylehub.utils.FailureCode
import ru.dayone.lifestylehub.utils.SuccessCode


open class Status {
    data class Success(val code: SuccessCode): Status()
    data class Failure(val code: FailureCode): Status()
    class InProgress : Status()
    class Default : Status()
}