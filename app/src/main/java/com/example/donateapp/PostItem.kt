package com.example.donateapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_post_item.*

class PostItem : AppCompatActivity() {

    lateinit var db : FirebaseFirestore
    private var imageUri : Uri?  = null

    companion object {
        private val IMAGE_PICK_CODE  = 1000
        private val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_item)


        val itemTitle = findViewById<TextView>(R.id.editTextDonate)
        val itemAdress = findViewById<TextView>(R.id.editTextLocation)
        val itemDescription = findViewById<TextView>(R.id.editTextFullDescription)


        db = FirebaseFirestore.getInstance()

        val addPicture = findViewById<Button>(R.id.addPicture)
        val addDonateItem = findViewById<Button>(R.id.add_donate)

        addDonateItem.setOnClickListener {

            addDonatePost()
        }


        addPicture.setOnClickListener {

            addPicture()
        }

    }

    private fun addDonatePost(){

        val itemTitle = editTextDonate.text.toString()
        val itemAdress = editTextLocation.text.toString()
        val itemDescription = editTextFullDescription.text.toString()

        if(itemTitle.isEmpty() || itemAdress.isEmpty() || itemDescription.isEmpty())
            return

        val item =  Items(itemTitle,itemDescription,itemAdress)
        val ref = db.collection("items").add(item)

        // För att användaren inte ska vara kvar på sidan efter ha lagt till på knappen ska vyn försvinna. Kan skapa ett intent som skickar tillbaka till
        // den sida man ska komma till och gör en finish()

    }
    private fun addPicture() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else{
                chooseImageGallery()
            }
        }else{
            chooseImageGallery()
        }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)


    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_CODE )  {

                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageGallery()
                }else{
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }


     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

         val itemImage = findViewById<ImageView>(R.id.imageView2)

        if(requestCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            itemImage.setImageURI(data?.data)

        }
    }
}

