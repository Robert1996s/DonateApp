package com.example.donateapp.DataClasses


import androidx.lifecycle.MutableLiveData
import com.example.donateapp.Models.FirebaseData
import java.util.*
import kotlin.collections.ArrayList

class Model : Observable () {

    val userInfo: MutableList<String>

    val userEmail: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {
        userInfo = ArrayList(1)

        //userInfo.add()
    }
    fun getValueAtIndex(the_index: Int): String {
        return userInfo[the_index]
    }

    fun setValueAtIndex(the_index: Int) {
        userInfo[the_index] = userInfo[the_index] + 1
        setChanged()
        notifyObservers()
    }

    fun getData(uid :String): String {
        var userEmail = FirebaseData().getProfileInfo(uid)
        println("!!!getDATA: ${userEmail}")
        return userEmail
    }
}