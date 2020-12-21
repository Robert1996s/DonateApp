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
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class FirstPage : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    private var itemList = mutableListOf<Items>()
    private var itemUid = ""
    private var uid = ""
    private var imageUrl = ""
    private lateinit var memoryCache: LruCache<String, Bitmap>
    private var internetConnection = false


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       var hasInternetType =  isOnline(this)

        println("Internet  status = "+hasInternetType)

        setContentView(R.layout.activity_first_page)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        //val itemImage = findViewById<ImageView>(R.id.item_image)

        //Encryption
        var map = Encryption().encrypt("Mitt hemliga meddelande".toByteArray(), "hejhejhej".toCharArray())

        val base64Encrypted = Base64.encodeToString(map["encrypted"], Base64.NO_WRAP)
        val base64Iv = Base64.encodeToString(map["iv"], Base64.NO_WRAP)
        val base64Salt = Base64.encodeToString(map["salt"], Base64.NO_WRAP)

        println("!!! Encrypted Message:"+base64Encrypted)

        val cache = LruChache<String>(2)

        val message = "Hallå"
        cache.put("1", message)
        cache.put("2", "Two")

        cache.get("1")
        cache.put("3", "Three")

        //assert((cache.get("1") == "One"))

        println("!!! cache data:" +cache.get("1"))

        //Decoding
        val encrypted = Base64.decode(base64Encrypted, Base64.NO_WRAP)
        val iv = Base64.decode(base64Iv, Base64.NO_WRAP)
        val salt = Base64.decode(base64Salt, Base64.NO_WRAP)

        val decrypted = Encryption().decrypt(
            hashMapOf("iv" to iv, "salt" to salt, "encrypted" to encrypted), "hejhejhej".toCharArray())


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
                itemList.clear()
                for (document in snapshot.documents) {
                    val item = document.toObject(Items::class.java)
                    if (item != null) {
                        itemList.add(item)
                        println("!!! ${item.item_image_url}")
                        imageUrl = item.item_image_url.toString()
                    }
                    adapter.notifyDataSetChanged()
                }
            }
            println("!!!Logged In As: ${auth.currentUser?.email}")
            getData()
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
                        //val bild = newItem.item_image_url
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
                    var networkvalue = "cellular"
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
            println("!!! NO ACCESS") // Cache
        }
    }




    /*private fun mergeSort(itemList: List<Int>): List<Int> {
        if (itemList.size <= 1) {
            return itemList
        }
        val middle = itemList.size / 2
        var left = itemList.subList(0, middle)
        var right = itemList.subList(middle,itemList.size)

        //return merge(mergeSort(left), mergeSort(right))
        return merge(mergeSort(left), mergeSort(right))
    }

    private fun merge(left: List<Int>, right: List<Int>) {
        var indexLeft = 0
        var indexRight = 0
        var newList : MutableList<Int> = mutableListOf()

        while (indexLeft < left.count() && indexRight < right.count()) {
            if (left[indexLeft] <= right[indexRight]) {
                newList.add(left[indexLeft])
                indexLeft++
            } else {
                newList.add(right[indexRight])
            }
        }
    } */

}