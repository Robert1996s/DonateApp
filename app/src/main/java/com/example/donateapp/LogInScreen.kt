package com.example.donateapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64.encodeToString
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.spec.PSSParameterSpec.DEFAULT
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class LogInScreen : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    fun hej():Int{

        return 3

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        //val preferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        //val editor = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit()

        //Private och public key?




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
            //User is logged in
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
                        toFirstPage()

                    }

                }
            return true
        }
    }
}
