package com.example.donateapp

import android.util.LruCache
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

object CacheHandler {

    private lateinit var memoryCache: LruCache<String, String>
    private lateinit var db: FirebaseFirestore

    fun createCache() {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8

        //memoryCache = object : LruChache<String, String>(cacheSize)
    }
}