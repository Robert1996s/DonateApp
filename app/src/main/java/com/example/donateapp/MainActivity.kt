package com.example.donateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        var shoppingItems = mutableListOf<UserData>()

        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            toUserSignUp()
        }

    } //ON CREATE

    private fun toUserSignUp() {
        val intent = Intent(this, UserSignUp::class.java)
        startActivity(intent)
    }
}

