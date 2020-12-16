package com.example.donateapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detal_information.*

class DetalInformation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detal_information)

        // get data from putextra
        var intent = intent
        val title = intent.getStringExtra("titleText")
        val titleDecscription = intent.getStringExtra("descriptionText")

        // set text from another activity?

        title_item.text = title
        title_description.text = titleDecscription








    }
}