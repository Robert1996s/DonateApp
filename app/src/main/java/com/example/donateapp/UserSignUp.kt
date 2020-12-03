package com.example.donateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_user_sign_up.*

class UserSignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_up)

        val userInputField = findViewById<TextView>(R.id.userName_register)
        val passwordRegister = findViewById<TextView>(R.id.password_register)
        val userEmail = findViewById<TextView>(R.id.email_register)
        val registerButton = findViewById<Button>(R.id.signup_button)

        registerButton.setOnClickListener {

            val name : String = userInputField.text.toString()
            val password : String = passwordRegister.text.toString()
            val email : String = userEmail.text.toString()

            createAccount()

        }
    }
    private fun createAccount() {

    }

}