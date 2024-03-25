package ru.dayone.lifestylehub.ui.leisure

import android.content.Context
import ru.dayone.lifestylehub.data.local.leisure.LeisureDatabase
import ru.dayone.lifestylehub.data.local.leisure.LeisureEntity

class LeisureRepository(
    context: Context
) {
    private val database = LeisureDatabase.getInstance(context)
    private val leisureDao = database.getLeisureDao()

    fun addLeisure(leisure: LeisureEntity){
        leisureDao.addLeisure(leisure)
    }

    fun getAllLeisure(userLogin: String): List<LeisureEntity>{
        return leisureDao.getAllLeisure(userLogin)
    }

    fun deleteLeisure(id: Int){
        leisureDao.deleteLeisure(id)
    }
}