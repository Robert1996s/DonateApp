package com.example.donateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirstPage : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    private var itemData : Items? = null
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val recyclerView = findViewById<RecyclerView>(R.id.test_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemList = ArrayList<Items>()
        val testList = mutableListOf<Items>()
        val adapter = ItemAdapter(this, testList)
        recyclerView.adapter = adapter


        itemList.add(Items("hey", "en sak"))


        //val donateRecyclerview = findViewById<RecyclerView>(R.id.donate_list)

        //donateRecyclerview.layoutManager = LinearLayoutManager(this)

        //val adapter = RecyclerviewAdapter(itemList, this )
        //donateRecyclerview.adapter = adapter

        getData()
    }

    private fun getData() {
        val docRef = db.collection("items")
            docRef.get()
            .addOnSuccessListener { document ->
                if (document != null){
                    Log.d("exists","Data: ${document}")

                }
            }
        println("!!!Done Getting data")
    }
}