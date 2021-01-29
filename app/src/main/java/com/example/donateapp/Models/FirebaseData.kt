package com.example.donateapp.Models

import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.donateapp.Activity.ProfileScreen
import com.example.donateapp.DataClasses.Items
import com.example.donateapp.DataClasses.UserData
import com.example.donateapp.ItemAdapter
import com.example.donateapp.R
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseData {


    lateinit var db: FirebaseFirestore
    lateinit var userInfo: UserData
    var userNameText = ""
    var userEmailText = ""

    fun getUserItemsData(uid: String) {
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
            GlobalItemList.globalItemList.add(item)
            GlobalUserItems.globalUserItemList.add(item)
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

     fun getProfileInfo(uid: String): UserData {

        db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(uid)
        docRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val document = task.result
                    if (document!!.exists()) {
                        println("!!!Data: ${document.data}")
                        val userInfo = document.toObject(UserData::class.java)

                        userNameText = userInfo!!.display_name.toString()
                        userEmailText = userInfo.email.toString()
                    }
                    else {
                        println("!!! Get profile data went wrong")
                    }
                }
            }
         return userInfo
    }

    fun getDataOnce () {
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