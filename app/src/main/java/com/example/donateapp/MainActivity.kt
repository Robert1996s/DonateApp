package com.example.donateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

        val loginButton = findViewById<Button>(R.id.login_button)

        loginButton.setOnClickListener {
            toUserLogin()
        }

        val signUpButton = findViewById<Button>(R.id.sign_up_button)

        signUpButton.setOnClickListener {
            touUserSignUp()
        }

    }

    private fun toUserLogin() {

        val intent = Intent(this, FirstPage::class.java)
        startActivity(intent)
    }

    private fun touUserSignUp() {

        val intent = Intent(this, UserSignUp::class.java)
        startActivity(intent)
    }


}





