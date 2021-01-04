package com.example.donateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.concurrent.thread

class ProfileScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    private var myItemList = mutableListOf<Items>()
    private var uid = ""
    var imageLink = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val currentUser: FirebaseUser? = auth.currentUser

        val userNameText = findViewById<TextView>(R.id.username_display)
        val userEmailText = findViewById<TextView>(R.id.user_email_display)
        val signOutBtn = findViewById<Button>(R.id.signout_button)
        val threadBtn = findViewById<Button>(R.id.button_thread)


        threadBtn.setOnClickListener {
            val thread = Thread(Runnable {
                println("!!!Thread Sleep")
                Thread.sleep(5000)
                println("!!!Thread Woke UP")
            })
            thread.start()
        }


        val adapter = ProfileListAdapter(this, myItemList)
        val recyclerView = findViewById<RecyclerView>(R.id.my_items_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        myItemList.add(Items("Bil", "En blå bil"))
        myItemList.add(Items("zebra", "Ingen Häst!"))
        myItemList.add(Items("Cykel", "Oanvänd cykel"))
        myItemList.add(Items("Tekokare", "Nästan helt ny"))
        myItemList.add(Items("Båt", "Gammal båt"))




        signOutBtn.setOnClickListener {
            logOut()
        }

        if (currentUser != null){
            uid = auth.currentUser!!.uid
            getProfileInfo()
        }

        val docRef = db.collection("users").document(uid).collection("userItems")
        docRef.addSnapshotListener{ snapshot, e ->
            if( snapshot != null ) {
                for (document in snapshot.documents) {
                    val item = document.toObject(Items::class.java)

                    if(item != null){

                    //imageUrl = temp!!.item_image_url.toString()
                 //   println("!!! ${item?.title}")
                   // println("!!! ${item?.description}")
                    //println("!!! ${item?.item_image_url}")
                }

                }

            }
        }



        var words = arrayListOf<String>("banan", "zebra", "annanas", "Bamse","Dolly", "Örjan")

        fun bubbleSortAlphabet(words: ArrayList<String>): ArrayList<String> {
            println("!! påbörjar sortering")
            var swap = true
            var letters = arrayOf("a", "b", "c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","å","ä","ö")
            var changes = 0
            while (swap){
                swap = false
                for(i in 0 until words.size -1){
                    changes ++
                    val wordToCheck =  words[i]
                    val wordToCheckNext =  words[i+1]
                    val first = wordToCheck.substring(0,1)
                    val second = wordToCheckNext.substring(0,1)
                    val firstWord = letters.indexOf(first.toLowerCase())
                    val secondWord = letters.indexOf(second.toLowerCase())
                    if (firstWord > secondWord) {
                        val temp = words[i]
                        words[i] = words[i + 1]
                        words[i + 1] = temp
                        swap = true
                    }
                    print("!!!change:${changes}${words}")
                }
            }
            return words
        }

        var list = bubbleSortAlphabet(words)
        bubbleSortAlphabet(words)
        println("!!!${words}")

    } // ON CREATE

private fun getProfileInfo() {

        val docRef = db.collection("users").document(uid)
        val userNameText = findViewById<TextView>(R.id.username_display)
        val userEmailText = findViewById<TextView>(R.id.user_email_display)

        docRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val document = task.result
                    if (document!!.exists()) {
                        println("!!!Data: ${document.data}")
                        val userInfo = document.toObject(UserData::class.java)

                        userNameText.text = userInfo!!.display_name.toString()
                        userEmailText.text = userInfo.email.toString()
                    }
                    else {
                        println("!!! Get profile data went wrong")
                    }
                }
                getMyItems()
            }
    }

    private fun getMyItems() {
        val docRef = db.collection("users").document(uid).collection("userItems")
        docRef.get()
            .addOnSuccessListener { documentSnapshot ->
                for (document in documentSnapshot.documents) {
                    val myItem = document!!.toObject(Items::class.java)
                    if (myItem != null) {
                        myItemList.add(myItem)
                        println("!!!${myItem?.title}")
                    }
                }
            }
    }

    /*{ documentSnapshot ->
        for (document in documentSnapshot.documents) {
            val newItem = document!!.toObject(Items::class.java)
            if (newItem != null) {
                println("!!!${newItem?.title}")
            }
        }
    } */

    /*private fun bubbleSort (list:IntArray):IntArray {
        var swap = true
        while (swap) {
            swap = false
            for (i in 0 until list.size-1) {
                if (list[i] > list[i + 1]) {
                    val temp = list[i]
                    list[i] = list[i + 1]
                    list[i + 1] = temp
                    swap = true
                }
            }
        }
        return list
    }

    private fun main(args: Array<String>) {
        val list = bubbleSort(intArrayOf(2,15,1,8,4))
        for (k in list) print("!!!$k ")
    } */


    private fun logOut() {
        auth.signOut()
        println("!!!You signed out")
        val intent = Intent(this, LogInScreen::class.java)
        startActivity(intent)
    }
}