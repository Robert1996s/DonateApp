package com.example.donateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class FirstPage : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    private var testList = mutableListOf<Items>()
    private var itemUid = ""


    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_page)


        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val adapter = ItemAdapter(this, testList)
        val recyclerView = findViewById<RecyclerView>(R.id.test_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val textTitle = findViewById<TextView>(R.id.title_text)
        val textDescription = findViewById<TextView>(R.id.description_text)


        val docRef = db.collection("items")
        docRef.addSnapshotListener{ snapshot, e ->
            if( snapshot != null ) {
                testList.clear()
                for (document in snapshot.documents) {
                    val temp = document.toObject(Items::class.java)
                    if(temp != null)
                        testList.add(temp)
                    println("!!!${temp?.title}")
                }
                adapter.notifyDataSetChanged()
            }
        }

        println("!!!Logged In As: ${auth.currentUser?.email}")
        getItemData()

    }

    private fun setData () {
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

    private fun getItemData() {
        val docRef = db.collection("items")
        docRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val temp = document.toObject(Items::class.java)
                    if (temp != null){
                        testList.add(temp)
                    }
                }
            }
        setData ()
        println("!!!Done Getting data")
    }
}