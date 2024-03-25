package ru.dayone.lifestylehub.ui.venue_details

import android.content.Context
import ru.dayone.lifestylehub.data.local.leisure.LeisureDatabase
import ru.dayone.lifestylehub.data.local.leisure.LeisureEntity

class DetailsLeisureRepository(
    context: Context
) {
    private val database = LeisureDatabase.getInstance(context)
    private val leisureDao = database.getLeisureDao()

    fun deleteLeisure(id: Int){
        leisureDao.deleteLeisure(id)
    }

    fun getLeisureByPlaceId(userLogin: String, placeId: String): List<LeisureEntity>{
        return leisureDao.getLeisureByPLaceId(userLogin, placeId)
    }
}