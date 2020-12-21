package com.example.donateapp

//import com.bumptech.glide.request.RequestOptions
import android.content.Context
import android.content.Intent
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
    private var internetConnection = false


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       var hasInternetType =  isOnline(this)

        println("Internet  status = "+hasInternetType)

        setContentView(R.layout.activity_first_page)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val itemImage = findViewById<ImageView>(R.id.item_image)

        val adapter = ItemAdapter(this, itemList)
        val recyclerView = findViewById<RecyclerView>(R.id.test_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val currentUser: FirebaseUser? = auth.currentUser

        if (currentUser != null) {
            uid = auth.currentUser!!.uid
        }

        //val bottomHome = findViewById<TextView>(R.id.title_text)
        //val textDescription = findViewById<TextView>(R.id.addButton)
        //val homeIcon = findViewById<ImageView>(R.id.home_icon)

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
                        imageUrl = item!!.item_image_url.toString()
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
                        val bild = newItem.item_image_url
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