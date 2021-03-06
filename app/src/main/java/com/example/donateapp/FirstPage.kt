package com.example.donateapp

//import com.bumptech.glide.request.RequestOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.LruCache
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.fasterxml.jackson.module.kotlin.*

class FirstPage : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    private var itemList = mutableListOf<Items>()
    private var itemUid = ""
    private var uid = ""
    private var imageUrl = ""
    private lateinit var memoryCache: LruCache<String, String>
    private var internetConnection = false
    private var jsonStr = ""
    private val cache = LruChache<String>(8)
    


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var hasInternetType =  isOnline(this)
        println("Internet  status = "+hasInternetType)
        setContentView(R.layout.activity_first_page)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val titleText = findViewById<TextView>(R.id.title_text)
        //val itemImage = findViewById<ImageView>(R.id.item_image)


        InternetModel()



        //Encryption
        var map = Encryption().encrypt("Mitt hemliga meddelande".toByteArray(), "hejhejhej".toCharArray())
        val base64Encrypted = Base64.encodeToString(map["encrypted"], Base64.NO_WRAP)
        val base64Iv = Base64.encodeToString(map["iv"], Base64.NO_WRAP)
        val base64Salt = Base64.encodeToString(map["salt"], Base64.NO_WRAP)

        println("!!! Encrypted Message:"+base64Encrypted)

        val mapper = jacksonObjectMapper()
      
        //För att se att vi cachar ut data ur databsen
        val getJson = mapper.writeValueAsString(itemList)

        println("!!!${getJson}")
      
        //val cache = LruChache<String>(8)

        InternetModel()


        //Decoding
        val encrypted = Base64.decode(base64Encrypted, Base64.NO_WRAP)
        val iv = Base64.decode(base64Iv, Base64.NO_WRAP)
        val salt = Base64.decode(base64Salt, Base64.NO_WRAP)

        val decrypted = Encryption().decrypt(hashMapOf("iv" to iv, "salt" to salt, "encrypted" to encrypted), "hejhejhej".toCharArray())


        //Decrypting the message to string
        var decryptedMessage: String? = null
        decrypted?.let {
            decryptedMessage = String(it, Charsets.UTF_8)
        }

        println("!!!Decrypted Message:"+decryptedMessage)



        val adapter = ItemAdapter(this, itemList)
        val recyclerView = findViewById<RecyclerView>(R.id.test_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {
            uid = auth.currentUser!!.uid
        }


        /*memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return super.sizeOf(key, value)
            }
        } */

        /*fun loadBitmap(resId: Int, imageView: ImageView) {
            val imageKey: String = resId.toString()

            val bitmap: Bitmap? = getBitmapFromMemCache(imageKey)?.also {
                itemImage.setImageBitmap(it)
            } ?: run {
                itemImage.setImageResource(R.drawable.ite)
                val task = BitmapWorkerTask()
                task.execute(resId)
                null
            }
        } */
        
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

                        imageUrl = item.item_image_url.toString()
                        val cacheTitle = item.title.toString()
                        cacheKey++

                        mapper.writeValueAsString(cacheTitle)
                        cache.put("1", cacheTitle)
                        println("!!!getData${cache.get("1")}")

                        val cacheTitle = item.title.toString()
                        //imageUrl = item.item_image_url.toString()
                        jsonStr = mapper.writeValueAsString(item)
                        cache.put(cacheKey.toString(), jsonStr)
                        println("!!!JSONSTRING: ${jsonStr}")

                    }
                    adapter.notifyDataSetChanged()
                    cacheKey++
                }
                println("!!! GET CACHE" + cache.get("1"))
                println("!!! GET CACHE" + cache.get("2"))
                println("!!! GET CACHE" + cache.get("3"))
            }
            getCacheData()
        }
        getData()
        updateImage(imageUrl)



        println("!!!Logged In As: ${auth.currentUser?.email}")
    } // ON CREATE



    private fun getData () {
        val itemRef = db.collection("items")
        itemRef.get()
            .addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot.documents) {
                    val newItem = document!!.toObject(Items::class.java)
                    if (newItem != null) {
                        newItem.title
                    }
                }
                //ConnectivityUtils.isConnected(this)
                //print("!!!!!!${ConnectivityUtils}")
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(context: Context): String {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {

                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.d("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    var networkvalue = "cellular/4G/5G"
                    internetConnection = true
                    checkInternet(internetConnection)
                    return networkvalue

                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    internetConnection = true
                    checkInternet(internetConnection)
                    var networkvalue = "Wifi"
                    return networkvalue
                }
                else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    var networkvalue = "ethernet"
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    internetConnection = true
                    checkInternet(internetConnection)

                    return networkvalue
                }
            }}

        return "none"
    }

    private fun checkInternet(checkInternet : Boolean) {
        if (checkInternet) {
            println("!!! INTERNET ACCESS") // Internet
        }
        else {
            //  Get/Display the cache data
            println("!!! NO ACCESS") // Cache

            val cache1 = cache.get("1")
            cache.get("2")
            cache.get("3")
            cache.get("4")
            cache.get("5")
        }
    }

    private fun getCacheData () {

        val cache1 = cache.get("1")
        val cache2 = cache.get("2")
        val cache3 = cache.get("3")
        val mapper = jacksonObjectMapper()

        val item:Items = mapper.readValue<Items>(cache1.toString())
        println("!!!Cache1 ITEM: " + item.title)
    }

    