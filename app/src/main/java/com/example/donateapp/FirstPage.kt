package com.example.donateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirstPage : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    private var itemList = mutableListOf<Items>()
    private var itemUid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val adapter = ItemAdapter(this, itemList)
        val recyclerView = findViewById<RecyclerView>(R.id.test_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        //just for commit


        val docRef = db.collection("items")
        docRef.addSnapshotListener{ snapshot, e ->
            if( snapshot != null ) {
                itemList.clear()
                for (document in snapshot.documents) {
                    val temp = document.toObject(Items::class.java)
                    if(temp != null)
                        itemList.add(temp)
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
    }
}