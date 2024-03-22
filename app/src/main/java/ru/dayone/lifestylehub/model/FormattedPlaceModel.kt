package ru.dayone.lifestylehub.model

data class FormattedPlaceModel(
    val id: String,
    val name: String,
    val address: String,
    val categories: List<String>,
    val photoUrl: String,
    val allCount: Int
)