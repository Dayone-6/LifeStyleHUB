package ru.dayone.lifestylehub.ui.my_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyActivityViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is activity Fragment"
    }
    val text: LiveData<String> = _text
}