package com.example.donateapp.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class testModel : ViewModel() {

    val _currentUserEmail = MutableLiveData<String>()

    val currentUserEmail: LiveData<String>
    get() = _currentUserEmail

    init {
        _currentUserEmail.value = ""
    }


}