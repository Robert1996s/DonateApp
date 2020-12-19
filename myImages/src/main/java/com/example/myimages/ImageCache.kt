package com.example.myimages

import android.graphics.Bitmap

interface ImageCache {

    interface ImageCache {
        fun put(url: String, bitmap: Bitmap)
        fun get(url: String): Bitmap?
        fun clear()
    }
}