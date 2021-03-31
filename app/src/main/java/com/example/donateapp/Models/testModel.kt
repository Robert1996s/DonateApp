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


    fun getData(uid :String): String {
        var userEmail = FirebaseData().getProfileInfo(uid)
        println("!!!getDATA: ${userEmail}")
        return userEmail
    }
}