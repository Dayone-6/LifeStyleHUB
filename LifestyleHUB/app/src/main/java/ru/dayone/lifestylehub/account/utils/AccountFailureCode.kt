package ru.dayone.lifestylehub.account.utils

enum class AccountFailureCode {
    LOGIN_ALREADY_EXISTS,
    DEFAULT,
    BAD_EMAIL,
    GET_RANDOM_USER_FAILED,
    INVALID_PASSWORD,
    LOGIN_NOT_EXISTS
}