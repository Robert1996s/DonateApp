package com.example.donateapp.Models

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkHandler {

    companion object {
        fun isOnline(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        println("!!! Internet: CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        println("!!! Internet: WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        println("!!! Internet: ETHERNET")
                        return true
                    }
                }
            }
            return false
        }
    }
}