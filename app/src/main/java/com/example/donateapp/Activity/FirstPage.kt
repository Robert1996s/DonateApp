package com.example.donateapp.Activity


import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.LruCache
import android.os.Build
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.donateapp.*
import com.example.donateapp.DataClasses.Items
import com.example.donateapp.DataClasses.Model
import com.example.donateapp.DataClasses.UserData
import com.example.donateapp.Models.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.fasterxml.jackson.module.kotlin.*
import java.util.Observer

class FirstPage : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private var uid = ""
    private var imageUrl = ""
    private lateinit var memoryCache: LruCache<String, String>
    private var jsonStr = ""
    private val cache = LruChache<String>(8) //<Array>
    private val cacheKeys = mutableListOf<String>()
    private var cacheItemList = mutableListOf<Items>()
    private var cacheItemJson = mutableListOf<String>()
    private var whichList = mutableListOf<Items>()
    private lateinit var viewModel: Model

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)








        println("!!! LIST SIZE FIRST PAGE: ${GlobalItemList.globalItemList.size}")

        if (NetworkHandler.isOnline(this)) {
            FirebaseData().getItemsData()
            CacheData().cacheData()
            whichList = GlobalItemList.globalItemList
        } else {
            cacheItemList = CacheData().getCacheData()
            whichList = cacheItemList
        }

        auth = FirebaseAuth.getInstance()
        val mapper = jacksonObjectMapper()

        //Encryption
        var map = Encryption()
            .encrypt("Mitt hemliga meddelande".toByteArray(), "hejhejhej".toCharArray())
        val base64Encrypted = Base64.encodeToString(map["encrypted"], Base64.NO_WRAP)
        val base64Iv = Base64.encodeToString(map["iv"], Base64.NO_WRAP)
        val base64Salt = Base64.encodeToString(map["salt"], Base64.NO_WRAP)

        println("!!! Encrypted Message:"+base64Encrypted)

        //Decoding
        val encrypted = Base64.decode(base64Encrypted, Base64.NO_WRAP)
        val iv = Base64.decode(base64Iv, Base64.NO_WRAP)
        val salt = Base64.decode(base64Salt, Base64.NO_WRAP)
        val decrypted = Encryption()
            .decrypt(hashMapOf("iv" to iv, "salt" to salt, "encrypted" to encrypted), "hejhejhej".toCharArray())

        //Decrypting the message to string
        var decryptedMessage: String? = null
        decrypted?.let {
            decryptedMessage = String(it, Charsets.UTF_8)
        }
        println("!!!Decrypted Message:"+decryptedMessage)

        val adapter = ItemAdapter(this, whichList)
        val recyclerView = findViewById<RecyclerView>(R.id.test_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            uid = auth.currentUser!!.uid
        }



        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
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
    } // ON CREATE


    private fun addPost() {
        val intent = Intent(this, PostItem::class.java)
        startActivity(intent)
    }

    private fun toProfile() {
        val intent = Intent(this, ProfileScreen::class.java)
        startActivity(intent)
    }
}