package com.example.donateapp.Models

import com.example.donateapp.DataClasses.Items
import com.example.donateapp.LruChache
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.firebase.firestore.FirebaseFirestore

class CacheData {

    lateinit var db: FirebaseFirestore
    private var jsonStr = ""
    private val cacheKeys = mutableListOf<String>()
    private var cacheItemList = mutableListOf<Items>()
    private var cacheItemJson = mutableListOf<String>()
    private val cache = LruChache<String>(8)

    fun cacheData() {
        db = FirebaseFirestore.getInstance()
        val mapper = jacksonObjectMapper()
        var cacheKey = 1
        val docRef = db.collection("items")
        docRef.addSnapshotListener { snapshot, e ->
            if (snapshot != null) {
                for (document in snapshot.documents) {
                    val item = document.toObject(Items::class.java)
                    if (item != null) {
                        jsonStr = mapper.writeValueAsString(item)
                        cache.put(cacheKey.toString(), jsonStr)
                        cacheKeys.add(cacheKey.toString())
                        cacheItemJson.add(jsonStr)
                    }
                    //adapter.notifyDataSetChanged()
                    cacheKey++
                }
            }
        }
    }

     fun getCacheData () :MutableList<Items> {
        val mapper = jacksonObjectMapper()
        var position = 0
        for (i in cacheItemJson) {
            var cacheItem = mapper.readValue<Items>(cacheItemJson[position])
            cacheItemList.add(cacheItem)
            position++
        }
        return cacheItemList
    }
}