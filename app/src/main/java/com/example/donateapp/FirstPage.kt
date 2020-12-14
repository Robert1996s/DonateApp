package com.example.donateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
//import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.row_card.*


class FirstPage : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    private var itemList = mutableListOf<Items>()
    private var itemUid = ""
    private var uid = ""
    private var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        val itemImage = findViewById<ImageView>(R.id.item_image)

        val adapter = ItemAdapter(this, itemList)
        val recyclerView = findViewById<RecyclerView>(R.id.test_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null){
            uid = auth.currentUser!!.uid
        }

        //val bottomHome = findViewById<TextView>(R.id.title_text)
        //val textDescription = findViewById<TextView>(R.id.addButton)
        //val homeIcon = findViewById<ImageView>(R.id.home_icon)

        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.addButton -> {
                    addPost()

                    false
                }
                R.id.profileButton -> {
                    toProfile()

                    false
                }
                else -> true
            }
        }

        val docRef = db.collection("items")
        docRef.addSnapshotListener{ snapshot, e ->
            if( snapshot != null ) {
                itemList.clear()
                for (document in snapshot.documents) {
                    val temp = document.toObject(Items::class.java)
                    if(temp != null)
                        itemList.add(temp)
                    imageUrl = temp!!.item_image_url.toString()
                    println("!!! ${temp.item_image_url}")
                }
                adapter.notifyDataSetChanged()
            }
        }
        println("!!!Logged In As: ${auth.currentUser?.email}")
        getData()
    }

    private fun getData () {
        val itemRef = db.collection("items")
        itemRef.get()
            .addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot.documents) {
                    val newItem = document!!.toObject(Items::class.java)
                    if (newItem != null) {
                        println("!!!${newItem?.title}")
                    }
                }
            }
        updateImage()
    }

    private fun updateImage(){
        if (imageUrl != "") {
            val itemImage = findViewById<ImageView>(R.id.item_image)
            Glide.with(this)
                .load(imageUrl)//.apply(RequestOptions.centerCropTransform())
                .into(itemImage)
        }
    }

    private fun addPost() {
        val intent = Intent(this, PostItem::class.java)
        startActivity(intent)
    }

    private fun toProfile() {
        val intent = Intent(this, ProfileScreen::class.java)
        startActivity(intent)
    }
}