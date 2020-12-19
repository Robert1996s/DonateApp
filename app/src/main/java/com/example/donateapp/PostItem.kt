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
import android.util.LruCache
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.view.isEmpty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detal_information.view.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_post_item.*
import kotlinx.android.synthetic.main.row_card.*


class PostItem : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    private var imageUri: Uri? = null
    private var uid = ""
    private var firebaseStorage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private lateinit var memoryCache: LruCache<String, Bitmap>

    companion object {
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_item)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        firebaseStorage = FirebaseStorage.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            uid = auth.currentUser!!.uid
        }
        val addPicture = findViewById<Button>(R.id.addPicture)
        val addDonateItem = findViewById<Button>(R.id.add_donate)


        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, value: Bitmap?): Int {
                return super.sizeOf(key, value)
            }
        }


        addDonateItem.setOnClickListener {
            addDonatePost()
            uploadImage()
        }


        addPicture.setOnClickListener {

            addPicture()
        }

    }

    private fun addDonatePost() {
        val itemTitle = editTextDonate.text.toString()
        val itemAdress = editTextLocation.text.toString()
        val itemDescription = editTextFullDescription.text.toString()
        val imageData = imageUri.toString()

        if (itemTitle.isEmpty() || itemAdress.isEmpty() || itemDescription.isEmpty()) {
            Toast.makeText(applicationContext, "Wrong Input", Toast.LENGTH_SHORT).show()

        } else {
            val item = Items(itemTitle, itemDescription, itemAdress, imageData)
            db.collection("items").add(item)
            db.collection("users").document(uid).collection("userItems").add(item)
        }

        // För att användaren inte ska vara kvar på sidan efter ha lagt till på knappen ska vyn försvinna. Kan skapa ett intent som skickar tillbaka till
        // den sida man ska komma till och gör en finish()

    }

    private fun addPicture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
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
        startActivityForResult(intent, IMAGE_PICK_CODE)
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
    private fun uploadImage() {

        if (imageUri != null) {
            val docRef = storageReference?.child("ItemsUpload/itemImage" + uid)
            docRef?.putFile(imageUri!!)
                ?.addOnSuccessListener {
                    Toast.makeText(
                        applicationContext,
                        "Image Uploaded",
                        Toast.LENGTH_LONG
                    ).show()
                    backToRecyclerView()

                }?.addOnFailureListener {
                    Toast.makeText(
                        applicationContext,
                        "Image NOT uploaded",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
    private fun backToRecyclerView() {
        val intent = Intent(this, FirstPage::class.java)
        startActivity(intent)
    }
}



