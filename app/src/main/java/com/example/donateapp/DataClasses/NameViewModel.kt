package com.example.donateapp.DataClasses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NameViewModel : ViewModel() {
    val userEmail: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}