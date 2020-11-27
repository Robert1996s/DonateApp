package com.example.donateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class UserSignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_sign_up)

        val userNameInput = findViewById<TextView>(R.id.userName_register)
        val userPasswordInput = findViewById<TextView>(R.id.password_register)
        val userEmailInput = findViewById<TextView>(R.id.email_register)
        val registerBtn = findViewById<Button>(R.id.signup_button)


        registerBtn.setOnClickListener {
            val name:String = userEmailInput.text.toString()
            val password:String = userPasswordInput.text.toString()
            val email:String = userEmailInput.text.toString()
            createAccount()
        }
    }

    private fun createAccount() {

    }
}