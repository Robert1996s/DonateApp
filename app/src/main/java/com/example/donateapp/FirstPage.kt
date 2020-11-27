package com.example.donateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirstPage : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val itemList = ArrayList<Items>()

        val donateRecyclerview = findViewById<RecyclerView>(R.id.donate_list)

        donateRecyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = RecyclerviewAdapter(itemList, this )
        donateRecyclerview.adapter = adapter
    }
}