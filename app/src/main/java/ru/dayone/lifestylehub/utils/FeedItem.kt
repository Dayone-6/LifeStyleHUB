package ru.dayone.lifestylehub.utils

import ru.dayone.lifestylehub.model.FormattedPlaceModel

open class FeedItem(
    val type: Int
){
    data class Place(val place: FormattedPlaceModel): FeedItem(FeedItemType.Place.ordinal)
    class PagingControl: FeedItem(FeedItemType.Paging.ordinal)
}

enum class FeedItemType{
    Place,
    Paging
}