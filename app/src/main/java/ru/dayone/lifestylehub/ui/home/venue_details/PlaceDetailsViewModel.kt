package ru.dayone.lifestylehub.ui.home.venue_details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.dayone.lifestylehub.data.local.details.PlaceDetailsEntity
import ru.dayone.lifestylehub.data.remote.places.details.model.DetailsResponseModel
import ru.dayone.lifestylehub.data.remote.places.model.PhotoData
import ru.dayone.lifestylehub.utils.CACHE_RELEVANCE_TIME
import ru.dayone.lifestylehub.utils.FailureCode
import ru.dayone.lifestylehub.utils.MAIN_DELIMITER
import ru.dayone.lifestylehub.utils.status.PlaceDetailsStatus

class PlaceDetailsViewModel(
    context: Context
) : ViewModel() {
    private val repository = PlaceDetailsRepository(context)

    private val _status: MutableLiveData<PlaceDetailsStatus> = MutableLiveData()
    val status: LiveData<PlaceDetailsStatus> = _status

    private var details: PlaceDetailsEntity? = null

    fun getDetails(id: String, token: String, date: String, apiKey: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val localData = repository.getLocalDetails(id)
                if (localData == null) {
                    invokeGetRemoteDetails(id, token, date)
                    invokeGetPhotos(id, apiKey)
                } else {
                    if ((localData.gotTime - System.currentTimeMillis()) >= CACHE_RELEVANCE_TIME) {
                        repository.deleteDetails(localData)
                        invokeGetRemoteDetails(id, token, date)
                        invokeGetPhotos(id, apiKey)
                    } else {
                        _status.postValue(PlaceDetailsStatus.Succeed(localData))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _status.postValue(PlaceDetailsStatus.Failed(FailureCode.GET_PLACE_DETAILS_FAILED))
            }
        }
    }

    private fun invokeGetRemoteDetails(id: String, token: String, date: String) {
        repository.getRemoteDetails(id, date, token)
            .enqueue(object : Callback<DetailsResponseModel> {
                override fun onResponse(
                    call: Call<DetailsResponseModel>,
                    response: Response<DetailsResponseModel>
                ) {
                    try {
                        if (response.body()!!.metadata.code != 200) {
                            _status.postValue(PlaceDetailsStatus.Failed(FailureCode.GET_PLACE_DETAILS_FAILED))
                            return
                        }
                        val data = response.body()!!.response.venue
                        if (details == null) {
                            details = PlaceDetailsEntity(
                                id,
                                data.name,
                                data.mainPhoto.urlPrefix,
                                data.mainPhoto.urlSuffix,
                                data.location.fullAddress.joinToString(),
                                (data.category.map { it.name }).joinToString(separator = MAIN_DELIMITER),
                                data.url ?: "",
                                if (data.likes != null) {
                                    data.likes.count
                                } else {
                                    0
                                },
                                if (data.hours != null) {
                                    data.hours.status
                                } else {
                                    ""
                                },
                                if (data.hours != null) {
                                    data.hours.isOpen
                                } else {
                                    false
                                },
                                if (data.phone != null) {
                                    data.phone.formattedPhone
                                } else {
                                    ""
                                },
                                System.currentTimeMillis()
                            )
                        } else {
                            details = PlaceDetailsEntity(
                                id,
                                data.name,
                                data.mainPhoto.urlPrefix,
                                data.mainPhoto.urlSuffix + MAIN_DELIMITER + details!!.suffixes,
                                data.location.fullAddress.joinToString(),
                                (data.category.map { it.name }).joinToString(separator = MAIN_DELIMITER),
                                data.url ?: "",
                                if (data.likes != null) {
                                    data.likes.count
                                } else {
                                    0
                                },
                                if (data.hours != null) {
                                    data.hours.status
                                } else {
                                    ""
                                },
                                if (data.hours != null) {
                                    data.hours.isOpen
                                } else {
                                    false
                                },
                                if (data.phone != null) {
                                    data.phone.formattedPhone
                                } else {
                                    ""
                                },
                                System.currentTimeMillis()
                            )
                            _status.postValue(PlaceDetailsStatus.Succeed(details!!))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        _status.postValue(PlaceDetailsStatus.Failed(FailureCode.DEFAULT))
                    }

                }

                override fun onFailure(call: Call<DetailsResponseModel>, t: Throwable) {
                    t.printStackTrace()
                    _status.postValue(PlaceDetailsStatus.Failed(FailureCode.GET_PLACE_DETAILS_FAILED))
                }

            })
    }

    private fun invokeGetPhotos(id: String, token: String) {
        repository.getPhotos(id, token).enqueue(object : Callback<List<PhotoData>> {
            override fun onResponse(
                call: Call<List<PhotoData>>,
                response: Response<List<PhotoData>>
            ) {
                try {
                    val urls =
                        response.body()!!.joinToString(separator = MAIN_DELIMITER) { it.urlSuffix }
                    if (details == null) {
                        details = PlaceDetailsEntity(
                            "",
                            "",
                            "",
                            urls,
                            "",
                            "",
                            "",
                            0,
                            "",
                            false,
                            "",
                            0
                        )
                    } else {
                        details!!.suffixes = details!!.suffixes + MAIN_DELIMITER + urls
                        _status.postValue(PlaceDetailsStatus.Succeed(details!!))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _status.postValue(PlaceDetailsStatus.Failed(FailureCode.DEFAULT))
                }
            }

            override fun onFailure(call: Call<List<PhotoData>>, t: Throwable) {
                t.printStackTrace()
                _status.postValue(PlaceDetailsStatus.Failed(FailureCode.GET_PLACE_DETAILS_FAILED))
            }

        })
    }
}