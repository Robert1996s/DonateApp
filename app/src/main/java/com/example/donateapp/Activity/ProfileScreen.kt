package com.example.donateapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.donateapp.DataClasses.Items
import com.example.donateapp.DataClasses.Model
import com.example.donateapp.DataClasses.NameViewModel
import com.example.donateapp.ProfileListAdapter
import com.example.donateapp.R
import com.example.donateapp.Models.FirebaseData
import com.example.donateapp.Models.GlobalItemList
import com.example.donateapp.Models.GlobalUserItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_profile_screen.*
import java.util.*
import kotlin.collections.ArrayList
import android.database.Observable as Observable1

class ProfileScreen() : AppCompatActivity(), java.util.Observer {

    private lateinit var auth: FirebaseAuth
    private var myItemList = mutableListOf<Items>()
    private var uid = ""
    //var imageLink = ""
    private var data = ""
    private val model: NameViewModel by viewModels()
    var myModel: Model? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)

        auth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = auth.currentUser
        val signOutBtn = findViewById<Button>(R.id.signout_button)
        val threadBtn = findViewById<Button>(R.id.button_thread)
        val userName = findViewById<TextView>(R.id.username_display)
        val userEmail = findViewById<TextView>(R.id.user_email_display)

        if (currentUser != null){
            uid = auth.currentUser!!.uid
            //data = FirebaseData().getProfileInfo(uid)
            //println("!!!Email:${data} Name:${data}")
            //data = username_display.text.toString()
            //data.email = userEmail.text.toString()
        }

        /*val nameObserver = Observer<String> { newName ->
            username_display.text = newName
        } */
        //model.currentName.observe(this, nameObserver)



        myModel = Model()
        myModel!!.getData(uid)
        myModel!!.addObserver(this)




        println("!!!DATA: ${myModel}")


        if (GlobalUserItems.globalUserItemList.size > 0) {
            println("!!!Already Have items")
        } else {
            FirebaseData().getUserItemsData(uid)
        }

        println("!!! LIST SIZE PROFILE: ${GlobalItemList.globalItemList.size}")

        threadBtn.setOnClickListener {
            val thread = Thread(Runnable {
                println("!!!Thread Sleep")
                //Thread.sleep(5000)
                println("!!!Thread Woke UP")
                //val otherValue = "New Value"
                //model.currentName.setValue(otherValue)

            })

            thread.start()
            //backToRecyclerView()
        }

        //observerData()

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



    override fun update(arg0: Observable, arg1: Any?) {
        user_email_display.text = myModel!!.getValueAtIndex(0)
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