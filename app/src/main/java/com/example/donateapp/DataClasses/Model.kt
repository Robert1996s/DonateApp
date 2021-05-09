package com.example.donateapp.DataClasses


import android.content.Context
import android.os.Handler
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.donateapp.Models.FirebaseData
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList
import java.util.Timer
import kotlin.concurrent.schedule
import kotlinx.coroutines.*



class Model : ViewModel () {

    val userInfo: MutableList<String>


    lateinit var db: FirebaseFirestore

    private val _userEmail = MutableLiveData<String>()

    fun userEmail(): LiveData<String>{
        return _userEmail
    }


    private val modelData = MutableLiveData<String>()

    fun getData(): LiveData<String>{
        return modelData
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
        //setChanged()
        //notifyObservers()
    }

      fun getFirebaseData (uid: String) {

        db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(uid)
        docRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val document = task.result
                    if (document!!.exists()) {
                        println("!!!Firebase Data: ${document.data}")
                        val userInfo = document.toObject(UserData::class.java)!!

                        _userEmail.value = userInfo.email

                        //stringObs().stringEmail = userInfo.email.toString()

                        //Model().userEmail(userInfo.email.toString())


                        //userNameText = userInfo!!.display_name.toString()
                        //userEmailText = userInfo.email.toString()

                        //testModel().currentUserEmail = userInfo.email.toString()

                        ///OBSERVE HÄR??

                        //Model().userEmail() = userInfo.email

                        //  var testet = coroutineScope {
                        //     val data = async { FirebaseData().getProfileInfo(uid)   }
                        //    modelData.value = data.await().toString()
                        //   }
                        //   println("!!! coroutines model data : $testet")
                        // }


                    }
                    else {
                        println("!!! Get profile data went wrong")
                    }






                }



                //No value when return
            }


    }


}