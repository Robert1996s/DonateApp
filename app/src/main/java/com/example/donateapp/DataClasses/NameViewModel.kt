package com.example.donateapp.DataClasses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NameViewModel : ViewModel() {
    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}