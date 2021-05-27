package com.example.donateapp.Models

import android.os.Handler
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.engine.Resource
import com.example.donateapp.DataClasses.*
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.*

class FirebaseData () {


    lateinit var db: FirebaseFirestore

    private lateinit var model: Model

     var userEmailText = MutableLiveData<String>()

    fun getEmail(): LiveData<String>{
        return userEmailText
    }

     fun getUserItemsData(uid: String)  {


        db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(uid).collection("userItems")
        docRef.addSnapshotListener { snapshot, e ->
            GlobalUserItems.globalUserItemList.clear()
            if (snapshot != null) {
                for (document in snapshot.documents) {
                    val item = document.toObject(Items::class.java)
                    if (item != null) {
                        GlobalUserItems.globalUserItemList.add(item)
                        println("!!! MY ITEMS: ${item.title}")

                    }
                }
            }

        }


    }

    fun postData(title: String, description: String, adress: String, uid: String)  {
        db = FirebaseFirestore.getInstance()
        val item = Items(
            title,
            description,
            adress)
            db.collection("items").add(item)
            db.collection("users").document(uid).collection("userItems").add(item)
    }

    fun getItemsData() {
        db = FirebaseFirestore.getInstance()
        val docRef = db.collection("items")
        docRef.addSnapshotListener { snapshot, e ->
            GlobalItemList.globalItemList.clear()
            if (snapshot != null) {
                for (document in snapshot.documents) {
                    val item = document.toObject(Items::class.java)
                    if (item != null) {
                        GlobalItemList.globalItemList.add(item)
                    }
                }
                println("!!!GETITEMSDATA")
            }
        }
    }




     fun getProfileInfo(uid: String)  {


        db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(uid)
        docRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){


                    val document = task.result
                    if (document!!.exists()) {
                        println("!!!Firebase Data: ${document.data}")
                        val userInfo = document.toObject(UserData::class.java)!!

                        userEmailText.value = userInfo.email
                    }
                    else {
                        println("!!! Get profile data went wrong")
                    }






            }



          //No value when return
    }


     }

    fun getItemsOnce () {
        db = FirebaseFirestore.getInstance()
        val docRef = db.collection("items")
        docRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val document = task.result
                    GlobalItemList.globalItemList.clear()
                    if (document != null) {
                        for (document in document){
                            val item = document.toObject(Items::class.java)
                            GlobalItemList.globalItemList.add(item)
                        }
                    }
                    else {
                        println("!!! Get profile data went wrong")
                    }
                }
            }
    }
}

//: MutableList<Items>
//return GlobalItemList.globalItemList