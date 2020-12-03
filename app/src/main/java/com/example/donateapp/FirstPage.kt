package com.example.donateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirstPage : AppCompatActivity() {


    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    private var itemList = mutableListOf<Items>()
    private var uid = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val adapter = RecyclerviewAdapter(this, itemList)

        //itemList.add(Items())
        val donateRecyclerview = findViewById<RecyclerView>(R.id.donate_list)
        donateRecyclerview.layoutManager = LinearLayoutManager(this)
        donateRecyclerview.adapter = adapter

        val titleText = findViewById<TextView>(R.id.title)
        val  titleDescription = findViewById<TextView>(R.id.description)

        // Kollar vad som händer med datan
        val docRef = db.collection("items")
        docRef.addSnapshotListener{ snapshot, e ->
            if( snapshot != null ) {
                itemList.clear()
                for (document in snapshot.documents) {
                    val temp = document.toObject(Items::class.java)
                    if (temp != null)
                        itemList.add(temp)
                    adapter.notifyDataSetChanged()
                   // println("!!!${temp?.title}")
                }
                adapter.notifyDataSetChanged()
            }
            getData()
        }

    }
    // Hämtar hem data från Firebase.
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

}
//RecyclerviewAdapter.submitList(itemList)
