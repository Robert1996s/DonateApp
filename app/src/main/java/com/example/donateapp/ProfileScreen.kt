package com.example.donateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ProfileScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    private var myItemList = mutableListOf<Items>()
    private var uid = ""
    private var myItemList = mutableListOf<Items>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser: FirebaseUser? = auth.currentUser

        val userNameText = findViewById<TextView>(R.id.username_display)
        val userEmailText = findViewById<TextView>(R.id.user_email_display)
        val signOutBtn = findViewById<Button>(R.id.signout_button)


        val adapter = ProfileListAdapter(this, myItemList)
        val recyclerView = findViewById<RecyclerView>(R.id.my_items_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter



        signOutBtn.setOnClickListener {
            logOut()
        }



        if (currentUser != null){
            uid = auth.currentUser!!.uid
            getProfileInfo()

        }

        val docRef = db.collection("users").document(uid).collection("userItems")
        docRef.addSnapshotListener{ snapshot, e ->
            if( snapshot != null ) {
                for (document in snapshot.documents) {
                    val item = document.toObject(Items::class.java)
                    if(item != null)
                        myItemList.add(item)
                    //imageUrl = temp!!.item_image_url.toString()
                    println("!!! ${item?.title}")
                    println("!!! ${item?.description}")

                }
            }
        }


    } // ON CREATE


    private fun getProfileInfo() {

        val docRef = db.collection("users").document(uid)
        val userNameText = findViewById<TextView>(R.id.username_display)
        val userEmailText = findViewById<TextView>(R.id.user_email_display)

        docRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val document = task.result
                    if (document!!.exists()) {
                        println("!!! Data exists! Data: ${document.data}")
                        val userInfo = document.toObject(UserData::class.java)

                        userNameText.text = userInfo!!.display_name.toString()
                        userEmailText.text = userInfo.email.toString()
                    }
                    else {
                        println("!!! Get profile data went wrong")
                    }
                }
                getMyItems()
            }
    }

    private fun getMyItems() {
        val docRef = db.collection("users").document(uid).collection("userItems")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot.documents) {
                    val myItem = document!!.toObject(Items::class.java)
                    if (myItem != null) {
                        myItemList.add(myItem)
                        println("!!!${myItem?.title}")
                    }
                }
            }
    }

    /*{ documentSnapshot ->
        for (document in documentSnapshot.documents) {
            val newItem = document!!.toObject(Items::class.java)
            if (newItem != null) {
                println("!!!${newItem?.title}")
            }
        }
    } */



    private fun logOut() {
        auth.signOut()
        println("!!!You signed out")
        val intent = Intent(this, LogInScreen::class.java)
        startActivity(intent)
    }
}