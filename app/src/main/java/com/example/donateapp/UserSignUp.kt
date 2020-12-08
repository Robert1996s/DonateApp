package com.example.donateapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class UserSignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_up)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val userNameInput = findViewById<TextView>(R.id.userName_register)
        val userPasswordInput = findViewById<TextView>(R.id.password_register)
        val userEmailInput = findViewById<TextView>(R.id.email_register)
        val registerBtn = findViewById<Button>(R.id.signup_button)


        registerBtn.setOnClickListener {
            val userName:String = userEmailInput.text.toString()
            val userPassword:String = userPasswordInput.text.toString()
            val userEmail:String = userEmailInput.text.toString()
            createAccount(userName, userPassword, userEmail)
        }
    }

    private fun createAccount(name: String, password:String, email:String) {
        if (name == "" || email == "" || password == "") {
            Toast.makeText(this, "Wrong Input", Toast.LENGTH_LONG).show()
        }
        else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful) {
                        //Sign in Successful
                        println("!!!User Created")
                        val user = auth.currentUser
                        userSignedUp()
                    }
                }
            }
    }

    private fun userSignedUp() {
        val intent = Intent(this, FirstPage::class.java)
        startActivity(intent)
    }
}