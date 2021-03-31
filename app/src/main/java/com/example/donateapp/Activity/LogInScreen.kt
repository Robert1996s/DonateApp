package com.example.donateapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.donateapp.DataClasses.Items
import com.example.donateapp.Models.FirebaseData
import com.example.donateapp.Models.GlobalItemList
import com.example.donateapp.Models.NetworkHandler
import com.example.donateapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

class LogInScreen : AppCompatActivity() {

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
        val emailInput = findViewById<EditText>(R.id.userEmail_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)



        loginButton.setOnClickListener {
            val emailInput = emailInput.text.toString()
            val passwordInput = passwordInput.text.toString()
            checkLoggedIn (emailInput, passwordInput)
        }

        signUpButton.setOnClickListener {
            toUserSignUp()
        }

        val currentUser: FirebaseUser? = auth.currentUser


        if (currentUser != null) {
            toFirstPage()
        }
        else
        {
        toUserSignUp()
        println("!!! NOT Logged IN")
        }

        if (NetworkHandler.isOnline(this)) {
            Toast.makeText(this, "Welcome ${currentUser?.email}", Toast.LENGTH_LONG).show()
        } else {
            println("!!! NO Internet")
            Toast.makeText(this, "Internet Trouble?", Toast.LENGTH_LONG).show()
        }
        println("!!!Logged In As: ${auth.currentUser?.email}")
        finish()
    } //ON CREATE

    private fun toFirstPage() {
        val intent = Intent(this, FirstPage::class.java)
        startActivity(intent)
    }

    private fun tologIn() {
        val intent = Intent(this, LogInScreen::class.java)
        startActivity(intent)
    }


    private fun toUserSignUp() {
        val intent = Intent(this, UserSignUp::class.java)
        startActivity(intent)
    }

    fun checkLoggedIn (emailInput: String, passwordInput: String):Boolean {
        if (emailInput == "" || passwordInput == "" || emailInput.length >50) {
            println("!!!Wrong Input")
            return false
        }
        else {
            auth.signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful) {
                        println("!!!Logged In")
                        }
                    }
                }
            return true
        }
}


