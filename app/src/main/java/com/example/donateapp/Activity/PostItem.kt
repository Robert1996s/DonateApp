package com.example.donateapp.Activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import com.example.donateapp.DataClasses.Items
import com.example.donateapp.Models.FirebaseData
import com.example.donateapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_post_item.*
import kotlinx.coroutines.*


class PostItem : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private var imageUri: Uri? = null
    private var uid = ""
    private var firebaseStorage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_item)

        auth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            uid = auth.currentUser!!.uid
        }
        val addPicture = findViewById<Button>(R.id.addPicture)
        val addDonateItem = findViewById<Button>(R.id.add_donate)

        addDonateItem.setOnClickListener {
            //uploadItemDataThread ()
            addDonatePost()
        }

        addPicture.setOnClickListener {
            addPicture()
        }

    } // ON CREATE


    private fun addDonatePost() {
        val itemTitle = editTextDonate.text.toString()
        val itemAdress = editTextLocation.text.toString()
        val itemDescription = editTextFullDescription.text.toString()

        if (itemTitle.isEmpty() || itemAdress.isEmpty() || itemDescription.isEmpty()) {
            Toast.makeText(applicationContext, "Wrong Input", Toast.LENGTH_SHORT).show()

        } else {
            FirebaseData().postData(itemTitle, itemDescription, itemAdress, uid)
            backToRecyclerView()
        }
    }

    private fun addPicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions,
                    PERMISSION_CODE
                )
            } else {
                chooseImageGallery()
            }
        } else {
            chooseImageGallery()
        }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent,
            IMAGE_PICK_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseImageGallery()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Sätter in bilden i ImageView och sätter datan till en global variabel
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageView2.setImageURI(data?.data)
            imageUri = data?.data
            println("!!!${imageUri}")
        }
    }

    //Laddar upp den valda bilder till firebase Storage, visar en toast som berättar för användaren om bilden laddades upp
    /*private fun uploadImage() {

        if (imageUri != null) {
            val docRef = storageReference?.child("ItemsUpload/itemImage" + uid)
            docRef?.putFile(imageUri!!)
                ?.addOnSuccessListener {
                    Toast.makeText(
                        applicationContext,
                        "Image Uploaded",
                        Toast.LENGTH_LONG
                    ).show()
                    //backToRecyclerView()

                }?.addOnFailureListener {
                    Toast.makeText(
                        applicationContext,
                        "Image NOT uploaded",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    } */
    
    private fun backToRecyclerView() {
        val intent = Intent(this, FirstPage::class.java)
        startActivity(intent)
        finish()
    }

}



