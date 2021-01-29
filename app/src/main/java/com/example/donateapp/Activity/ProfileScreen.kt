package com.example.donateapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donateapp.DataClasses.Items
import com.example.donateapp.ProfileListAdapter
import com.example.donateapp.R
import com.example.donateapp.DataClasses.UserData
import com.example.donateapp.Models.FirebaseData
import com.example.donateapp.Models.GlobalItemList
import com.example.donateapp.Models.GlobalUserItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile_screen.*

class ProfileScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var myItemList = mutableListOf<Items>()
    private var uid = ""
    //var imageLink = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)

        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        val signOutBtn = findViewById<Button>(R.id.signout_button)
        val threadBtn = findViewById<Button>(R.id.button_thread)

        if (currentUser != null){
            uid = auth.currentUser!!.uid
            //FirebaseData().getProfileInfo(uid)
            getProfileInfo()
        }


        if (GlobalUserItems.globalUserItemList.size > 0) {
            println("!!!Already Have items")
        } else {
            FirebaseData().getUserItemsData(uid)
        }

        println("!!! LIST SIZE PROFILE: ${GlobalItemList.globalItemList.size}")

        threadBtn.setOnClickListener {
            val thread = Thread(Runnable {
                println("!!!Thread Sleep")
                Thread.sleep(5000)
                println("!!!Thread Woke UP")
            })
            thread.start()
            backToRecyclerView()
        }

        val adapter = ProfileListAdapter(this, GlobalUserItems.globalUserItemList)
        val recyclerView = findViewById<RecyclerView>(R.id.my_items_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        signOutBtn.setOnClickListener {
            logOut()
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

        lateinit var db: FirebaseFirestore
        db = FirebaseFirestore.getInstance()
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
                //getMyItems()
            }
    }

    private fun backToRecyclerView() {
        val intent = Intent(this, FirstPage::class.java)
        startActivity(intent)
        finish()
    }

    private fun logOut() {
        auth.signOut()
        println("!!!You signed out")
        val intent = Intent(this, LogInScreen::class.java)
        startActivity(intent)
    }
}