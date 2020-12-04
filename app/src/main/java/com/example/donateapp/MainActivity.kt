package com.example.donateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        var shoppingItems = mutableListOf<Items>()
        val loginButton = findViewById<Button>(R.id.login_button)
        val signUpButton = findViewById<Button>(R.id.button_signup)

        loginButton.setOnClickListener {
            toFirstPage()
        }

        signUpButton.setOnClickListener {
            toUserSignUp()
        }

        val currentUser: FirebaseUser? = auth.currentUser


        if (currentUser != null) {
            //User is logged in
            println("!!!Logged IN")
            val intent = Intent(this, FirstPage::class.java)
            startActivity(intent)
        }
        else
    {
        //Not logged in
        println("!!! NOT Logged IN")
    }


    } //ON CREATE

    private fun toFirstPage() {
        val intent = Intent(this, FirstPage::class.java)
        startActivity(intent)
    }

    private fun toUserSignUp() {
        val intent = Intent(this, UserSignUp::class.java)
        startActivity(intent)
    }
}
