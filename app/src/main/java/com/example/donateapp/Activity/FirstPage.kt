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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.donateapp.*
import com.example.donateapp.DataClasses.Items
import com.example.donateapp.Models.Encryption
import com.example.donateapp.Models.NetworkHandler
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.fasterxml.jackson.module.kotlin.*
import org.json.JSONObject


class FirstPage : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    private var itemList = mutableListOf<Items>()
    private var uid = ""
    private var imageUrl = ""
    private lateinit var memoryCache: LruCache<String, String>
    private var jsonStr = ""
    private val cache = LruChache<String>(8) //<Array>
    private val cacheKeys = mutableListOf<String>()
    private var cacheItemList = mutableListOf<Items>()
    private var cacheItemJson = mutableListOf<String>()
    private var whichList = mutableListOf<Items>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)

        if (NetworkHandler.isOnline(this)) {
            whichList = itemList
            Toast.makeText(this, "FIREBASE DATA", Toast.LENGTH_LONG).show()
        } else {
            getCacheData()
            whichList = cacheItemList
            Toast.makeText(this, "CACHE LIST", Toast.LENGTH_LONG).show()
        }


        db = FirebaseFirestore.getInstance()
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

        val docRef = db.collection("items")
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                var cacheKey = 1
                itemList.clear()
                for (document in snapshot.documents) {
                    val item = document.toObject(Items::class.java)
                    if (item != null) {
                        itemList.add(item)
                        jsonStr = mapper.writeValueAsString(item)
                        cache.put(cacheKey.toString(), jsonStr)
                        cacheKeys.add(cacheKey.toString())
                        println("!!!Saving Data ${jsonStr}")
                        cacheItemJson.add(jsonStr)
                    }
                    adapter.notifyDataSetChanged()
                    cacheKey++
                }
            }
            getCacheData()
        }

        updateImage(imageUrl)

    } // ON CREATE

    private fun getData () {
        val itemRef = db.collection("items")
        itemRef.get()
            .addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot.documents) {
                    val newItem = document!!.toObject(Items::class.java)
                    if (newItem != null) {

                    }
                }
            }
    }

    private fun updateImage(iurl: String){
        if (iurl != "") {
            val itemImage = findViewById<ImageView>(R.id.item_image)
            Glide.with(this@FirstPage)
                .load(iurl)
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

    //Get and put the cache data into a list
    private fun getCacheData () {
        val mapper = jacksonObjectMapper()
        var cacheKey = 1
        var jsonStrPosition = 0
        var number = 0

        for (i in cacheItemJson) {
            //cache.get(cacheKey.toString())
            var cacheItem = mapper.readValue<Items>(cacheItemJson[jsonStrPosition])
            cacheItemList.add(cacheItem)
            cacheKey++
            jsonStrPosition++
        }
        println("!!! CacheList SIZE: ${cacheItemList.size}")

        for (i in cacheItemList) {
            println("!!! ITEM: ${number} ${cacheItemList[number].title}")
            number++
        }
        //val preferences = getSharedPreferences("app_cache", Context.MODE_PRIVATE)
        //title = preferences.getString("0", cacheItemJson[0]).toString()
        //println("!!!TITLE: ${title}")
    }
}