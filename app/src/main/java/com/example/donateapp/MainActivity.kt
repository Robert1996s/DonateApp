package com.example.donateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    lateinit var recyclerView: RecyclerView

    lateinit var userTextView: EditText
    lateinit var passwordTextView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        var shoppingItems = mutableListOf<Items>()

        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            toUserSignUp()
        }

    }

    private fun toUserSignUp() {

        val intent = Intent(this, FirstPage::class.java)
        startActivity(intent)
    }

}





